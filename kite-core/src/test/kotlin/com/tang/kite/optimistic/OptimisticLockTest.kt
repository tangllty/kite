package com.tang.kite.optimistic

import com.tang.kite.BaseDataTest
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.optimistic.Version
import com.tang.kite.config.KiteConfig
import com.tang.kite.config.optimistic.defaults.DefaultOptimisticLockProcessor
import com.tang.kite.exception.OptimisticLockException
import com.tang.kite.optimistic.entity.OptimisticLockAccount
import com.tang.kite.optimistic.mapper.OptimisticLockAccountMapper
import com.tang.kite.session.entity.LogicalAccount
import com.tang.kite.session.entity.TenantAccount
import com.tang.kite.session.mapper.TenantAccountMapper
import com.tang.kite.tenant.TenantManager
import com.tang.kite.utils.Reflects
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
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
