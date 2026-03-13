package com.tang.kite.session

import com.tang.kite.BaseDataTest
import com.tang.kite.config.logical.LogicalDeleteConfig
import com.tang.kite.logical.delete.LogicalDeleteManager
import com.tang.kite.session.entity.LogicalAccount
import com.tang.kite.session.mapper.LogicalAccountMapper
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * @author Tang
 */
class LogicalDeleteTest : BaseDataTest() {

    @Test
    fun insert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(LogicalAccountMapper::class)
        LogicalDeleteManager.withLogical {
            accountMapper.insert(LogicalAccount(username = "logical-delete1", password = "password"))
        }
        session.rollback()
        session.close()
        assertNotNull(accountMapper)
    }

    @Test
    fun batchInsert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(LogicalAccountMapper::class)
        LogicalDeleteManager.withLogical {
            accountMapper.batchInsert(listOf(
                LogicalAccount(username = "logical-delete1", password = "password"),
                LogicalAccount(username = "logical-delete2", password = "password")
            ))
        }
        session.rollback()
        session.close()
        assertNotNull(accountMapper)
    }

    @Test
    fun update() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(LogicalAccountMapper::class)
        LogicalDeleteManager.withLogical {
            val account = accountMapper.queryWrapper().eq(LogicalAccount::username, "user").one()
            accountMapper.updateSelective(LogicalAccount(id = account?.id, username = "user", password = "modified"))
        }
        session.rollback()
        session.close()
        assertNotNull(accountMapper)
    }

    @Test
    fun delete() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(LogicalAccountMapper::class)
        LogicalDeleteManager.withLogical {
            val beforeSize = accountMapper.count()
            val account = accountMapper.queryWrapper().eq(LogicalAccount::username, "user").one()
            accountMapper.deleteById(account?.id!!)
            val afterSize = accountMapper.count()
            assert(beforeSize > afterSize)
        }
        session.rollback()
        session.close()
        assertNotNull(accountMapper)
    }

}
