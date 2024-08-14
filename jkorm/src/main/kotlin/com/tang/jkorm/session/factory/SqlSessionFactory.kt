package com.tang.jkorm.session.factory

import com.tang.jkorm.session.SqlSession

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

}
