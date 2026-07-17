package com.tang.kite.sql.function

/**
 * Comma-separated arguments for SQL function
 *
 * @author Tang
 */
class CommaArgs(private val args: MutableList<FunctionRender>) : FunctionRender {

    constructor(vararg args: FunctionRender) : this(args.toMutableList())

    override fun render(): String {
        return args.joinToString(", ") { it.render() }
    }

}
