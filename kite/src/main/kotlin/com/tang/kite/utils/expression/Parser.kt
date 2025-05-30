package com.tang.kite.utils.expression

/**
 * Parser: Builds AST from tokens.
 *
 * @author Tang
 */
class Parser(private val tokens: List<Token>) {

    private var current = 0

    /**
     * Parse the top-level expression.
     */
    fun parseExpression(): Expr = logicalOr()

    /**
     * Parse logical OR expressions.
     */
    private fun logicalOr(): Expr {
        var expr = logicalAnd()
        while (match(TokenType.OR)) {
            expr = Expr.Binary(expr, TokenType.OR, logicalAnd())
        }
        return expr
    }

    /**
     * Parse logical AND expressions.
     */
    private fun logicalAnd(): Expr {
        var expr = equality()
        while (match(TokenType.AND)) {
            expr = Expr.Binary(expr, TokenType.AND, equality())
        }
        return expr
    }

    /**
     * Parse equality expressions (==, !=).
     */
    private fun equality(): Expr {
        var expr = comparison()
        while (match(TokenType.EQ, TokenType.NEQ)) {
            val operator = previous().type
            expr = Expr.Binary(expr, operator, comparison())
        }
        return expr
    }

    /**
     * Parse comparison expressions (>, <, >=, <=).
     */
    private fun comparison(): Expr {
        var expr = addition()
        while (match(TokenType.GT, TokenType.LT, TokenType.GTE, TokenType.LTE)) {
            val operator = previous().type
            expr = Expr.Binary(expr, operator, addition())
        }
        return expr
    }

    /**
     * Parse addition and subtraction.
     */
    private fun addition(): Expr {
        var expr = multiplication()
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            val operator = previous().type
            expr = Expr.Binary(expr, operator, multiplication())
        }
        return expr
    }

    /**
     * Parse multiplication, division, and modulo.
     */
    private fun multiplication(): Expr {
        var expr = power()
        while (match(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.MOD)) {
            val operator = previous().type
            expr = Expr.Binary(expr, operator, power())
        }
        return expr
    }

    /**
     * Parse power operator (^).
     */
    private fun power(): Expr {
        var expr = unary()
        while (match(TokenType.POW)) {
            val operator = previous().type
            expr = Expr.Binary(expr, operator, unary())
        }
        return expr
    }

    /**
     * Parse unary operators (!, -).
     */
    private fun unary(): Expr {
        if (match(TokenType.NOT, TokenType.MINUS)) {
            val operator = previous().type
            return Expr.Unary(operator, unary())
        }
        return primary()
    }

    /**
     * Parse primary expressions: literals, variables, parenthesis, property/method calls.
     */
    private fun primary(): Expr {
        return when {
            match(TokenType.NUMBER) -> Expr.Literal(previous().value)
            match(TokenType.STRING) -> Expr.Literal(previous().value)
            match(TokenType.BOOLEAN) -> Expr.Literal(previous().value)
            match(TokenType.NULL) -> Expr.Literal(null)
            match(TokenType.IDENTIFIER) -> handlePropertyOrCallOrIndex()
            match(TokenType.LPAREN) -> handleParenthesized()
            else -> throw IllegalArgumentException("Unexpected token: ${peek()}")
        }
    }

    /**
     * Handle property access, method calls, and index access (supports nesting).
     * e.g. arr[0], user.profile.tags[1]
     */
    private fun handlePropertyOrCallOrIndex(): Expr {
        var expr: Expr = Expr.Variable(previous().value as String)
        loop@ while (true) {
            when {
                match(TokenType.DOT) -> {
                    if (match(TokenType.IDENTIFIER)) {
                        val name = previous().value as String
                        if (match(TokenType.LPAREN)) {
                            val args = mutableListOf<Expr>()
                            if (!check(TokenType.RPAREN)) {
                                do {
                                    args.add(parseExpression())
                                } while (match(TokenType.COMMA))
                            }
                            consume(TokenType.RPAREN, "Expect ')' after function arguments")
                            expr = Expr.MethodCall(expr, name, args)
                        } else {
                            expr = Expr.Property(expr, name)
                        }
                    } else {
                        throw IllegalArgumentException("Expect property or method name after '.'")
                    }
                }
                match(TokenType.LBRACKET) -> {
                    val indexExpr = parseExpression()
                    consume(TokenType.RBRACKET, "Expect ']' after index")
                    expr = Expr.Index(expr, indexExpr)
                }
                else -> break@loop
            }
        }
        return expr
    }

    /**
     * Handle parenthesized expressions.
     */
    private fun handleParenthesized(): Expr {
        val expr = parseExpression()
        consume(TokenType.RPAREN, "Expect ')' after expression")
        return expr
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return false
        return peek().type == type
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun peek(): Token = tokens[current]

    private fun previous(): Token = tokens[current - 1]

    private fun isAtEnd(): Boolean = current >= tokens.size

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw IllegalArgumentException(message)
    }

}
