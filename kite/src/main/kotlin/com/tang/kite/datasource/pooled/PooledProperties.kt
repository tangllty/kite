package com.tang.kite.datasource.pooled

import com.tang.kite.datasource.unpooled.UnpooledProperties

/**
 * @author Tang
 */
class PooledProperties : UnpooledProperties() {

    var initialConnections: Int = 5

    var maximumActiveConnections: Int = 10

    var minimumFreeConnections: Int = 2

    var maximumFreeConnections: Int = 5

}
