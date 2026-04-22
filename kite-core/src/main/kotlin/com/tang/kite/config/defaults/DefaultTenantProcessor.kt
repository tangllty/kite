package com.tang.kite.config.defaults

import com.tang.kite.config.tenant.TenantProcessor
import java.lang.reflect.Field

/**
 * Default tenant processor implementation
 *
 * @author Tang
 */
object DefaultTenantProcessor : TenantProcessor {

    override fun getTenantIds(field: Field): List<Any> {
        throw NotImplementedError("Please implement this method according to your business logic")
    }

}
