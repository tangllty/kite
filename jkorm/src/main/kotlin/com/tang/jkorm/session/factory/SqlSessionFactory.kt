package com.tang.jkorm.session.factory

import com.tang.jkorm.session.SqlSession

/**
 * @author Tang
 */
interface SqlSessionFactory {

    fun openSession(): SqlSession

    fun openSession(autoCommit: Boolean): SqlSession

}
