package com.tang.kite.io

import org.yaml.snakeyaml.Yaml
import java.io.InputStream

/**
 * @author Tang
 */
object Resources {

    fun getResourceAsStream(name: String): InputStream {
        return Resources::class.java.classLoader.getResourceAsStream(name)
            ?: throw IllegalArgumentException("Resource not found: $name")
    }

    fun getDataSourceProperties(inputStream: InputStream): Map<String, String> {
        val yaml = Yaml().load<Map<String, Map<String, Map<String, String>>>>(inputStream)
        val kite = yaml["kite"]
        val datasource = kite!!["datasource"] as Map<String, String>
        return datasource
    }

}
