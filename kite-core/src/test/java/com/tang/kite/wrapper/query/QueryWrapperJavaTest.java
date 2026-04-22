package com.tang.kite.wrapper.query;

import com.tang.kite.session.entity.Account;
import com.tang.kite.sql.dialect.DerbyDialect;
import com.tang.kite.sql.dialect.SqlDialect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tang
 */
public class QueryWrapperJavaTest extends QueryWrapperConstants implements QueryWrapperTest {

    private final SqlDialect sqlDialect = new DerbyDialect();

    @Test
    @Override
    public void selectAll() {
        var sqlStatement = QueryWrapper.<Account>create()
            .select()
            .from(Account.class)
            .build()
            .getSqlStatement(sqlDialect);
        assertEquals(getSelectFromAccount(), sqlStatement.getActualSql());
    }

    @Test
    @Override
    public void selectSpecificColumns() {
        var sqlStatement = QueryWrapper.<Account>create()
            .select(Account::getId, Account::getUsername, Account::getCreateTime)
            .from(Account.class)
            .build()
            .getSqlStatement(sqlDialect);
        assertEquals("select id, username, create_time from account", sqlStatement.getActualSql());
    }

    @Test
    @Override
    public void selectWhere() {
        var sqlStatement = QueryWrapper.<Account>create()
            .select()
            .from(Account.class)
            .where()
            .eq(Account::getId, 1)
            .build()
            .getSqlStatement(sqlDialect);
        assertEquals(getSelectFromAccount() + " where id = ?", sqlStatement.getSql());
        assertEquals(getSelectFromAccount() + " where id = 1", sqlStatement.getActualSql());
    }

    @Test
    @Override
    public void selectGroupBy() {
        var sqlStatement = QueryWrapper.<Account>create()
            .select()
            .from(Account.class)
            .groupBy(Account::getUsername)
            .build()
            .getSqlStatement(sqlDialect);
        assertEquals(getSelectFromAccount() + " group by username", sqlStatement.getActualSql());
    }

    @Test
    @Override
    public void selectHaving() {
        var sqlStatement = QueryWrapper.<Account>create()
            .select()
            .from(Account.class)
            .groupBy(Account::getUsername)
            .having()
            .eq(Account::getUsername, "Tang")
            .build()
            .getSqlStatement(sqlDialect);
        assertEquals(getSelectFromAccount() + " group by username having username = ?", sqlStatement.getSql());
        assertEquals(getSelectFromAccount() + " group by username having username = 'Tang'", sqlStatement.getActualSql());
    }

    @Test
    @Override
    public void selectOrderBy() {
        var sqlStatement = QueryWrapper.<Account>create()
            .select()
            .from(Account.class)
            .orderBy(Account::getId)
            .build()
            .getSqlStatement(sqlDialect);
        assertEquals(getSelectFromAccount() + " order by id asc", sqlStatement.getActualSql());
    }

}
