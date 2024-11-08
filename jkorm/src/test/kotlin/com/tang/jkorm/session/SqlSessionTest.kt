package com.tang.jkorm.session

import com.tang.jkorm.BaseDataTest
import com.tang.jkorm.paginate.OrderItem
import com.tang.jkorm.session.entity.Account
import com.tang.jkorm.session.entity.Role
import com.tang.jkorm.session.mapper.AccountJavaMapper
import com.tang.jkorm.session.mapper.AccountMapper
import com.tang.jkorm.session.mapper.RoleMapper
import com.tang.jkorm.wrapper.query.QueryWrapper
import com.tang.jkorm.wrapper.update.UpdateWrapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.sql.Date
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class SqlSessionTest : BaseDataTest() {

    @Test
    fun getMapper() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        session.close()
        assertNotNull(accountMapper)
    }

    @Test
    fun insert() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insert(account)
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun insertSelective() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang")
        val rows = accountMapper.insertSelective(account)
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun javaInsert() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountJavaMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insert(account)
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun insertAccount() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(
            username = "tang",
            password = "123456",
            createTime = Date(System.currentTimeMillis()),
            updateTime = null,
            balance = BigDecimal(100.00)
        )
        val rows = accountMapper.insertAccount(account)
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun javaInsertAccount() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountJavaMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insertAccount(account)
        session.commit()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun multiSqlSession() {
        val session1 = sqlSessionFactory.openSession()
        val session2 = sqlSessionFactory.openSession()
        val accountMapper1 = session1.getMapper(AccountMapper::class.java)
        val accountMapper2 = session2.getMapper(AccountMapper::class.java)
        val account1 = Account(username = "tang1", password = "123456")
        val account2 = Account(username = "tang2", password = "123456")
        val rows1 = accountMapper1.insert(account1)
        val rows2 = accountMapper2.insert(account2)
        session1.commit()
        session2.commit()
        session1.close()
        session2.close()
        assertEquals(1, rows1)
        assertEquals(1, rows2)
    }

    @Test
    fun multiInsert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper1 = session.getMapper(AccountMapper::class.java)
        val account1 = Account(username = "tang1", password = "123456")
        val account2 = Account(username = "tang2", password = "123456")
        val rows1 = accountMapper1.insert(account1)
        val rows2 = accountMapper1.insert(account2)
        session.commit()
        session.close()
        assertEquals(1, rows1)
        assertEquals(1, rows2)
    }

    @Test
    fun batchInsert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = listOf(
            Account(username = "tang1", password = "123456"),
            Account(username = "tang2", password = "123456")
        )
        val rows = accountMapper.batchInsert(accounts.toTypedArray())
        session.commit()
        session.close()
        assertEquals(2, rows)
    }

    @Test
    fun batchInsertSelective() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = listOf(
            Account(username = "tang1"),
            Account(username = "tang2")
        )
        val rows = accountMapper.batchInsertSelective(accounts.toTypedArray())
        session.commit()
        session.close()
        assertEquals(2, rows)
    }

    @Test
    fun update() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(id = 1, username = "tang", password = "654321")
        val rows = accountMapper.update(account)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang", password = "654321")
        val condition = Account(id = 1)
        val rows = accountMapper.update(account, condition)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateSelective() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(id = 1, username = "tang")
        val rows = accountMapper.updateSelective(account)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateWrapper() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val updateWrapper = UpdateWrapper.create<Account>()
            .from(Account::class.java)
            .set(Account::username, "tang")
            .set("password", "123456", false)
            .where()
            .eq(Account::id, 1, true)
            .build()
        val rows = accountMapper.updateWrapper(updateWrapper)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateWrapperPost() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val rows = accountMapper.updateWrapper()
            .from(Account::class.java)
            .set(Account::username, "tang")
            .set("password", "123456", false)
            .where()
            .eq(Account::id, 1)
            .execute()
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun delete() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(id = 1)
        val rows = accountMapper.delete(account)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun deleteById() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val rows = accountMapper.deleteById(1L)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun select() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.select()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectOrderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.select(OrderItem("id", false))
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectById() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = accountMapper.selectById(1)
        session.close()
        assertNotNull(account)
    }

    @Test
    fun selectCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "admin")
        val accounts = accountMapper.select(account)
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectConditionOrderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "admin")
        val accounts = accountMapper.select(account, OrderItem("id", false))
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun queryWrapper() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val queryWrapper = QueryWrapper.create<Account>()
            .select("id", "username")
            .column(Account::password)
            .from(Account::class.java)
            .build()
        val accounts = accountMapper.queryWrapper(queryWrapper)
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun queryWrapperPost() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.queryWrapper()
            .select("id", "username")
            .column(Account::password)
            .from(Account::class.java)
            .execute()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectDistinct() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.queryWrapper()
            .distinct()
            .select("id", "username")
            .column(Account::password)
            .from(Account::class.java)
            .execute()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun count() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val count = accountMapper.count()
        session.close()
        assertNotEquals(0, count)
    }

    @Test
    fun countCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "admin")
        val count = accountMapper.count(account)
        session.close()
        assertNotEquals(0, count)
    }

    @Test
    fun paginate() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(2, 5)
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(2, 5, Account(username = "tang"))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateOderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(2, 5, OrderItem("id", false))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateOderByKMutableProperty1() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(2, 5, OrderItem(Account::updateTime, false))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateOrderBys() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(2, 5, listOf(OrderItem("id", false), OrderItem("username", true)))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateOrderBysCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(2, 5, Account(username = "tang"), arrayOf(OrderItem("id", false)))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun sqlInjection() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "admin' or '1'='1")
        val list = accountMapper.select(account)
        session.close()
        assertEquals(0, list.size)
    }

    @Test
    fun autoFillId() {
        val session = sqlSessionFactory.openSession()
        val roleMapper = session.getMapper(RoleMapper::class.java)
        val role = Role(name = "tang")
        val rows = roleMapper.insert(role)
        session.commit()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun paginateRequest() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(request)
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun test() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val queryWrapper = QueryWrapper.create<Account>()
            .select("id", "username")
            .column(Account::password)
            .from(Account::class.java)
            .build()

//        val sqlStatement = queryWrapper.getSqlStatement()
//        println(sqlStatement.sql)
//        println(sqlStatement.parameters)
//
        val list2 = accountMapper.queryWrapper().select().from(Account::class.java).execute()
        list2.forEach({
        })


        session.close()
    }

}
