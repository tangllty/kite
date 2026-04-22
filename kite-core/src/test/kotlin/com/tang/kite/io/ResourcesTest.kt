package com.tang.kite.io

import com.tang.kite.datasource.pooled.PooledProperties
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * @author Tang
 */
class ResourcesTest {

    private val resource = "kite-config.yml"

    private val multipleDatasourceResource = "kite-multiple-datasource-config.yml"

    @Test
    fun getResourceAsStream() {
        val inputStream = Resources.getResourceAsStream(resource)
        assertNotNull(inputStream)
    }

    @Test
    fun getKiteProperties() {
        val inputStream = Resources.getResourceAsStream(resource)
        val kiteProperties = Resources.getKiteProperties(inputStream)
        assertNotNull(kiteProperties)
    }

    @Test
    fun getKiteAsObject() {
        val inputStream = Resources.getResourceAsStream(resource)
        val kiteProperties = Resources.getKiteAsObject(inputStream, TestKiteProperties::class)
        assertNotNull(kiteProperties)
    }

    @Test
    fun getDataSourceProperties() {
        val inputStream = Resources.getResourceAsStream(resource)
        val dataSourceProperties = Resources.getDataSourceProperties(inputStream)
        assertNotNull(dataSourceProperties)
    }

    @Test
    fun getMultipleDatasourceProperties() {
        val inputStream = Resources.getResourceAsStream(multipleDatasourceResource)
        val multipleDatasourceProperties = Resources.getDataSourceProperties(inputStream, PooledProperties::class.java)
        assertNotNull(multipleDatasourceProperties["ds1"])
        assertNotNull(multipleDatasourceProperties["ds2"])
    }

}
