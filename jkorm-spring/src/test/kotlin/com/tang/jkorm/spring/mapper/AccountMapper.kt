package com.tang.jkorm.spring.mapper

import com.tang.jkorm.spring.entity.Account

/**
 * @author Tang
 */
interface AccountMapper : BaseMapper<Account> {

    fun insertAccount(account: Account): Int {
        return insert(account)
    }

}
