package com.tang.jkorm.session.mapper;

import com.tang.jkorm.mapper.BaseMapper;
import com.tang.jkorm.session.entity.Account;

/**
 * @author Tang
 */
public interface AccountJavaMapper extends BaseMapper<Account> {

    default int insertAccount(Account account) {
        return insert(account);
    }

}
