package com.tang.kite.session

import com.tang.kite.BaseDataTest
import com.tang.kite.config.tenant.TenantConfig
import com.tang.kite.config.tenant.TenantProcessor
import com.tang.kite.session.entity.LogicalAccount
import com.tang.kite.session.entity.TenantAccount
import com.tang.kite.session.mapper.TenantAccountMapper
import com.tang.kite.tenant.TenantManager
import java.lang.reflect.Field
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class TenantTest : BaseDataTest() {

    init {
        TenantConfig.tenantProcessor = object : TenantProcessor {
            override fun getTenantIds(field: Field): List<Any> {
                return listOf(1, 2)
            }
        }
    }

    @Test
    fun insert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(TenantAccountMapper::class)
        TenantManager.withTenant {
            val rows = accountMapper.insert(TenantAccount(username = "tenant1", password = "password"))
            assertTrue { rows != 0 }
        }
        session.rollback()
        session.close()
    }

    @Test
    fun batchInsert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(TenantAccountMapper::class)
        TenantManager.withTenant {
            val rows = accountMapper.batchInsert(listOf(
                TenantAccount(username = "tenant1", password = "password"),
                TenantAccount(username = "tenant2", password = "password")
            ))
            assertTrue { rows != 0 }
        }
        session.rollback()
        session.close()
    }

    @Test
    fun update() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(TenantAccountMapper::class)
        TenantManager.withTenant {
            accountMapper.insert(TenantAccount(username = "tenant", password = "password"))
            val account = accountMapper.queryWrapper().eq(LogicalAccount::username, "tenant").one()
            val rows = accountMapper.updateSelective(TenantAccount(id = account?.id, password = "modified"))
            assertTrue { rows != 0 }
        }
        session.rollback()
        session.close()
    }

    @Test
    fun delete() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(TenantAccountMapper::class)
        TenantManager.withTenant {
            accountMapper.insert(TenantAccount(username = "tenant", password = "password"))
            val beforeSize = accountMapper.count()
            val account = accountMapper.queryWrapper().eq(TenantAccount::username, "tenant").one()
            accountMapper.deleteById(account?.id!!)
            val afterSize = accountMapper.count()
            assertTrue { beforeSize > afterSize }
        }
        session.rollback()
        session.close()
    }

}
