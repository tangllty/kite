package com.tang.kite.session;

import com.tang.kite.BaseDataTest;
import com.tang.kite.paginate.OrderItem;
import com.tang.kite.session.entity.Account;
import com.tang.kite.session.mapper.AccountMapper;
import com.tang.kite.wrapper.query.QueryWrapper;
import com.tang.kite.wrapper.update.UpdateWrapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Tang
 */
public class SqlSessionJavaTest extends BaseDataTest {

    @Test
    public void paginateOrderByList() {
        var session = Companion.getSqlSessionFactory().openSession();
        var accountMapper = session.getMapper(AccountMapper.class);
        var page = accountMapper.paginate(2, 5, new OrderItem<>("id", false));
        session.close();
        assertNotEquals(0, page.getTotal());
    }

    @Test
    public void paginateOrderBysList() {
        var session = Companion.getSqlSessionFactory().openSession();
        var accountMapper = session.getMapper(AccountMapper.class);
        var page = accountMapper.paginate(2, 5, List.of(new OrderItem<>("id", false), new OrderItem<>("username", true)));
        session.close();
        assertNotEquals(0, page.getTotal());
    }

    @Test
    public void paginateOrderByFunction() {
        var session = Companion.getSqlSessionFactory().openSession();
        var accountMapper = session.getMapper(AccountMapper.class);
        var page = accountMapper.paginate(2, 5, new OrderItem<>(Account::getUpdateTime, false));
        session.close();
        assertNotEquals(0, page.getTotal());
    }

    @Test
    public void updateWrapper() {
        var session = Companion.getSqlSessionFactory().openSession();
        var accountMapper = session.getMapper(AccountMapper.class);
        var updateWrapper = UpdateWrapper.<Account>create()
            .from(Account.class)
            .set(Account::getUsername, "tang")
            .set("password", "123456", false)
            .where()
            .eq(Account::getId, 1, true)
            .build();
        var rows = accountMapper.updateWrapper(updateWrapper);
        session.rollback();
        session.close();
        assertEquals(1, rows);
    }

    @Test
    public void updateWrapperPost() {
        var session = Companion.getSqlSessionFactory().openSession();
        var accountMapper = session.getMapper(AccountMapper.class);
        var rows = accountMapper.updateWrapper()
            .from(Account.class)
            .set(Account::getUsername, "tang")
            .set("password", "123456", false)
            .where()
            .eq(Account::getId, 1)
            .update();
        session.rollback();
        session.close();
        assertEquals(1, rows);
    }

    @Test
    public void queryWrapper() {
        var session = Companion.getSqlSessionFactory().openSession();
        var accountMapper = session.getMapper(AccountMapper.class);
        var queryWrapper = QueryWrapper.<Account>create()
            .select("id", "username")
            .column(Account::getPassword)
            .from(Account.class)
            .build();
        var accounts = accountMapper.selectWrapper(queryWrapper);
        session.close();
        assertFalse(accounts.isEmpty());
    }

    @Test
    public void queryWrapperPost() {
        var session = Companion.getSqlSessionFactory().openSession();
        var accountMapper = session.getMapper(AccountMapper.class);
        var accounts = accountMapper.queryWrapper()
            .select("id", "username")
            .column(Account::getPassword)
            .from(Account.class)
            .list();
        session.close();
        assertFalse(accounts.isEmpty());
    }

    @Test
    public void queryWrapperNestedCondition() {
        var session = Companion.getSqlSessionFactory().openSession();
        var accountMapper = session.getMapper(AccountMapper.class);
        var accounts = accountMapper.queryWrapper()
            .select(Account::getId, Account::getUsername, Account::getBalance)
            .from(Account.class)
            .eq(Account::getId, 1)
            .or()
            .eq(Account::getId, 2)
            .and(it -> {
                it.eq(Account::getUsername, "tang")
                .or(or -> {
                    or.eq(Account::getUsername, "admin")
                    .or()
                    .eq(Account::getBalance, new BigDecimal("1000.00"));
                });
            })
            .or()
            .eq(Account::getUsername, "admin")
            .list();
        session.close();
        assertFalse(accounts.isEmpty());
    }

}
