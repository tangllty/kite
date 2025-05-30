package com.tang.kite.utils.expression

/**
 * Token types for the lexer and parser.
 *
 * @author Tang
 */
enum class TokenType {

    LPAREN, RPAREN, COMMA, DOT,
    LBRACKET, RBRACKET,
    PLUS, MINUS, MULTIPLY, DIVIDE, MOD, POW,
    NOT,
    EQ, NEQ, GT, LT, GTE, LTE,
    AND, OR,
    IDENTIFIER, STRING, NUMBER,
    BOOLEAN, NULL;

    companion object {

        /**
         * Map single character to token type.
         */
        fun fromChar(c: Char): TokenType? = when (c) {
            '(' -> LPAREN
            ')' -> RPAREN
            ',' -> COMMA
            '.' -> DOT
            '[' -> LBRACKET
            ']' -> RBRACKET
            '+' -> PLUS
            '-' -> MINUS
            '*' -> MULTIPLY
            '/' -> DIVIDE
            '%' -> MOD
            '^' -> POW
            '!' -> NOT
            else -> null
        }

        /**
         * Map string to token type.
         */
        fun fromString(s: String): TokenType? = when (s) {
            "==" -> EQ
            "!=" -> NEQ
            ">" -> GT
            "<" -> LT
            ">=" -> GTE
            "<=" -> LTE
            "&&" -> AND
            "||" -> OR
            else -> null
        }

    }

}
