package com.tang.kite.spring.holder

import com.tang.kite.session.SqlSession
import org.springframework.transaction.support.ResourceHolderSupport

/**
 * Sql session holder
 *
 * @author Tang
 */
class SqlSessionHolder(internal val sqlSession: SqlSession) : ResourceHolderSupport()
