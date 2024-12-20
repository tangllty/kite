package com.tang.jkorm.spring.utils

import com.tang.jkorm.logging.LOGGER
import com.tang.jkorm.session.SqlSession
import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.spring.holder.SqlSessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager

/**
 * @author Tang
 */
object SqlSessions {

    fun openSession(sqlSessionFactory: SqlSessionFactory): SqlSession {
        if (TransactionSynchronizationManager.hasResource(sqlSessionFactory)) {
            LOGGER.info("Get the sql session from the transaction synchronization manager")
            return (TransactionSynchronizationManager.getResource(sqlSessionFactory) as SqlSessionHolder).sqlSession
        }
        val session = sqlSessionFactory.openSession()
        TransactionSynchronizationManager.bindResource(sqlSessionFactory, SqlSessionHolder(session))
        LOGGER.info("Open a new sql session")
        return session
    }

    fun closeSession(sqlSessionFactory: SqlSessionFactory, sqlSession: SqlSession) {
        val holder = TransactionSynchronizationManager.getResource(sqlSessionFactory) as SqlSessionHolder?
        if (holder != null && holder.sqlSession == sqlSession) {
            holder.released()
        } else {
            LOGGER.info("Close the sql session")
            sqlSession.close()
        }
    }

}
