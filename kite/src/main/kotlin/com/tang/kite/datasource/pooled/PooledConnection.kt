package com.tang.kite.datasource.pooled

import java.sql.Connection
import java.sql.ShardingKey

/**
 * A wrapper for a pooled connection that delegates all calls to the underlying connection.
 *
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

    override fun beginRequest() {
        connection.beginRequest()
    }

    override fun endRequest() {
        connection.endRequest()
    }

    override fun setShardingKeyIfValid(shardingKey: ShardingKey?, superShardingKey: ShardingKey?, timeout: Int): Boolean {
        return connection.setShardingKeyIfValid(shardingKey, superShardingKey, timeout)
    }

    override fun setShardingKeyIfValid(shardingKey: ShardingKey?, timeout: Int): Boolean {
        return connection.setShardingKeyIfValid(shardingKey, timeout)
    }

    override fun setShardingKey(shardingKey: ShardingKey?, superShardingKey: ShardingKey?) {
        connection.setShardingKey(shardingKey, superShardingKey)
    }

    override fun setShardingKey(shardingKey: ShardingKey?) {
        connection.setShardingKey(shardingKey)
    }

}
