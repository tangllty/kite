package com.tang.kite.session.mapper

import com.tang.kite.annotation.Insert
import com.tang.kite.annotation.Param
import com.tang.kite.annotation.Select
import com.tang.kite.mapper.BaseMapper
import com.tang.kite.session.entity.Account

/**
 * @author Tang
 */
interface AccountMapper : BaseMapper<Account> {

    @Select("select * from account")
    fun selectAnnotation(): List<Account>

    @Insert("insert into account (username, password) values (#{username}, #{password})")
    fun insertAnnotation(account: Account): Int

    @Insert("insert into account (username, password) values (#{account.username}, #{param3})")
    fun insertAnnotation(@Param("account") account: Account, other: Account, str: String): Int

}
