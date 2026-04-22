package com.tang.kite.session.entity

/**
 * @author Tang
 */
class AccountRole(
    var accountId: Long? = null,
    var roleId: Long? = null
) {
    override fun toString(): String {
        return "'AccountRole => accountId=$accountId roleId=$roleId'"
    }
}
