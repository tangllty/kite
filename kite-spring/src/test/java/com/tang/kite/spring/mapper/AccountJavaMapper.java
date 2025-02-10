package com.tang.kite.spring.mapper;

import com.tang.kite.mapper.BaseMapper;
import com.tang.kite.spring.entity.Account;

/**
 * @author Tang
 */
public interface AccountJavaMapper extends BaseMapper<Account> {

    default int insertAccount(Account account) {
        return insert(account);
    }

}
