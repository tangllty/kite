package com.tang.jkorm.datasource

import javax.sql.DataSource

/**
 * @author Tang
 */
interface DataSourceFactory {

    fun getProperties(): Map<String, String>

    fun getDataSource(): DataSource

}
