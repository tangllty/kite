package com.tang.kite.sql

import com.tang.kite.session.entity.Account
import com.tang.kite.session.entity.Role
import com.tang.kite.sql.dialect.DerbyDialect
import com.tang.kite.sql.enumeration.ComparisonOperator
import com.tang.kite.sql.enumeration.JoinType
import com.tang.kite.sql.enumeration.LogicalOperator
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
        select.columns.add(Column(Account::password))
        select.columns.add(Column(Role::id))
        select.columns.add(Column(Role::name))
        select.from = TableReference(Account::class)
        val joinCondition = LogicalStatement(
            ComparisonStatement(Column(Account::id), Column(Role::id), ComparisonOperator.EQUAL),
            LogicalOperator.AND
        )
        val join = JoinTable(Role::class, JoinType.LEFT)
        join.conditions.add(joinCondition)
        select.joins.add(join)
        val condition = ComparisonStatement(Column(Account::id), 1, ComparisonOperator.EQUAL)
        select.where.add(LogicalStatement(condition, LogicalOperator.AND))

        println(select.getSqlStatement(DerbyDialect()))
    }

}
