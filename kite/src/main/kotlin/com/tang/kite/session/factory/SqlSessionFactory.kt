package com.tang.kite.session.factory

import com.tang.kite.session.SqlSession
import com.tang.kite.session.TransactionIsolationLevel

/**
 * SQL session factory
 *
 * @author Tang
 */
interface SqlSessionFactory {

    /**
     * Open a new session
     */
    fun openSession(): SqlSession

    /**
     * Open a new session
     *
     * @param autoCommit auto commit
     */
    fun openSession(autoCommit: Boolean): SqlSession

    /**
     * Open a new session
     *
     * @param isolationLevel transaction isolation level
     */
    fun openSession(isolationLevel: TransactionIsolationLevel): SqlSession

}
