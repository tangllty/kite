package com.tang.kite.sql.function

import java.io.Serializable

/**
 * Top-level interface for all SQL expressions nodes
 *
 * @author Tang
 */
interface FunctionRender : Serializable {

    fun render(): String

}
