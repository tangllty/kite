package com.tang.jkorm.io

import java.io.InputStream

/**
 * @author Tang
 */
object Resources {

    fun getResourceAsStream(name: String): InputStream {
        return Resources::class.java.classLoader.getResourceAsStream(name)
            ?: throw IllegalArgumentException("Resource not found: $name")
    }

}
