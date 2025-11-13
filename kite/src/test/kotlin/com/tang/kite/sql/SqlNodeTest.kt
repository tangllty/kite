package com.tang.kite.sql

import com.tang.kite.session.entity.Account
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.enumeration.ComparisonOperator
import com.tang.kite.sql.enumeration.LogicalOperator
import com.tang.kite.sql.provider.ProviderType
import com.tang.kite.sql.statement.ComparisonStatement
import com.tang.kite.sql.statement.LogicalStatement
import kotlin.test.Test


/**
 * @author Tang
 */
class SqlNodeTest {

    @Test
    fun select() {
        val select = SqlNode.Select()
        select.columns.add(Column(Account::id))
        select.columns.add(Column(Account::username))
        select.from = TableReference(Account::class)
        val condition = ComparisonStatement(Column(Account::id), 1, ComparisonOperator.EQUAL)
        select.where.add(LogicalStatement(condition, LogicalOperator.AND))

        println(select.getSqlStatement(object : SqlDialect {
            override fun getType(): ProviderType {
                return ProviderType.POSTGRESQL
            }

            override fun applyLimitClause(sql: StringBuilder, parameters: MutableList<Any?>, limit: Long, offset: Long) {
            }
        }))
    }

}
