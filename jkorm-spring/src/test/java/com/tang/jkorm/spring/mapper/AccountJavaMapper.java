package com.tang.jkorm.spring.mapper;

import com.tang.jkorm.spring.entity.Account;

/**
 * @author Tang
 */
public interface AccountJavaMapper extends BaseMapper<Account> {

    default int insertAccount(Account account) {
        return insert(account);
    }

}
