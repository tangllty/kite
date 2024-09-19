package com.tang.jkorm.spring.mapper

import com.tang.jkorm.paginate.Page
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * @author Tang
 */
interface BaseMapper<T> : com.tang.jkorm.mapper.BaseMapper<T> {

    fun paginate(): Page<T> {
        return paginate(getRequest())
    }

    fun paginate(type: T): Page<T> {
        return paginate(getRequest(), type)
    }

    fun paginate(orderBys: Array<Pair<String, Boolean>>): Page<T> {
        return paginate(getRequest(), orderBys)
    }

    fun paginate(orderBys: List<Pair<String, Boolean>>): Page<T> {
        return paginate(getRequest(), orderBys.toTypedArray())
    }

    fun paginate(orderBys: Array<Pair<String, Boolean>>, type: T): Page<T> {
        return paginate(getRequest(), orderBys, type)
    }

    fun paginate(orderBys: List<Pair<String, Boolean>>, type: T): Page<T> {
        return paginate(getRequest(), orderBys.toTypedArray(), type)
    }

    private fun getRequest(): HttpServletRequest {
        val attributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        return attributes.request
    }

}
