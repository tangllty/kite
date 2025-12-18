package com.tang.kite.utils.expression

import com.tang.kite.utils.Reflects
import kotlin.math.pow

/**
 * Evaluator: Evaluates the AST.
 *
 * @author Tang
 */
class Evaluator(private val context: Map<String, Any?>) {

    /**
     * Evaluate an expression node.
     */
    fun evaluate(expr: Expr): Any? = when (expr) {
        is Expr.Literal -> expr.value
        is Expr.Variable -> resolveVariable(expr.name)
        is Expr.Property -> evalProperty(expr)
        is Expr.MethodCall -> evalMethodCall(expr)
        is Expr.Unary -> evalUnary(expr)
        is Expr.Binary -> evalBinary(expr)
        is Expr.FunctionCall -> evalFunctionCall(expr)
        is Expr.Index -> evalIndex(expr)
    }

    /**
     * Resolve variable from context.
     */
    private fun resolveVariable(name: String): Any? {
        return Reflects.getValue(context, name)
    }

    /**
     * Evaluate property access.
     */
    private fun evalProperty(expr: Expr.Property): Any? {
        val target = evaluate(expr.target)
        return getProperty(target, expr.name)
    }

    /**
     * Get property value from object, map, or field.
     */
    private fun getProperty(target: Any?, name: String): Any? {
        if (target == null) throw IllegalArgumentException("Cannot access property '$name' of null")
        val kClass = target::class
        if (target is Map<*, *>) {
            return target[name]
        }
        val prop = kClass.members.firstOrNull { it.name == name }
        if (prop != null) {
            return prop.call(target)
        }
        try {
            val field = kClass.java.getDeclaredField(name)
            return Reflects.getValue(field, target)
        } catch (_: Exception) {}
        throw IllegalArgumentException("Property '$name' not found on ${kClass.simpleName}")
    }

    /**
     * Evaluate method call (supports contains, startsWith, endsWith, length, size).
     */
    private fun evalMethodCall(expr: Expr.MethodCall): Any? {
        val target = evaluate(expr.target)
        val args = expr.arguments.map { evaluate(it) }
        return when (expr.name) {
            "contains" -> when (target) {
                is String -> args.firstOrNull()?.let { target.contains(it.toString()) } ?: false
                is Iterable<*> -> args.firstOrNull()?.let { target.contains(it) } ?: false
                is Array<*> -> args.firstOrNull()?.let { target.contains(it) } ?: false
                is Map<*, *> -> args.firstOrNull()?.let { target.contains(it) } ?: false
                else -> throw IllegalArgumentException("Unsupported target for 'contains'")
            }
            "startsWith" -> when (target) {
                is String -> args.firstOrNull()?.let { target.startsWith(it.toString()) } ?: false
                else -> throw IllegalArgumentException("Unsupported target for 'startsWith'")
            }
            "endsWith" -> when (target) {
                is String -> args.firstOrNull()?.let { target.endsWith(it.toString()) } ?: false
                else -> throw IllegalArgumentException("Unsupported target for 'endsWith'")
            }
            "length", "size" -> getLengthOrSize(target)
            else -> {
                val kClass = target?.let { it::class }
                val method = kClass?.members?.firstOrNull { it.name == expr.name && it.parameters.size == args.size + 1 }
                if (method != null) {
                    Reflects.makeAccessible(method)
                    return method.call(target, *args.toTypedArray())
                }
                throw IllegalArgumentException("Unknown method: ${expr.name}")
            }
        }
    }

    /**
     * Unified length()/size() method for String, Collection, Map, Array.
     */
    private fun getLengthOrSize(target: Any?): Int {
        return when (target) {
            is String -> target.length
            is Collection<*> -> target.size
            is Iterable<*> -> target.toList().size
            is Map<*, *> -> target.size
            is Array<*> -> target.size
            else -> throw IllegalArgumentException("Cannot get length()/size() of ${target?.let { it::class.simpleName } ?: "null"}")
        }
    }

    /**
     * Evaluate unary operators.
     */
    private fun evalUnary(expr: Expr.Unary): Any {
        val right = evaluate(expr.right)
        return when (expr.operator) {
            TokenType.NOT -> !isTruthy(right)
            TokenType.MINUS -> when (right) {
                is Int -> -right
                is Double -> -right
                else -> throw IllegalArgumentException("Unsupported type for negation")
            }
            else -> throw IllegalArgumentException("Invalid unary operator")
        }
    }

    /**
     * Evaluate binary operators.
     */
    private fun evalBinary(expr: Expr.Binary): Any {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)

