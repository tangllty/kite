package com.tang.kite.sql.function

/**
 * Space-separated arguments for SQL function
 *
 * @author Tang
 */
class SpaceArgs(private val args: MutableList<FunctionRender>) : FunctionRender {

    constructor(vararg args: FunctionRender) : this(args.toMutableList())

    override fun render(): String {
        return args.joinToString(" ") { it.render() }
    }

}
