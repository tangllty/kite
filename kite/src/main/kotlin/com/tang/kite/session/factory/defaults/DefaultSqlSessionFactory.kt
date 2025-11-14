package com.tang.kite.session.factory.defaults

import com.tang.kite.executor.defaults.DefaultExecutorFactory
import com.tang.kite.session.Configuration
import com.tang.kite.session.SqlSession
import com.tang.kite.enumeration.transaction.TransactionIsolationLevel
import com.tang.kite.session.defaults.DefaultSqlSession
import com.tang.kite.session.factory.SqlSessionFactory

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
        val transactionFactory = configuration.transactionFactory
        val transaction = transactionFactory.newTransaction(configuration.dataSource, isolationLevel, autoCommit)
        val executor = DefaultExecutorFactory().newExecutor(transaction)
        return DefaultSqlSession(executor, configuration.sqlDialect)
    }

}
