package com.tang.jkorm.transaction

import java.sql.Connection
import java.sql.SQLException

/**
 * @author Tang
 */
interface Transaction {

    fun getConnection(): Connection

    fun commit()

    fun rollback()

    fun close()

}
