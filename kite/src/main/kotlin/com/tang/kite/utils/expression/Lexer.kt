package com.tang.kite.utils.expression

/**
 * Lexer: Tokenizes the input string.
 *
 * @author Tang
 */
class Lexer(private val input: String) {

    private var pos = 0

    private val tokens = mutableListOf<Token>()

    /**
     * Tokenize the input string into a list of tokens.
     */
    fun tokenize(): List<Token> {
        while (pos < input.length) {
            when (val c = input[pos]) {
                ' ', '\t', '\n' -> pos++
                '(', ')', ',', '.', '[', ']', '%', '^' -> {
                    tokens.add(Token(TokenType.fromChar(c)!!))
                    pos++
                }
                '=', '!', '>', '<', '&', '|', '+', '-', '*', '/' -> handleOperators()
                '"', '\'' -> handleString()
                in '0'..'9' -> handleNumber()
                in 'a'..'z', in 'A'..'Z', '_' -> handleIdentifier()
                else -> throw IllegalArgumentException("Unexpected character: '$c'")
            }
        }
        return tokens
    }

    /**
     * Handle multi-character and single-character operators.
     */
    private fun handleOperators() {
        val twoCharOp = when {
            pos + 1 < input.length -> input.substring(pos, pos + 2)
            else -> null
        }

        when (twoCharOp) {
            "==", "!=", ">=", "<=", "&&", "||" -> {
                tokens.add(Token(TokenType.fromString(twoCharOp)!!))
                pos += 2
                return
            }
        }

        when (input[pos]) {
            '>' -> tokens.add(Token(TokenType.GT))
            '<' -> tokens.add(Token(TokenType.LT))
            '!' -> tokens.add(Token(TokenType.NOT))
            else -> tokens.add(Token(TokenType.fromChar(input[pos])!!))
        }
        pos++
    }

    /**
     * Handle string literals.
     */
    private fun handleString() {
        val quote = input[pos++]
        val sb = StringBuilder()
        while (pos < input.length && input[pos] != quote) {
            if (input[pos] == '\\') {
                sb.append(escapeSequence(input[++pos]))
            } else {
                sb.append(input[pos])
            }
            pos++
        }
        if (pos >= input.length) throw IllegalArgumentException("Unterminated string")
        tokens.add(Token(TokenType.STRING, sb.toString()))
        pos++
    }

    /**
     * Handle escape sequences in strings.
     */
    private fun escapeSequence(c: Char): Char = when (c) {
        'n' -> '\n'
        't' -> '\t'
        'r' -> '\r'
        'b' -> '\b'
        else -> c
    }

    /**
     * Handle numeric literals (int and double).
     */
    private fun handleNumber() {
        val start = pos
        while (pos < input.length && input[pos].isDigit()) pos++
        if (pos < input.length && input[pos] == '.') {
            pos++
            while (pos < input.length && input[pos].isDigit()) pos++
            tokens.add(Token(TokenType.NUMBER, input.substring(start, pos).toDouble()))
        } else {
            tokens.add(Token(TokenType.NUMBER, input.substring(start, pos).toInt()))
        }
    }

    /**
     * Handle identifiers (variables, function names).
     */
    private fun handleIdentifier() {
        val start = pos
        while (pos < input.length && (input[pos].isLetterOrDigit() || input[pos] == '_')) pos++
        val value = input.substring(start, pos)
        when (value) {
            "true" -> tokens.add(Token(TokenType.BOOLEAN, true))
            "false" -> tokens.add(Token(TokenType.BOOLEAN, false))
            "null" -> tokens.add(Token(TokenType.NULL, null))
            else -> tokens.add(Token(TokenType.IDENTIFIER, value))
        }
    }

}
