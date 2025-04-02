package com.tang.kite.io

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tang
 */
class ResourcesTest {

    private val resource = "kite-config.yml"

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
        val kiteProperties = Resources.getKiteAsObject(inputStream, TestKiteProperties::class.java)
        assertNotNull(kiteProperties)
    }

    @Test
    fun getDataSourceProperties() {
        val inputStream = Resources.getResourceAsStream(resource)
        val dataSourceProperties = Resources.getDataSourceProperties(inputStream)
        assertNotNull(dataSourceProperties)
    }

}
