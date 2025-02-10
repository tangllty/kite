package com.tang.kite.spring.transaction

import com.tang.kite.logging.LOGGER
import com.tang.kite.session.SqlSession
import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.holder.SqlSessionHolder
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

/**
 * Spring transaction synchronization
 *
 * @author Tang
 */
class SpringTransactionSynchronization(

    private val holder: SqlSessionHolder,

    private val sqlSessionFactory: SqlSessionFactory

) : TransactionSynchronization {

    private var holderActive = true

    private fun message(action: String, sqlSession: SqlSession) {
        LOGGER.debug("Transaction synchronization {} SqlSession[{}]", action, sqlSession)
    }

    private fun message(action: String, holder: SqlSessionHolder) {
        message(action, holder.sqlSession)
    }

    override fun getOrder(): Int {
        return DataSourceUtils.CONNECTION_SYNCHRONIZATION_ORDER.dec()
    }

    override fun suspend() {
        if (this.holderActive) {
            message("suspending", holder)
            TransactionSynchronizationManager.unbindResource(sqlSessionFactory)
        }
    }

    override fun resume() {
        if (this.holderActive) {
            message("resuming", holder)
            TransactionSynchronizationManager.bindResource(sqlSessionFactory, holder)
        }
    }

    override fun beforeCommit(readOnly: Boolean) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            message("committing", holder)
            holder.sqlSession.commit()
        }
    }

    override fun beforeCompletion() {
        if (!this.holder.isOpen) {
            message("deregistering", holder)
            this.holderActive = false
            message("closing", holder)
            holder.sqlSession.close()
        }
    }

    override fun afterCompletion(status: Int) {
        if (holderActive) {
            message("deregistering", holder)
            TransactionSynchronizationManager.unbindResourceIfPossible(sqlSessionFactory)
            this.holderActive = false
            message("closing", holder)
            holder.sqlSession.close()
        }
        this.holder.reset()
    }

}
