package com.tang.jkorm.transaction

import java.sql.Connection

/**
 * @author Tang
 */
interface Transaction {

    fun getConnection(): Connection

    fun commit()

    fun rollback()

    fun close()

}
