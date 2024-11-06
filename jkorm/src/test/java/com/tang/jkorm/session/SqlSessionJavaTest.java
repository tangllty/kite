package com.tang.jkorm.session;

import com.tang.jkorm.BaseDataTest;
import com.tang.jkorm.paginate.OrderItem;
import com.tang.jkorm.session.entity.Account;
import com.tang.jkorm.session.mapper.AccountMapper;
import com.tang.jkorm.wrapper.update.UpdateWrapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
            .execute();
        session.rollback();
        session.close();
        assertEquals(1, rows);
    }

}
