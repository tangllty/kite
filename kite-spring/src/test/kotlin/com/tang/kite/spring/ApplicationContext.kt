package com.tang.kite.spring

import com.tang.kite.datasource.DataSourceRegistry
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * @author Tang
 */
object ApplicationContext {

    val context = AnnotationConfigApplicationContext(ApplicationConfig::class.java)

    init {
        val baseDataTest = BaseDataTest()
        baseDataTest.createDatabase()
        DataSourceRegistry.clear()
    }

}
