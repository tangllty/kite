package com.tang.jkorm.session.mapper

import com.tang.jkorm.mapper.BaseMapper
import com.tang.jkorm.session.entity.Account

/**
 * @author Tang
 */
interface AccountMapper : BaseMapper<Account> {

    fun insertAccount(account: Account): Int {
        return insert(account)
    }

}
