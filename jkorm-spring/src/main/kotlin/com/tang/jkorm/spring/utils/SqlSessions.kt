package com.tang.jkorm.spring.utils

import com.tang.jkorm.logging.LOGGER
import com.tang.jkorm.session.SqlSession
import com.tang.jkorm.session.factory.SqlSessionFactory

/**
 * @author Tang
 */
object SqlSessions {

    fun openSession(sqlSessionFactory: SqlSessionFactory): SqlSession {
        LOGGER.info("Open a new sql session")
        return sqlSessionFactory.openSession()
    }

    fun closeSession(sqlSession: SqlSession) {
        LOGGER.info("Close the sql session")
        sqlSession.close()
    }

}
