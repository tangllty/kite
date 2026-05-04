package com.tang.kite.optimistic

import com.tang.kite.BaseDataTest
import com.tang.kite.optimistic.entity.OptimisticLockAccount
import com.tang.kite.optimistic.mapper.OptimisticLockAccountMapper
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class OptimisticLockTest : BaseDataTest() {

    @Test
    fun insert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(OptimisticLockAccountMapper::class)
        OptimisticLockManager.withOptimisticLock {
            val rows = accountMapper.insert(OptimisticLockAccount(username = "optimistic-lock", password = "password"))
            assertTrue { rows != 0 }
        }
        session.rollback()
        session.close()
    }

    @Test
    fun update() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(OptimisticLockAccountMapper::class)
        OptimisticLockManager.withOptimisticLock {
            accountMapper.insert(OptimisticLockAccount(username = "optimistic-lock", password = "password"))
            val account = accountMapper.queryWrapper().eq(OptimisticLockAccount::username, "optimistic-lock").one()
            val rows = accountMapper.updateSelective(OptimisticLockAccount(id = account?.id, password = "modified", version = account?.version))
            assertTrue { rows != 0 }
        }
        session.rollback()
        session.close()
    }

}
