package com.tang.kite.spring.service.impl

import com.tang.kite.mapper.BaseMapper
import com.tang.kite.service.BaseService
import org.springframework.beans.factory.annotation.Autowired

/**
 * Base service interface implementation
 *
 * @author Tang
 */
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
open class ServiceImpl<M : BaseMapper<T>, T : Any> : BaseService<T> {

    @Autowired
    lateinit var baseMapper: M

    override fun getMapper(): BaseMapper<T> {
        return baseMapper
    }

}