        return when (expr.operator) {
            TokenType.EQ -> isEqual(left, right)
            TokenType.NEQ -> !isEqual(left, right)
            TokenType.GT -> compare(left, right) > 0
            TokenType.LT -> compare(left, right) < 0
            TokenType.GTE -> compare(left, right) >= 0
            TokenType.LTE -> compare(left, right) <= 0
            TokenType.AND -> isTruthy(left) && isTruthy(right)
            TokenType.OR -> isTruthy(left) || isTruthy(right)
            TokenType.PLUS -> when {
                left is Number && right is Number -> if (left is Double || right is Double) left.toDouble() + right.toDouble() else left.toInt() + right.toInt()
                left is String || right is String -> left.toString() + right.toString()
                else -> throw IllegalArgumentException("Unsupported operands for addition")
            }
            TokenType.MINUS -> arithmeticOp(left, right) { a, b -> a - b }
            TokenType.MULTIPLY -> arithmeticOp(left, right) { a, b -> a * b }
            TokenType.DIVIDE -> arithmeticOp(left, right) { a, b ->
                if (b == 0.0) throw ArithmeticException("Division by zero")
                else a / b
            }
            TokenType.MOD -> arithmeticOp(left, right) { a, b ->
                if (b == 0.0) throw ArithmeticException("Modulo by zero")
                else a % b
            }
            TokenType.POW -> arithmeticOp(left, right) { a, b ->
                a.pow(b)
            }
            else -> throw IllegalArgumentException("Invalid binary operator")
        }
    }

    /**
     * Helper for arithmetic operations.
     */
    private inline fun arithmeticOp(left: Any?, right: Any?, op: (Double, Double) -> Double): Any {
        return when {
            left is Number && right is Number -> {
                val l = left.toDouble()
                val r = right.toDouble()
                val result = op(l, r)
                if (left is Int && right is Int && result.isFinite()) result.toInt() else result
            }
            else -> throw IllegalArgumentException("Operands must be numbers")
        }
    }

    /**
     * Evaluate function calls (not used in main grammar, but reserved).
     */
    private fun evalFunctionCall(expr: Expr.FunctionCall): Any {
        val args = expr.arguments.map { evaluate(it) }
        val target = if (expr.functionName == "startsWith" || expr.functionName == "contains") {
            if (args.isEmpty()) throw IllegalArgumentException("Function ${expr.functionName} requires a target")
            args[0]
        } else {
            null
        }

        return when (expr.functionName) {
            "contains" -> when (target) {
                is String -> args.getOrNull(1)?.let { target.contains(it.toString()) } ?: false
                is Collection<*> -> args.getOrNull(1)?.let { target.contains(it) } ?: false
                else -> throw IllegalArgumentException("Unsupported target for 'contains'")
            }
            "startsWith" -> when (target) {
                is String -> args.getOrNull(1)?.let { target.startsWith(it.toString()) } ?: false
                else -> throw IllegalArgumentException("Unsupported target for 'startsWith'")
            }
            else -> throw IllegalArgumentException("Unknown function: ${expr.functionName}")
        }
    }

    /**
     * Evaluate index access, e.g. arr[0], map["key"], string[1]
     */
    private fun evalIndex(expr: Expr.Index): Any? {
        val target = evaluate(expr.target)
        val index = evaluate(expr.index)
        return when (target) {
            is List<*> -> (index as? Int)?.let { target[it] }
            is Iterable<*> -> (index as? Int)?.let { target.toList()[it] }
            is Array<*> -> (index as? Int)?.let { target[it] }
            is Map<*, *> -> target[index]
            is String -> (index as? Int)?.let { target[it] }
            else -> throw IllegalArgumentException("Cannot index type: ${target?.let { it::class.simpleName } ?: "null"}")
        }
    }

    /**
     * Check if a value is truthy.
     */
    private fun isTruthy(value: Any?): Boolean = when (value) {
        null -> false
        is Boolean -> value
        else -> true
    }

    /**
     * Check equality (supports null, numbers).
     * Throws if types are not compatible.
     */
    private fun isEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        if (a == null || b == null) return false
        if (a is Collection<*> && b !is Collection<*>) {
            throw IllegalArgumentException("Type error: Cannot compare collection '$a' with non-collection '$b'.")
        }
        if (b is Collection<*> && a !is Collection<*>) {
            throw IllegalArgumentException("Type error: Cannot compare collection '$b' with non-collection '$a'.")
        }
        if (a is Array<*> && b !is Array<*>) {
            throw IllegalArgumentException("Type error: Cannot compare array '$a' with non-array '$b'.")
        }
        if (b is Array<*> && a !is Array<*>) {
            throw IllegalArgumentException("Type error: Cannot compare array '$b' with non-array '$a'.")
        }
        if ((a is Collection<*> || a is Array<*>) && b is String) {
            throw IllegalArgumentException("Type error: Cannot compare collection/array with string.")
        }
        if ((b is Collection<*> || b is Array<*>) && a is String) {
            throw IllegalArgumentException("Type error: Cannot compare collection/array with string.")
        }
        return a == b || (a is Number && b is Number && a.toDouble() == b.toDouble())
    }

    /**
     * Compare two values (numbers, comparable).
     * Throws if types are not compatible.
     */
    private fun compare(a: Any?, b: Any?): Int {
        if (a == null && b == null) return 0
        if (a == null) return -1
        if (b == null) return 1
        if (a is Number && b is Number) {
            return a.toDouble().compareTo(b.toDouble())
        }
        if (a is Comparable<*> && b is Comparable<*> && a::class == b::class) {
            @Suppress("UNCHECKED_CAST")
            return (a as Comparable<Any>).compareTo(b)
        }
        // Type error for comparing incompatible types
        throw IllegalArgumentException("Type error: Cannot compare ${a.let { it::class.simpleName }} with ${b.let { it::class.simpleName }}.")
    }

}
