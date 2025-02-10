package com.tang.kite.transaction

import java.sql.Connection

/**
 * @author Tang
 */
interface Transaction : AutoCloseable {

    fun getConnection(): Connection

    fun commit()

    fun rollback()

    override fun close()

}
