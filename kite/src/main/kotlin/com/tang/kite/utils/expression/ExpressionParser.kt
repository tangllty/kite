package com.tang.kite.utils.expression

/**
 * ExpressionParser parses and evaluates expressions.
 *
 * @author Tang
 */
object ExpressionParser {

    /**
     * Parse an expression string into an AST (Expr).
     */
    fun parse(expression: String): Expr {
        val tokens = Lexer(expression).tokenize()
        return Parser(tokens).parseExpression()
    }

    /**
     * Evaluate an AST (Expr) with the given context.
     */
    fun evaluate(expr: Expr, context: Map<String, Any?>): Any? {
        return Evaluator(context).evaluate(expr)
    }

    /**
     * Evaluate an expression string with the given context.
     */
    fun evaluate(expression: String, context: Map<String, Any?>): Any? {
        val expr = parse(expression)
        return evaluate(expr, context)
    }

    /**
     * Evaluate an expression string and return the result as String.
     * Throws exception if the result is not a String.
     */
    fun evaluateString(expression: String, context: Map<String, Any?>): String {
        val result = evaluate(expression, context)
        return result as? String ?: throw IllegalArgumentException("Expression result is not a String: $result")
    }

    /**
     * Evaluate an expression string and return the result as Int.
     * Throws exception if the result is not an Int.
     */
    fun evaluateInt(expression: String, context: Map<String, Any?>): Int {
        return when (val result = evaluate(expression, context)) {
            is Int -> result
            is Number -> result.toInt()
            else -> throw IllegalArgumentException("Expression result is not an Int: $result")
        }
    }

    /**
     * Evaluate an expression string and return the result as Double.
     * Throws exception if the result is not a Double.
     */
    fun evaluateDouble(expression: String, context: Map<String, Any?>): Double {
        return when (val result = evaluate(expression, context)) {
            is Double -> result
            is Number -> result.toDouble()
            else -> throw IllegalArgumentException("Expression result is not a Double: $result")
        }
    }

    /**
     * Evaluate an expression string and return the result as Boolean.
     * Throws exception if the result is not a Boolean.
     */
    fun evaluateBoolean(expression: String, context: Map<String, Any?>): Boolean {
        val result = evaluate(expression, context)
        return result as? Boolean ?: throw IllegalArgumentException("Expression result is not a Boolean: $result")
    }

    /**
     * Evaluate an expression string and return the result as List.
     * Throws exception if the result is not a List.
     */
    fun evaluateList(expression: String, context: Map<String, Any?>): List<*> {
        val result = evaluate(expression, context)
        return result as? List<*>
            ?: throw IllegalArgumentException("Expression result is not a List: $result")
    }

    /**
     * Evaluate an expression string and return the result as Map.
     * Throws exception if the result is not a Map.
     */
    fun evaluateMap(expression: String, context: Map<String, Any?>): Map<*, *> {
        val result = evaluate(expression, context)
        return result as? Map<*, *> ?: throw IllegalArgumentException("Expression result is not a Map: $result")
    }

}
