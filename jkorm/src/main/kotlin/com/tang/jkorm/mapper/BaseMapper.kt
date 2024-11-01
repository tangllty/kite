package com.tang.jkorm.mapper

import com.tang.jkorm.config.JkOrmConfig
import com.tang.jkorm.paginate.OrderItem
import com.tang.jkorm.paginate.Page
import com.tang.jkorm.wrapper.update.UpdateWrapper
import jakarta.servlet.http.HttpServletRequest

/**
 * Base mapper
 *
 * @param T Entity type
 *
 * @author Tang
 */
interface BaseMapper<T> {

    /**
     * Insert entity
     *
     * @param type Entity
     * @return Inserted count
     */
    fun insert(type: T): Int

    /**
     * Insert entity selective, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param type Entity
     * @return Inserted count
     */
    fun insertSelective(type: T): Int

    /**
     * Batch insert entity
     *
     * @param list Entity list
     * @return Inserted count
     */
    fun batchInsert(list: Iterable<T>): Int

    /**
     * Batch insert entity
     *
     * @param list Entity list
     * @return Inserted count
     */
    fun batchInsert(list: Array<T>): Int {
        return batchInsert(list.toList())
    }

    /**
     * Batch insert entity selective, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param list Entity list
     * @return Inserted count
     */
    fun batchInsertSelective(list: Iterable<T>): Int

    /**
     * Batch insert entity selective, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param list Entity list
     * @return Inserted count
     */
    fun batchInsertSelective(list: Array<T>): Int {
        return batchInsertSelective(list.toList())
    }

    /**
     * Update entity by primary key
     *
     * @param type Entity
     * @return Updated count
     */
    fun update(type: T): Int

    /**
     * Update entity by where entity condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * The primary key will also be updated, if set in the value entity
     *
     * @param type Value entity
     * @param condition Condition entity
     * @return Updated count
     */
    fun update(type: T, condition: T): Int

    /**
     * Update entity by update wrapper
     */
    fun update(updateWrapper: UpdateWrapper): Int

    /**
     * Update entity by update wrapper
     */
    fun update(): UpdateWrapper {
        return UpdateWrapper(this)
    }

    /**
     * Update entity selective by primary key, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param type Entity
     * @return Updated count
     */
    fun updateSelective(type: T): Int

    /**
     * Delete entity by condition, all fields are used as condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param type Entity
     * @return Deleted count
     */
    fun delete(type: T): Int

    /**
     * Delete entity by primary key
     *
     * @param id Entity id
     * @return Deleted count
     */
    fun deleteById(id: Long): Int

    /**
     * Select all
     *
     * @return Entity list
     */
    fun select(): List<T>

    /**
     * Select all by order by
     *
     * @param orderBy Order by
     * @return Entity list
     */
    fun select(orderBy: OrderItem<T>): List<T> {
        return select(arrayOf(orderBy))
    }

    /**
     * Select all by order by
     *
     * @param orderBys Order by
     * @return Entity list
     */
    fun select(orderBys: Array<OrderItem<T>>): List<T>

    /**
     * Select all by order by
     *
     * @param orderBys Order by list
     * @return Entity list
     */
    fun select(orderBys: List<OrderItem<T>>): List<T> {
        return select(orderBys.toTypedArray())
    }

    /**
     * Select by condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param type Entity
     * @return Entity list
     */
    fun select(type: T): List<T>

    /**
     * Select by condition and order by
     *
     * @param type Entity
     * @param orderBy Order by
     * @return Entity list
     */
    fun select(type: T, orderBy: OrderItem<T>): List<T> {
        return select(type, arrayOf(orderBy))
    }

    /**
     * Select by condition and order by
     *
     * @param type Entity
     * @param orderBys Order by array
     * @return Entity list
     */
    fun select(type: T, orderBys: Array<OrderItem<T>>): List<T>

    /**
     * Select by condition and order by
     *
     * @param type Entity
     * @param orderBys Order by list
     * @return Entity list
     */
    fun select(type: T, orderBys: List<OrderItem<T>>): List<T> {
        return select(type, orderBys.toTypedArray())
    }

    /**
     * Select by primary key
     *
     * @param id Entity id
     * @return Entity
     */
    fun selectById(id: Long): T?

    /**
     * Count all
     *
     * @return Count
     */
    fun count(): Long

