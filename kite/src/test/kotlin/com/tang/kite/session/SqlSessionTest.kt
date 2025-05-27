package com.tang.kite.session

import com.tang.kite.BaseDataTest
import com.tang.kite.paginate.OrderItem
import com.tang.kite.session.entity.Account
import com.tang.kite.session.entity.AccountAs
import com.tang.kite.session.entity.AccountFunction
import com.tang.kite.session.entity.Role
import com.tang.kite.session.mapper.AccountAsMapper
import com.tang.kite.session.mapper.AccountFunctionMapper
import com.tang.kite.session.mapper.AccountJavaMapper
import com.tang.kite.session.mapper.AccountMapper
import com.tang.kite.session.mapper.AccountOneToManyMapper
import com.tang.kite.session.mapper.AccountOneToManyWithJoinTableMapper
import com.tang.kite.session.mapper.AccountOneToOneMapper
import com.tang.kite.session.mapper.AccountOneToOneWIthJoinTableMapper
import com.tang.kite.session.mapper.RoleMapper
import com.tang.kite.sql.function.SqlAlias
import com.tang.kite.sql.function.SqlFunction
import com.tang.kite.sql.function.`as`
import com.tang.kite.wrapper.query.QueryWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
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
        val rows = accountMapper.insert(account)
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
    fun selectConditionNoParam() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account()
        val accounts = accountMapper.select(account)
        session.close()
        assertTrue(accounts.isNotEmpty())
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
    fun queryWrapperNestedCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.queryWrapper()
            .select(Account::id, Account::username, Account::balance)
            .from(Account::class.java)
            .eq(Account::id, 1)
            .or()
            .eq(Account::id, 2)
            .and {
                eq(Account::username, "tang")
                or {
                    eq(Account::username, "admin")
                    or()
                    eq(Account::balance, BigDecimal(1000.00))
                }
            }
            .or()
            .eq(Account::id, 3)
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
    fun selectWrapperGroupBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.queryWrapper()
            .select()
            .columns(Account::username, Account::password)
            .from(Account::class.java)
            .groupBy(Account::username)
            .groupBy(Account::password)
            .execute()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectWrapperOrderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.queryWrapper()
            .select()
            .columns(Account::username, Account::password)
            .from(Account::class.java)
            .orderBy(Account::username)
            .orderBy(Account::balance, false)
            .execute()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectWrapperGroupByOrderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.queryWrapper()
            .select()
            .columns(Account::username, Account::password)
            .from(Account::class.java)
            .groupBy(Account::username, Account::password)
            .orderBy(Account::username)
            .execute()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectWrapperHaving() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.queryWrapper()
            .select()
            .columns(Account::username, Account::password)
            .from(Account::class.java)
            .groupBy(Account::username, Account::password)
            .having {
                isNotNull(Account::password)
            }
            .execute()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun queryWrapperAs() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountAsMapper::class.java)
        val queryWrapper = QueryWrapper.create<AccountAs>()
            .select(AccountAs::id, AccountAs::username, AccountAs::password)
            .column(SqlAlias(AccountAs::username).`as`(AccountAs::usernameAs))
            .column(AccountAs::password `as` AccountAs::passwordAs)
            .from(AccountAs::class.java)
            .build()
        val accounts = accountMapper.queryWrapper(queryWrapper)
        session.close()
        assertTrue(accounts.isNotEmpty())
        accounts.forEach {
            assertEquals(it.username, it.usernameAs)
            assertEquals(it.password, it.passwordAs)
        }
    }

    @Test
    fun queryWrapperFunction() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountFunctionMapper::class.java)
        val queryWrapper = QueryWrapper.create<AccountFunction>()
            .select(AccountFunction::id, AccountFunction::username)
            .from(AccountFunction::class.java)
            .where()
            .like(SqlFunction.lower(AccountFunction::username), SqlFunction.lower("Tang"))
            .build()
        val accounts = accountMapper.queryWrapper(queryWrapper)
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
    fun selectOneToOne() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountOneToOneMapper::class.java)
        val account = accountMapper.selectByIdWithJoins(1)
        session.close()
        assertNotNull(account)
        assertNotNull(account!!.id)
        assertNotNull(account.username)
        assertNotNull(account.role)
        assertNotNull(account.role!!.id)
        assertNotNull(account.role!!.name)
    }

    @Test
    fun selectOneToOneWithJoinTable() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountOneToOneWIthJoinTableMapper::class.java)
        val account = accountMapper.selectByIdWithJoins(1)
        session.close()
        assertNotNull(account)
        assertNotNull(account!!.id)
        assertNotNull(account.username)
        assertNotNull(account.role)
        assertNotNull(account.role!!.id)
        assertNotNull(account.role!!.name)
    }

    @Test
    fun selectOneToMany() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountOneToManyMapper::class.java)
        val account = accountMapper.selectByIdWithJoins(2)
        session.close()
        assertNotNull(account)
        assertNotNull(account!!.id)
        assertNotNull(account.username)
        assertNotNull(account.roles)
        account.roles!!.forEach {
            assertNotNull(it.id)
            assertNotNull(it.name)
        }
    }

    @Test
    fun selectOneToManyWithJoinTable() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountOneToManyWithJoinTableMapper::class.java)
        val account = accountMapper.selectByIdWithJoins(2)
        session.close()
        assertNotNull(account)
        assertNotNull(account!!.id)
        assertNotNull(account.username)
        assertNotNull(account.roles)
        account.roles!!.forEach {
            assertNotNull(it.id)
            assertNotNull(it.name)
        }
    }

    @Test
    fun selectAnnotation() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.selectAnnotation()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun insertAnnotation() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insertAnnotation(account)
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun insertAnnotationParam() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insertAnnotation(account, Account(), "test")
        session.close()
        assertEquals(1, rows)
    }

}
