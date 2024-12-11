package com.tang.jkorm.session.factory.defaults

import com.tang.jkorm.executor.defaults.DefaultExecutorFactory
import com.tang.jkorm.session.Configuration
import com.tang.jkorm.session.SqlSession
import com.tang.jkorm.session.TransactionIsolationLevel
import com.tang.jkorm.session.defaults.DefaultSqlSession
import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.transaction.jdbc.JdbcTransactionFactory

/**
 * Default SQL session factory
 *
 * @author Tang
 */
class DefaultSqlSessionFactory(private val configuration: Configuration) : SqlSessionFactory {

    /**
     * Open a new session
     *
     * @return [SqlSession]
     */
    override fun openSession(): SqlSession {
        return openSession(false)
    }

    /**
     * Open a new session
     *
     * @param autoCommit auto commit
     * @return [SqlSession]
     */
    override fun openSession(autoCommit: Boolean): SqlSession {
        return openSession(null, autoCommit)
    }

    /**
     * Open a new session
     *
     * @param isolationLevel transaction isolation level
     * @return [SqlSession]
     */
    override fun openSession(isolationLevel: TransactionIsolationLevel): SqlSession {
        return openSession(isolationLevel, false)
    }

    /**
     * Open a new session
     *
     * @param isolationLevel transaction isolation level
     * @param autoCommit auto commit
     * @return [SqlSession]
     */
    private fun openSession(isolationLevel: TransactionIsolationLevel?, autoCommit: Boolean): SqlSession {
        val transaction = JdbcTransactionFactory().newTransaction(configuration.dataSource, isolationLevel, autoCommit)
        val executor = DefaultExecutorFactory().newExecutor(configuration, transaction)
        return DefaultSqlSession(configuration, executor, configuration.sqlProvider)
    }

}
