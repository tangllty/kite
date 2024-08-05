package com.tang.jkorm.io

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tang
 */
class ResourcesTest {

    @Test
    fun getResourceAsStream() {
        val resourceAsStream = Resources.getResourceAsStream("jkorm-config.yml")
        assertNotNull(resourceAsStream)
    }

}
