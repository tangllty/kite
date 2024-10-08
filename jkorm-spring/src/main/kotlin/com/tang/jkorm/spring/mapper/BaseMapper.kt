package com.tang.jkorm.spring.mapper

import com.tang.jkorm.config.JkOrmConfig
import com.tang.jkorm.paginate.OrderItem
import com.tang.jkorm.paginate.Page
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * Base mapper enhanced by spring
 *
 * @author Tang
 */
interface BaseMapper<T> : com.tang.jkorm.mapper.BaseMapper<T> {

    /**
     * Paginate
     *
     * @return Page
     */
    fun paginate(): Page<T> {
        return paginate(getRequest())
    }

    /**
     * Paginate by condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param type Entity type
     * @return Page
     */
    fun paginate(type: T): Page<T> {
        return paginate(getRequest(), type)
    }

    /**
     * Paginate by order by
     *
     * @param orderBy Order by
     * @return Page
     */
    fun paginate(orderBy: OrderItem<T>): Page<T> {
        return paginate(getRequest(), arrayOf(orderBy))
    }

    /**
     * Paginate by order by
     *
     * @param orderBys Order by array
     * @return Page
     */
    fun paginate(orderBys: Array<OrderItem<T>>): Page<T> {
        return paginate(getRequest(), orderBys)
    }

    /**
     * Paginate by order by
     *
     * @param orderBys Order by list
     * @return Page
     */
    fun paginate(orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(getRequest(), orderBys.toTypedArray())
    }

    /**
     * Paginate by order by and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param type Entity type
     * @param orderBy Order by
     * @return Page
     */
    fun paginate(type: T, orderBy: OrderItem<T>): Page<T> {
        return paginate(getRequest(), type, arrayOf(orderBy))
    }

    /**
     * Paginate by order by and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param type Entity type
     * @param orderBys Order by array
     * @return Page
     */
    fun paginate(type: T, orderBys: Array<OrderItem<T>>): Page<T> {
        return paginate(getRequest(), type, orderBys)
    }

    /**
     * Paginate by order by and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param type Entity type
     * @param orderBys Order by list
     * @return Page
     */
    fun paginate(type: T, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(getRequest(), type, orderBys.toTypedArray())
    }

    /**
     * Get request from request context holder
     *
     * @return Request
     */
    private fun getRequest(): HttpServletRequest {
        val attributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        return attributes.request
    }

}