    /**
     * Count by condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param type Entity
     * @return Count
     */
    fun count(type: T): Long

    /**
     * Paginate by page number and page size
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long): Page<T>

    /**
     * Paginate by page number, page size and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param type Entity
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, type: T): Page<T>

    /**
     * Paginate by page number, page size and order by
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBy Order by
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBy: OrderItem<T>): Page<T> {
        return paginate(pageNumber, pageSize, arrayOf(orderBy))
    }

    /**
     * Paginate by page number, page size and order by
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBys Order by array
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBys: Array<OrderItem<T>>): Page<T>

    /**
     * Paginate by page number, page size and order by
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBys Order by list
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(pageNumber, pageSize, orderBys.toTypedArray())
    }

    /**
     * Paginate by page number, page size, order by and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param type Entity
     * @param orderBy Order by
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, type: T, orderBy: OrderItem<T>): Page<T> {
        return paginate(pageNumber, pageSize, type, arrayOf(orderBy))
    }

    /**
     * Paginate by page number, page size, order by and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param type Entity
     * @param orderBys Order by array
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, type: T, orderBys: Array<OrderItem<T>>): Page<T>

    /**
     * Paginate by page number, page size, order by and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param type Entity
     * @param orderBys Order by list
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, type: T, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(pageNumber, pageSize, type, orderBys.toTypedArray())
    }

    /**
     * Paginate by request
     *
     * @param request HttpServletRequest
     * @return Page
     */
    fun paginate(request: HttpServletRequest): Page<T> {
        return paginate(getPageNumber(request), getPageSize(request))
    }

    /**
     * Paginate by request and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param request HttpServletRequest
     * @param type Entity
     * @return Page
     */
    fun paginate(request: HttpServletRequest, type: T): Page<T> {
        return paginate(getPageNumber(request), getPageSize(request), type)
    }

    /**
     * Paginate by request and order by
     *
     * @param request HttpServletRequest
     * @param orderBy Order by
     * @return Page
     */
    fun paginate(request: HttpServletRequest, orderBy: OrderItem<T>): Page<T> {
        return paginate(request, arrayOf(orderBy))
    }

    /**
     * Paginate by request and order by
     *
     * @param request HttpServletRequest
     * @param orderBys Order by array
     * @return Page
     */
    fun paginate(request: HttpServletRequest, orderBys: Array<OrderItem<T>>): Page<T> {
        return paginate(getPageNumber(request), getPageSize(request), orderBys)
    }

    /**
     * Paginate by request and order by
     *
     * @param request HttpServletRequest
     * @param orderBys Order by list
     * @return Page
     */
    fun paginate(request: HttpServletRequest, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(request, orderBys.toTypedArray())
    }

    /**
     * Paginate by request, order by and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param request HttpServletRequest
     * @param type Entity
     * @param orderBy Order by
     * @return Page
     */
    fun paginate(request: HttpServletRequest, type: T, orderBy: OrderItem<T>): Page<T> {
        return paginate(request, type, arrayOf(orderBy))
    }

    /**
     * Paginate by request, order by and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param request HttpServletRequest
     * @param type Entity
     * @param orderBys Order by array
     * @return Page
     */
    fun paginate(request: HttpServletRequest, type: T, orderBys: Array<OrderItem<T>>): Page<T> {
        return paginate(getPageNumber(request), getPageSize(request), type, orderBys)
    }

    /**
     * Paginate by request, order by and condition, ignore [JkOrmConfig.selectiveStrategy] value
     *
     * @param request HttpServletRequest
     * @param type Entity
     * @param orderBys Order by list
     * @return Page
     */
    fun paginate(request: HttpServletRequest, type: T, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(request, type, orderBys.toTypedArray())
    }

    /**
     * Get page number from request
     *
     * @param request HttpServletRequest
     * @return Page number
     */
    fun getPageNumber(request: HttpServletRequest): Long {
        return request.getParameter(JkOrmConfig.INSTANCE.pageNumberParameter)?.toLong() ?: JkOrmConfig.INSTANCE.pageNumber
    }

    /**
     * Get page size from request
     *
     * @param request HttpServletRequest
     * @return Page size
     */
    fun getPageSize(request: HttpServletRequest): Long {
        return request.getParameter(JkOrmConfig.INSTANCE.pageSizeParameter)?.toLong() ?: JkOrmConfig.INSTANCE.pageSize
    }

}
