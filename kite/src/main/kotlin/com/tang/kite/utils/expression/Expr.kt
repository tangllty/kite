package com.tang.kite.utils.expression

/**
 * AST Node Definitions.
 *
 * @author Tang
 */
sealed class Expr {

    /**
     * Literal value (number, string, boolean, null).
     */
    data class Literal(val value: Any?) : Expr()

    /**
     * Variable reference.
     */
    data class Variable(val name: String) : Expr()

    /**
     * Property access (e.g., user.age).
     */
    data class Property(val target: Expr, val name: String) : Expr()

    /**
     * Method call (e.g., user.name.length()).
     */
    data class MethodCall(val target: Expr, val name: String, val arguments: List<Expr>) : Expr()

    /**
     * Unary operator.
     */
    data class Unary(val operator: TokenType, val right: Expr) : Expr()

    /**
     * Binary operator.
     */
    data class Binary(val left: Expr, val operator: TokenType, val right: Expr) : Expr()

    /**
     * Function call (reserved).
     */
    data class FunctionCall(val functionName: String, val arguments: List<Expr>) : Expr()

    /**
     * Index access (e.g. arr[0], user.profile.tags[1])
     */
    data class Index(val target: Expr, val index: Expr) : Expr()

}
