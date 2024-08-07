package com.tang.jkorm.session.factory.defaults

import com.tang.jkorm.executor.defaults.DefaultExecutorFactory
import com.tang.jkorm.session.Configuration
import com.tang.jkorm.session.SqlSession
import com.tang.jkorm.session.defaults.DefaultSqlSession
import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.transaction.jdbc.JdbcTransactionFactory

/**
 * @author Tang
 */
class DefaultSqlSessionFactory(private val configuration: Configuration) : SqlSessionFactory {

    override fun openSession(): SqlSession {
        return openSession(false)
    }

    override fun openSession(autoCommit: Boolean): SqlSession {
        val transaction = JdbcTransactionFactory().newTransaction(configuration.dataSource, null, autoCommit)
        val executor = DefaultExecutorFactory().newExecutor(configuration, transaction)
        return DefaultSqlSession(configuration, executor)
    }

}
