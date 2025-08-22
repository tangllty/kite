package com.tang.kite.utils.parser

import com.tang.kite.annotation.Param
import com.tang.kite.sql.SqlStatement
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.expression.ExpressionParser
import java.lang.reflect.Parameter

/**
 * author Tang
 */
object SqlParser {

    fun parse(sql: String, params: Map<String, Any?>): SqlStatement {
        return parse(sql, params, true)
    }

    fun parse(sql: String, params: Map<String, Any?>, beautify: Boolean): SqlStatement {
        if (sql.isBlank()) {
            throw IllegalArgumentException("SQL must not be blank.")
        }
        val (rawSql, paramList) = parseSql(sql, params)
        val finalSql = autoAppendWhereIfNeeded(rawSql)
        if (!isValidSql(finalSql)) {
            throw IllegalArgumentException("SQL syntax error: invalid SQL, content: $finalSql")
        }
        val cleanedSql = if (beautify) beautifySql(finalSql) else cleanSql(finalSql)
        return SqlStatement(cleanedSql, paramList)
    }

    /**
     * Build a map of all available parameter names to their values.
     * Supports [Param] annotation and paramN format parameter names.
     */
    fun buildParamValueMap(parameters: Array<Parameter>, args: Array<out Any>?): Map<String, Any?> {
        if (args == null || args.isEmpty()) {
            return emptyMap()
        }
        val map = mutableMapOf<String, Any?>()
        parameters.forEachIndexed { index, param ->
            val ann = param.getAnnotation(Param::class.java)
            if (ann != null) {
                map[ann.value] = args[index]
            }
            map["param${index.plus(1)}"] = args[index]
        }
        if (args.size == 1) {
            when (val singleArg = args[0]) {
                is Map<*, *> -> {
                    singleArg.forEach { (key, value) ->
                        if (key is String) {
                            map[key] = value
                        }
                    }
                }
                else -> {
                    // If it's a single object, add its fields to the map
                    val fields = Reflects.getFields(singleArg.javaClass)
                    fields.forEach { field ->
                        Reflects.makeAccessible(field, singleArg)
                        map[field.name] = Reflects.getValue(singleArg, field.name)
                    }
                }
            }
        }
        return map
    }

    /**
     * Parses the SQL string, replacing placeholders with '?' and collecting parameters.
     */
    private fun parseSql(sql: String, params: Map<String, Any?>): Pair<String, MutableList<Any?>> {
        val sqlBuffer = StringBuilder()
        val paramList = mutableListOf<Any?>()
        var i = 0
        var parenCount = 0
        while (i < sql.length) {
            val c = sql[i]
            if (c == 'i' && startsWithIf(sql, i)) {
                val (ifSql, ifParams, endIdx) = parseIfBlock(sql, i, params)
                sqlBuffer.append(ifSql)
                paramList.addAll(ifParams)
                i = endIdx
                continue
            }
            when {
                c == '(' -> {
                    parenCount++
                    sqlBuffer.append(c)
                }
                c == ')' -> {
                    parenCount--
                    if (parenCount < 0) {
                        throw IllegalArgumentException("SQL syntax error: Unmatched ')' at position $i")
                    }
                    sqlBuffer.append(c)
                }
                c == '#' && i + 1 < sql.length && sql[i + 1] == '{' -> {
                    val (paramName, end) = extractPlaceholder(sql, i)
                    sqlBuffer.append("?")
                    paramList.add(resolveParam(paramName, params, i))
                    i = end - 1
                }
                else -> sqlBuffer.append(c)
            }
            i++
        }
        if (parenCount != 0) {
            throw IllegalArgumentException("SQL syntax error: unmatched '(' or ')' in SQL.")
        }
        return sqlBuffer.toString() to paramList
    }

    /**
     * 跳过 else if/else 分支，返回跳过后的索引
     */
    private fun skipElseBranches(sql: String, startIdx: Int): Int {
        var i = startIdx
        while (i < sql.length) {
            i = getNextNonWhitespaceIndex(sql, i)
            if (i >= sql.length) {
                break
            }
            if (!startsWithElse(sql, i)) {
                break
            }

            i += 4
            i = getNextNonWhitespaceIndex(sql, i)
            if (startsWithIf(sql, i)) {
                // 处理 else if
                i += 2
                i = getNextNonWhitespaceIndex(sql, i)
                if (i < sql.length && sql[i] == '(') {
                    i++
                    var condParen = 1
                    while (i < sql.length && condParen > 0) {
                        if (sql[i] == '(') {
                            condParen++
                        } else if (sql[i] == ')') {
                            condParen--
                        }
                        i++
                    }
                }
                i = getNextNonWhitespaceIndex(sql, i)
            }
            if (i < sql.length && sql[i] == '{') {
                i++
                var braceCount = 1
                while (i < sql.length && braceCount > 0) {
                    if (sql[i] == '{') {
                        braceCount++
                    } else if (sql[i] == '}') {
                        braceCount--
                    }
                    i++
                }
            }
        }
        return i
    }

