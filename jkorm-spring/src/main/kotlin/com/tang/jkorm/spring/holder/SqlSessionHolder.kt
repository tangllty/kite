package com.tang.jkorm.spring.holder

import com.tang.jkorm.session.SqlSession
import org.springframework.transaction.support.ResourceHolderSupport

/**
 * Sql session holder
 *
 * @author Tang
 */
class SqlSessionHolder(internal val sqlSession: SqlSession) : ResourceHolderSupport() {
}
