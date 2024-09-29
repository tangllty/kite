package com.tang.jkorm.session;

import com.tang.jkorm.BaseDataTest;
import com.tang.jkorm.session.mapper.AccountMapper;
import com.tang.jkorm.utils.Pairs;
import kotlin.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Tang
 */
public class SqlSessionJavaTest extends BaseDataTest {

    @Test
    public void paginateOrderByList() {
        var session = Companion.getSqlSessionFactory().openSession();
        var accountMapper = session.getMapper(AccountMapper.class);
        var page = accountMapper.paginate(2, 5, Pairs.of("id", false));
        session.close();
        assertNotEquals(0, page.getTotal());
    }

    @Test
    public void paginateOrderBysList() {
        var session = Companion.getSqlSessionFactory().openSession();
        var accountMapper = session.getMapper(AccountMapper.class);
        var page = accountMapper.paginate(2, 5, List.of(new Pair<>("id", false), new Pair<>("username", true)));
        session.close();
        assertNotEquals(0, page.getTotal());
    }

}
