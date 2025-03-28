package com.tang.kite

import com.tang.kite.datasource.unpooled.UnpooledDataSourceFactory
import com.tang.kite.io.Resources
import com.tang.kite.session.factory.SqlSessionFactoryBuilder
import jakarta.servlet.http.HttpServletRequestWrapper
import org.junit.jupiter.api.BeforeAll
import org.yaml.snakeyaml.Yaml
import javax.sql.DataSource

/**
 * @author Tang
 */
open class BaseDataTest {

    private fun createDataSource(): DataSource {
        val inputStream = Resources.getResourceAsStream("kite-config.yml")
        val yaml = Yaml().load<Map<String, Map<String, Map<String, String>>>>(inputStream)
        val kite = yaml["kite"]
        val datasource = kite!!["datasource"] as Map<String, String>
        return UnpooledDataSourceFactory(datasource).getDataSource()
    }

    fun createDatabase() {
        val dataSource = createDataSource()
        val database = Resources.getResourceAsStream("kite-test.sql")
        val connection = dataSource.connection
        val statement = connection.createStatement()
        database.bufferedReader().useLines { lines ->
            val sqlLines = ArrayList<String>()
            val sqlBuilder = StringBuilder()
            lines.forEach { line ->
                if (line.isBlank() || line.startsWith("--")) {
                    return@forEach
                }
                sqlBuilder.append(line)
                if (line.endsWith(";")) {
                    sqlLines.add(sqlBuilder.toString())
                    sqlBuilder.clear()
                }
            }
            sqlLines.forEach { sql ->
                try {
                    statement.execute(sql.replace(";", ""))
                } catch (_: Exception) {}
            }
        }
        statement.close()
        connection.close()
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun setup() {
            val baseDataTest = BaseDataTest()
            baseDataTest.createDatabase()
        }

        private val resource = Resources.getResourceAsStream("kite-config.yml")

        val sqlSessionFactoryBuild = SqlSessionFactoryBuilder().build(resource)

        val sqlSessionFactory inline get() = sqlSessionFactoryBuild

        val request = HttpServletRequestWrapper(MockHttpServletRequest())

    }

}
