package com.tang.kite.session

import com.tang.kite.BaseDataTest
import com.tang.kite.paginate.OrderItem
import com.tang.kite.session.entity.Account
import com.tang.kite.session.entity.AccountAs
import com.tang.kite.session.entity.AccountFunction
import com.tang.kite.session.mapper.AccountAsMapper
import com.tang.kite.session.mapper.AccountFunctionMapper
import com.tang.kite.session.mapper.AccountMapper
import com.tang.kite.session.mapper.AccountOneToManyMapper
import com.tang.kite.session.mapper.AccountOneToManyWithJoinTableMapper
import com.tang.kite.session.mapper.AccountOneToOneMapper
import com.tang.kite.session.mapper.AccountOneToOneWIthJoinTableMapper
import com.tang.kite.sql.function.SqlAlias
import com.tang.kite.sql.function.SqlFunction
import com.tang.kite.sql.function.`as`
import com.tang.kite.wrapper.query.QueryWrapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class SelectTest : BaseDataTest() {

    @Test
    fun select() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = accountMapper.select()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectOrderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = accountMapper.select(OrderItem("id", false))
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectById() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = accountMapper.selectById(1)
        session.close()
        assertNotNull(account)
    }

    @Test
    fun selectConditionNoParam() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account()
        val accounts = accountMapper.select(account)
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(username = "admin")
        val accounts = accountMapper.select(account)
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectConditionOrderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(username = "admin")
        val accounts = accountMapper.select(account, OrderItem("id", false))
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun queryWrapper() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
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
    fun queryWrapperShort() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val queryWrapper = QueryWrapper.create<Account>()
            .eq(Account::username, "admin")
            .build()
        val accounts = accountMapper.queryWrapper(queryWrapper)
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectOneWrapper() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val queryWrapper = QueryWrapper.create<Account>()
            .select("id", "username")
            .column(Account::password)
            .from(Account::class.java)
            .eq(Account::username, "admin")
            .build()
        val account = accountMapper.selectOneWrapper(queryWrapper)
        session.close()
        assertNotNull(account)
    }

    @Test
    fun queryWrapperPost() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = accountMapper.queryWrapper()
            .select("id", "username")
            .column(Account::password)
            .from(Account::class.java)
            .list()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectWrapperShortPost() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = accountMapper.queryWrapper()
            .eq(Account::username, "admin")
            .list()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectOneWrapperPost() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = accountMapper.queryWrapper()
            .select("id", "username")
            .column(Account::password)
            .from(Account::class.java)
            .eq(Account::username, "admin")
            .one()
        session.close()
        assertNotNull(account)
    }

    @Test
    fun queryWrapperNestedCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
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
            .list()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectDistinct() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = accountMapper.queryWrapper()
            .distinct()
            .select("id", "username")
            .column(Account::password)
            .from(Account::class.java)
            .list()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectWrapperGroupBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = accountMapper.queryWrapper()
            .select()
            .columns(Account::username, Account::password)
            .from(Account::class.java)
            .groupBy(Account::username)
            .groupBy(Account::password)
            .list()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectWrapperOrderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = accountMapper.queryWrapper()
            .select()
            .columns(Account::username, Account::password)
            .from(Account::class.java)
            .orderBy(Account::username)
            .orderBy(Account::balance, false)
            .list()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectWrapperGroupByOrderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = accountMapper.queryWrapper()
            .select()
            .columns(Account::username, Account::password)
            .from(Account::class.java)
            .groupBy(Account::username, Account::password)
            .orderBy(Account::username)
            .list()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectWrapperHaving() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = accountMapper.queryWrapper()
            .select()
            .columns(Account::username, Account::password)
            .from(Account::class.java)
            .groupBy(Account::username, Account::password)
            .having {
                isNotNull(Account::password)
            }
            .list()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun queryWrapperAs() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountAsMapper::class)
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
        val accountMapper = session.getMapper(AccountFunctionMapper::class)
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
        val accountMapper = session.getMapper(AccountMapper::class)
        val count = accountMapper.count()
        session.close()
        assertNotEquals(0, count)
    }

    @Test
    fun countCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(username = "admin")
        val count = accountMapper.count(account)
        session.close()
        assertNotEquals(0, count)
    }

    @Test
    fun paginate() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val page = accountMapper.paginate(2, 5)
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val page = accountMapper.paginate(2, 5, Account(username = "tang"))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateOderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val page = accountMapper.paginate(2, 5, OrderItem("id", false))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateOderByKMutableProperty1() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val page = accountMapper.paginate(2, 5, OrderItem(Account::updateTime, false))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateOrderBys() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val page = accountMapper.paginate(2, 5, listOf(OrderItem("id", false), OrderItem("username", true)))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateOrderBysCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val page = accountMapper.paginate(2, 5, Account(username = "tang"), arrayOf(OrderItem("id", false)))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateRequest() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val page = accountMapper.paginate(request)
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun selectOneToOne() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountOneToOneMapper::class)
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
        val accountMapper = session.getMapper(AccountOneToOneWIthJoinTableMapper::class)
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
        val accountMapper = session.getMapper(AccountOneToManyMapper::class)
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
        val accountMapper = session.getMapper(AccountOneToManyWithJoinTableMapper::class)
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
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = accountMapper.selectAnnotation()
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectConditionAnnotation() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(username = "admin")
        val accounts = accountMapper.selectCondition(account)
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun sqlInjection() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(username = "admin' or '1'='1")
        val list = accountMapper.select(account)
        session.close()
        assertEquals(0, list.size)
    }

}
