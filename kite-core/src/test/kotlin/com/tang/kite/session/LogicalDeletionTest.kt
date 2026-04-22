package com.tang.kite.session

import com.tang.kite.BaseDataTest
import com.tang.kite.logical.LogicalDeletionManager
import com.tang.kite.session.entity.LogicalAccount
import com.tang.kite.session.mapper.LogicalAccountMapper
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class LogicalDeletionTest : BaseDataTest() {

    @Test
    fun insert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(LogicalAccountMapper::class)
        LogicalDeletionManager.withLogical {
            val rows = accountMapper.insert(LogicalAccount(username = "logical-deletion1", password = "password"))
            assertTrue { rows != 0 }
        }
        session.rollback()
        session.close()
    }

    @Test
    fun batchInsert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(LogicalAccountMapper::class)
        LogicalDeletionManager.withLogical {
            val rows = accountMapper.batchInsert(listOf(
                LogicalAccount(username = "logical-deletion1", password = "password"),
                LogicalAccount(username = "logical-deletion2", password = "password")
            ))
            assertTrue { rows != 0 }
        }
        session.rollback()
        session.close()
    }

    @Test
    fun update() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(LogicalAccountMapper::class)
        LogicalDeletionManager.withLogical {
            val account = accountMapper.queryWrapper().eq(LogicalAccount::username, "user").one()
            val rows = accountMapper.updateSelective(LogicalAccount(id = account?.id, username = "user", password = "modified"))
            assertTrue { rows != 0 }
        }
        session.rollback()
        session.close()
    }

    @Test
    fun delete() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(LogicalAccountMapper::class)
        LogicalDeletionManager.withLogical {
            val beforeSize = accountMapper.count()
            val account = accountMapper.queryWrapper().eq(LogicalAccount::username, "user").one()
            accountMapper.deleteById(account?.id!!)
            val afterSize = accountMapper.count()
            assertTrue { beforeSize > afterSize }
        }
        session.rollback()
        session.close()
    }

}