    /**
     * Parses an "if" block in the SQL string, supporting else and else if.
     */
    private fun parseIfBlock(sql: String, startIdx: Int, params: Map<String, Any?>): Triple<String, List<Any?>, Int> {
        var i = startIdx + 2
        i = getNextNonWhitespaceIndex(sql, i)
        if (i >= sql.length || sql[i] != '(') {
            throw IllegalArgumentException("SQL syntax error: 'if' must be followed by '(' at position $startIdx")
        }

        i++ // skip '('
        val condStart = i
        var condParen = 1
        while (i < sql.length && condParen > 0) {
            if (sql[i] == '(') {
                condParen++
            } else if (sql[i] == ')') {
                condParen--
            }
            i++
        }
        if (condParen != 0) {
            throw IllegalArgumentException("SQL syntax error: unmatched '(' in if condition at position $startIdx")
        }

        val condEnd = i - 1
        val condition = sql.substring(condStart, condEnd).trim()
        if (condition.isEmpty()) {
            throw IllegalArgumentException("SQL syntax error: empty condition in if statement at position $startIdx")
        }

        i = getNextNonWhitespaceIndex(sql, i)
        if (i >= sql.length || sql[i] != '{') {
            throw IllegalArgumentException("SQL syntax error: 'if' must be followed by '{' after condition at position $startIdx")
        }

        i++ // skip '{'
        val sqlStart = i
        var braceCount = 1
        while (i < sql.length && braceCount > 0) {
            if (sql[i] == '{') {
                braceCount++
            } else if (sql[i] == '}') {
                braceCount--
            }
            i++
        }
        if (braceCount != 0) {
            throw IllegalArgumentException("SQL syntax error: unmatched '{' in if statement at position $startIdx")
        }

        val sqlEnd = i - 1
        val innerSql = sql.substring(sqlStart, sqlEnd)
        val condResult = try {
            ExpressionParser.evaluateBoolean(condition, params)
        } catch (e: Exception) {
            throw IllegalArgumentException("SQL syntax error: failed to evaluate if condition '$condition' at position $startIdx: ${e.message}", e)
        }
        if (condResult) {
            val (innerParsedSql, innerParams) = parseSql(innerSql, params)
            val nextIdx = skipElseBranches(sql, i)
            return Triple(innerParsedSql, innerParams, nextIdx)
        }

        var j = i
        while (j < sql.length) {
            j = getNextNonWhitespaceIndex(sql, j)
            if (j >= sql.length) {
                break
            }
            if (!startsWithElse(sql, j)) {
                break
            }

            j += 4
            j = getNextNonWhitespaceIndex(sql, j)
            if (startsWithIf(sql, j)) {
                val (elseIfSql, elseIfParams, nextIdx) = parseIfBlock(sql, j, params)
                return Triple(elseIfSql, elseIfParams, nextIdx)
            }
            if (j >= sql.length || sql[j] != '{') {
                throw IllegalArgumentException("SQL syntax error: 'else' must be followed by '{' at position $j")
            }

            j++ // skip '{'
            val elseStart = j
            var elseBrace = 1
            while (j < sql.length && elseBrace > 0) {
                if (sql[j] == '{') {
                    elseBrace++
                } else if (sql[j] == '}') {
                    elseBrace--
                }
                j++
            }
            if (elseBrace != 0) {
                throw IllegalArgumentException("SQL syntax error: unmatched '{' in else statement at position $elseStart")
            }

            val elseEnd = j - 1
            val elseSql = sql.substring(elseStart, elseEnd)
            val (elseParsedSql, elseParams) = parseSql(elseSql, params)
            return Triple(elseParsedSql, elseParams, j)
        }
        return Triple("", emptyList(), i)
    }

    /**
     * Automatically appends "WHERE" if the SQL does not contain it and has conditions.
     */
    private fun autoAppendWhereIfNeeded(sql: String): String {
        val lower = sql.lowercase()
        val hasWhere = Regex("\\bwhere\\b").containsMatchIn(lower)
        if (hasWhere) {
            return sql
        }

        // Find all condition fragments starting with and/or, and no where before
        val forbidden = setOf("order", "group", "having", "limit")
        val lines = sql.lines().toMutableList()
        var firstCondIdx = -1
        for ((idx, line) in lines.withIndex()) {
            val trimmed = line.trimStart().lowercase()
            val firstWord = Regex("^\\s*(\\w+)").find(trimmed)?.groupValues?.get(1) ?: ""
            if ((trimmed.startsWith("and ") || trimmed.startsWith("or "))
                && firstWord !in forbidden
            ) {
                firstCondIdx = idx
                break
            }
        }
        if (firstCondIdx != -1) {
            // Merge where
            val condLine = lines[firstCondIdx]
            val cleaned = condLine.trimStart().replace(Regex("^(and|or)\\s+", RegexOption.IGNORE_CASE), "")
            lines[firstCondIdx] = "where $cleaned"
            return lines.joinToString("\n")
        }
        return sql
    }

