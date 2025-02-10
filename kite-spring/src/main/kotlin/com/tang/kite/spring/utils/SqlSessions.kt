package com.tang.kite.spring.utils

import com.tang.kite.logging.LOGGER
import com.tang.kite.session.SqlSession
import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.holder.SqlSessionHolder
import com.tang.kite.spring.transaction.SpringTransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

/**
 * @author Tang
 */
object SqlSessions {

    fun openSession(sqlSessionFactory: SqlSessionFactory): SqlSession {
        if (TransactionSynchronizationManager.hasResource(sqlSessionFactory)) {
            val holder = TransactionSynchronizationManager.getResource(sqlSessionFactory) as SqlSessionHolder
            if (holder.isSynchronizedWithTransaction) {
                holder.requested()
                LOGGER.debug("Get the SqlSession[{}] from the transaction synchronization manager", holder.sqlSession)
                return holder.sqlSession
            }
        }
        LOGGER.debug("Open a new sql session")
        val session = sqlSessionFactory.openSession()
        registerTransactionSynchronization(sqlSessionFactory, SqlSessionHolder(session))
        return session
    }

    fun closeSession(sqlSessionFactory: SqlSessionFactory, sqlSession: SqlSession) {
        val holder = TransactionSynchronizationManager.getResource(sqlSessionFactory) as SqlSessionHolder?
        if (holder != null && holder.sqlSession == sqlSession) {
            LOGGER.debug("Releasing transaction synchronization for SqlSession[{}]", holder.sqlSession)
            holder.released()
        } else {
            LOGGER.debug("Close the sql session")
            sqlSession.close()
        }
    }

    private fun registerTransactionSynchronization(sqlSessionFactory: SqlSessionFactory, holder: SqlSessionHolder) {
        if (TransactionSynchronizationManager.isSynchronizationActive().not()) {
            return
        }
        LOGGER.debug("Registering transaction synchronization for SqlSession[{}]", holder.sqlSession)
        holder.requested()
        TransactionSynchronizationManager.bindResource(sqlSessionFactory, holder)
        TransactionSynchronizationManager.registerSynchronization(SpringTransactionSynchronization(holder, sqlSessionFactory))
        holder.isSynchronizedWithTransaction = true
        holder.requested()
    }

}
