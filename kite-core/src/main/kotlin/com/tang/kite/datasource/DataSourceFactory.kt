package com.tang.kite.datasource

import javax.sql.DataSource

/**
 * @author Tang
 */
interface DataSourceFactory<T> {

    fun getProperties(): T

    fun getDataSource(): DataSource

}
