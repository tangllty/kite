package com.tang.kite.session.mapper

import com.tang.kite.mapper.BaseMapper
import com.tang.kite.session.entity.Account

/**
 * @author Tang
 */
interface AccountMapper : BaseMapper<Account> {

    fun insertAccount(account: Account): Int {
        return insert(account)
    }

}
