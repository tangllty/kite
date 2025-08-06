package com.tang.kite.sql

import com.tang.kite.paginate.OrderItem
import com.tang.kite.session.entity.Account
import com.tang.kite.sql.provider.ProviderType
import com.tang.kite.sql.provider.mysql.MysqlSqlProvider
import org.junit.jupiter.api.Test

/**
 * @author Tang
 */
class MysqlSqlProvidersTest : SqlProviderTest {

    private val columnsWithoutId = "username, password, create_time, update_time, balance"

    private val columns = "id, $columnsWithoutId"

    private val sqlProvider = MysqlSqlProvider()

    @Test
    override fun providerType() {
        equals(ProviderType.MYSQL, sqlProvider.providerType())
    }

    @Test
    override fun insert() {
        val account = Account(username = "tang", password = "123456")
        val statement = sqlProvider.insert(account)
        equals("insert into account ($columnsWithoutId) values (?, ?, ?, ?, ?)", statement.sql)
        equals("insert into account ($columnsWithoutId) values ('tang', '123456', NULL, NULL, NULL)", statement.getActualSql())
    }

    @Test
    override fun insertSelective() {
        val account = Account(username = "tang", password = "123456")
        val statement = sqlProvider.insertSelective(account)
        equals("insert into account (username, password) values (?, ?)", statement.sql)
        equals("insert into account (username, password) values ('tang', '123456')", statement.getActualSql())
    }

    @Test
    override fun batchInsert() {
        val accounts = listOf(
            Account(username = "tang1", password = "123456"),
            Account(username = "tang2", password = "123456")
        )
        val statement = sqlProvider.batchInsert(accounts)
        equals("insert into account ($columnsWithoutId) values (?, ?, ?, ?, ?)", statement.sql)
        equals("[[tang1, 123456, null, null, null], [tang2, 123456, null, null, null]]", statement.parameters.toString())
    }

    @Test
    override fun batchInsertSelective() {
        val accounts = listOf(
            Account(username = "tang1", password = "123456"),
            Account(username = "tang2", password = "123456")
        )
        val statement = sqlProvider.batchInsertSelective(accounts)
        equals("insert into account (username, password) values (?, ?)", statement.first().sql)
        equals("[[tang1, 123456], [tang2, 123456]]", statement.map { it.parameters }.toString())
    }

    @Test
    override fun update() {
        val account = Account(id = 1, username = "tang", password = "123456")
        val statement = sqlProvider.update(account)
        equals("update account set username = ?, password = ?, create_time = ?, update_time = ?, balance = ? where id = ?", statement.sql)
        equals("update account set username = 'tang', password = '123456', create_time = NULL, update_time = NULL, balance = NULL where id = 1", statement.getActualSql())
    }

    @Test
    override fun updateCondition() {
        val account = Account(username = "tang", password = "123456")
        val condition = Account(id = 1)
        val statement = sqlProvider.update(account, condition)
        equals("update account set username = ?, password = ? where id = ?", statement.sql)
        equals("update account set username = 'tang', password = '123456' where id = 1", statement.getActualSql())
    }

    @Test
    override fun updateSelective() {
        val account = Account(id = 1, password = "123456")
        val statement = sqlProvider.updateSelective(account)
        equals("update account set password = ? where id = ?", statement.sql)
        equals("update account set password = '123456' where id = 1", statement.getActualSql())
    }

    @Test
    override fun delete() {
        val account = Account(id = 1, username = "tang")
        val statement = sqlProvider.delete(Account::class.java, account)
        equals("delete from account where id = ? and username = ?", statement.sql)
        equals("delete from account where id = 1 and username = 'tang'", statement.getActualSql())
    }

    @Test
    override fun deleteById() {
        val account = Account(id = 1)
        val statement = sqlProvider.delete(Account::class.java, account)
        equals("delete from account where id = ?", statement.sql)
        equals("delete from account where id = 1", statement.getActualSql())
    }

    @Test
    override fun select() {
        val statement = sqlProvider.select(Account::class.java, null, emptyArray())
        equals("select $columns from account", statement.sql)
    }

    @Test
    override fun selectCondition() {
        val account = Account(username = "tang")
        val statement = sqlProvider.select(Account::class.java, account, emptyArray())
        equals("select $columns from account where username = ?", statement.sql)
        equals("select $columns from account where username = 'tang'", statement.getActualSql())
    }

    @Test
    override fun selectOrderBy() {
        val statement = sqlProvider.select(Account::class.java, null, arrayOf(OrderItem("id", false)))
        equals("select $columns from account order by id desc", statement.sql)
        equals("select $columns from account order by id desc", statement.getActualSql())
    }

    @Test
    override fun count() {
        val statement = sqlProvider.count(Account::class.java, null)
        equals("select count(*) from account", statement.sql)
    }

    @Test
    override fun countCondition() {
        val account = Account(username = "tang")
        val statement = sqlProvider.count(Account::class.java, account)
        equals("select count(*) from account where username = ?", statement.sql)
        equals("select count(*) from account where username = 'tang'", statement.getActualSql())
    }

    @Test
    override fun paginate() {
        val statement = sqlProvider.paginate(Account::class.java, null, emptyArray(), 1, 5)
        equals("select $columns from account limit ?, ?", statement.sql)
        equals("select $columns from account limit 0, 5", statement.getActualSql())
    }

    @Test
    override fun paginateCondition() {
        val account = Account(username = "tang")
        val statement = sqlProvider.paginate(Account::class.java, account, emptyArray(), 1, 5)
        equals("select $columns from account where username = ? limit ?, ?", statement.sql)
        equals("select $columns from account where username = 'tang' limit 0, 5", statement.getActualSql())
    }

    @Test
    override fun paginateOrderBy() {
        val statement = sqlProvider.paginate(Account::class.java, null, arrayOf(OrderItem("id", false)), 1, 5)
        equals("select $columns from account order by id desc limit ?, ?", statement.sql)
        equals("select $columns from account order by id desc limit 0, 5", statement.getActualSql())
    }

}
