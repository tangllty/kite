package com.tang.kite.datasource.pooled

import java.sql.Connection

/**
 * @author Tang
 */
class PooledConnection(

    private val connection: Connection,

    private val pooledDataSource: PooledDataSource

) : Connection by connection {

    override fun close() {
        val isReleased = pooledDataSource.releaseConnection(this)
        if (isReleased.not()) {
            connection.close()
        }
    }

    override fun toString(): String {
        return connection.toString()
    }

}
