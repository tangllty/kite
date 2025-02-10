package com.tang.kite.spring.mapper

import com.tang.kite.mapper.BaseMapper
import com.tang.kite.spring.entity.Account

/**
 * @author Tang
 */
interface AccountMapper : BaseMapper<Account> {

    fun insertAccount(account: Account): Int {
        return insert(account)
    }

}
