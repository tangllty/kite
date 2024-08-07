package com.tang.jkorm

import com.tang.jkorm.datasource.defaults.DefaultDataSourceFactory
import com.tang.jkorm.io.Resources
import com.tang.jkorm.session.factory.SqlSessionFactoryBuilder
import org.junit.jupiter.api.BeforeAll
import org.yaml.snakeyaml.Yaml
import javax.sql.DataSource

/**
 * @author Tang
 */
open class BaseDataTest {

    private fun createDataSource(): DataSource {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val yaml = Yaml().load<Map<String, Map<String, Map<String, String>>>>(inputStream)
        val jkorm = yaml["jkorm"]
        val datasource = jkorm!!["datasource"] as Map<String, String>
        return DefaultDataSourceFactory(datasource).getDataSource()
    }

    fun createDatabase() {
        val dataSource = createDataSource()
        val database = Resources.getResourceAsStream("jkorm-test.sql")
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
                println(sql)
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

        private val resource = Resources.getResourceAsStream("jkorm-config.yml")

        val sqlSessionFactoryBuild = SqlSessionFactoryBuilder().build(resource)

        val sqlSessionFactory inline get() = sqlSessionFactoryBuild

    }

}
