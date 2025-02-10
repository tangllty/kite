package com.tang.kite.io

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tang
 */
class ResourcesTest {

    @Test
    fun getResourceAsStream() {
        val resourceAsStream = Resources.getResourceAsStream("kite-config.yml")
        assertNotNull(resourceAsStream)
    }

}
