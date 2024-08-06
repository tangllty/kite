package com.tang.jkorm

import com.tang.jkorm.datasource.defaults.DefaultDataSourceFactory
import com.tang.jkorm.io.Resources
import com.tang.jkorm.session.factory.SqlSessionFactoryBuilder
import com.tang.jkorm.session.mapper.AccountMapper
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.yaml.snakeyaml.Yaml
import javax.sql.DataSource
import kotlin.test.assertEquals

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

    @Test
    fun createDatabase() {
        val dataSource = createDataSource()
        val database = Resources.getResourceAsStream("jkorm-test.sql")
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
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
                        statement.addBatch(sql.replace(";", ""))
                    }
                }
                statement.executeBatch()
            }
        }
    }

    @Test
    fun select() {
        createDatabase()
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val openSession = sqlSessionFactory.openSession()
        val accountMapper = openSession.getMapper(AccountMapper::class.java)
        val list = accountMapper.select()
        assertEquals(2, list.size)
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