    /**
     * Extracts a placeholder from the SQL string, returning the parameter name and the end index.
     */
    private fun extractPlaceholder(sql: String, startIdx: Int): Pair<String, Int> {
        var end = startIdx + 2
        var braceCount = 1
        while (end < sql.length && braceCount > 0) {
            when (sql[end]) {
                '{' -> braceCount++
                '}' -> braceCount--
            }
            end++
        }
        if (braceCount != 0) {
            throw IllegalArgumentException("SQL syntax error: unmatched #{} placeholder at position $startIdx")
        }
        val paramName = sql.substring(startIdx + 2, end - 1).trim()
        if (paramName.isEmpty()) {
            throw IllegalArgumentException("SQL syntax error: empty parameter name in #{} at position $startIdx")
        }
        return paramName to end
    }

    /**
     * Resolves a parameter name to its value from the provided parameters map.
     * Supports nested properties using dot notation (e.g., "user.name").
     */
    private fun resolveParam(paramName: String, params: Map<String, Any?>, pos: Int): Any? {
        val rootKey = paramName.substringBefore('.')
        if (!params.containsKey(rootKey)) {
            throw IllegalArgumentException("SQL parameter error: parameter '$rootKey' not found at position $pos")
        }
        val rootObj = params[rootKey]
        return if (paramName.contains('.') && rootObj != null) {
            Reflects.getValue(rootObj, paramName.removePrefix("$rootKey."))
        } else {
            rootObj
        }
    }

    private fun beautifySql(sql: String): String {
        var beautified = sql.replace(Regex("\\s+"), " ").trim()
        val symbolPatterns = listOf(
            ">=", "<=", "<>", "!=", "=", ">", "<", "\\+", "\\*", "/",
        )
        for (symbol in symbolPatterns) {
            beautified = beautified.replace(Regex("\\s*($symbol)\\s*"), " $1 ")
        }
        beautified = beautified.replace(Regex("\\(\\s+"), "(")
        beautified = beautified.replace(Regex("\\s+\\)"), ")")
        beautified = beautified.replace(Regex("\\s+\\("), " (")
        beautified = beautified.replace(Regex("\\)\\s+"), ") ")
        beautified = beautified.replace(Regex("\\s+"), " ").trim()
        return beautified
    }

    private fun cleanSql(sql: String): String =
        sql.lines().map { it.trimEnd() }.filter { it.isNotBlank() }.joinToString("\n")

    private fun isValidSql(sql: String): Boolean {
        // Remove all if blocks for validation
        val cleaned = removeIfBlocks(sql).trim()
        // Accept if starts with select/insert/update/delete (case-insensitive, ignore leading whitespace)
        val validStart = Regex("^(select|insert|update|delete)\\b", RegexOption.IGNORE_CASE)
        if (!validStart.containsMatchIn(cleaned)) {
            return false
        }
        // Must contain at least one letter
        if (!Regex("[a-zA-Z]").containsMatchIn(cleaned)) {
            return false
        }
        // Allow ?, (), basic SQL symbols, and common punctuation
        if (Regex("[^\\w\\s,=><!*()?.'\"-_%]").containsMatchIn(cleaned)) {
            return false
        }
        return true
    }

    private fun removeIfBlocks(sql: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < sql.length) {
            if (startsWithIf(sql, i)) {
                var j = i + 2
                j = getNextNonWhitespaceIndex(sql, j)
                if (j < sql.length && sql[j] == '(') {
                    // skip condition
                    j++
                    var condParen = 1
                    while (j < sql.length && condParen > 0) {
                        if (sql[j] == '(') {
                            condParen++
                        } else if (sql[j] == ')') {
                            condParen--
                        }
                        j++
                    }
                    j = getNextNonWhitespaceIndex(sql, j)
                    if (j < sql.length && sql[j] == '{') {
                        j++
                        var braceCount = 1
                        while (j < sql.length && braceCount > 0) {
                            if (sql[j] == '{') {
                                braceCount++
                            } else if (sql[j] == '}') {
                                braceCount--
                            }
                            j++
                        }
                        i = j
                        continue
                    }
                }
            }
            sb.append(sql[i])
            i++
        }
        return sb.toString()
    }

    private fun getNextNonWhitespaceIndex(sql: String, startIdx: Int): Int {
        var i = startIdx
        while (i < sql.length && sql[i].isWhitespace()) {
            i++
        }
        return i
    }

    private fun startsWithIf(sql: String, index: Int): Boolean {
        return sql.startsWith("if", index, true)
    }

    private fun startsWithElse(sql: String, index: Int): Boolean {
        return sql.startsWith("else", index, true)
    }

}
