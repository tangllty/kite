package com.tang.kite.mapper

import com.tang.kite.config.KiteConfig
import com.tang.kite.config.PageConfig
import com.tang.kite.paginate.OrderItem
import com.tang.kite.paginate.Page
import com.tang.kite.wrapper.query.QueryWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
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
     * Insert entity selective, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param type Entity
     * @return Inserted count
     */
    fun insertSelective(type: T): Int

    /**
     * Batch insert entity
     *
     * @param list Entity list
     * @param batchSize Batch size
     * @return Inserted count
     */
    fun batchInsert(list: Iterable<T>, batchSize: Int): Int

    /**
     * Batch insert entity
     *
     * @param list Entity list
     * @param batchSize Batch size
     * @return Inserted count
     */
    fun batchInsert(list: Array<T>, batchSize: Int): Int {
        return batchInsert(list.toList(), batchSize)
    }

    /**
     * Batch insert entity
     *
     * @param list Entity list
     * @return Inserted count
     */
    fun batchInsert(list: Iterable<T>): Int {
        return batchInsert(list, KiteConfig.batchSize)
    }

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
     * Batch insert entity selective, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param list Entity list
     * @param batchSize Batch size
     * @return Inserted count
     */
    fun batchInsertSelective(list: Iterable<T>, batchSize: Int): Int

    /**
     * Batch insert entity selective, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param list Entity list
     * @param batchSize Batch size
     * @return Inserted count
     */
    fun batchInsertSelective(list: Array<T>, batchSize: Int): Int {
        return batchInsertSelective(list.toList())
    }

    /**
     * Batch insert entity selective, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param list Entity list
     * @return Inserted count
     */
    fun batchInsertSelective(list: Iterable<T>): Int {
        return batchInsertSelective(list, KiteConfig.batchSize)
    }

    /**
     * Batch insert entity selective, ignore [KiteConfig.selectiveStrategy] value
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
     * Update entity by where entity condition, ignore [KiteConfig.selectiveStrategy] value
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
    fun updateWrapper(updateWrapper: UpdateWrapper<T>): Int

    /**
     * Update entity by update wrapper
     */
    fun updateWrapper(): UpdateWrapper<T> {
        return UpdateWrapper(this)
    }

    /**
     * Update entity selective by primary key, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param type Entity
     * @return Updated count
     */
    fun updateSelective(type: T): Int

    /**
     * Delete entity by condition, all fields are used as condition, ignore [KiteConfig.selectiveStrategy] value
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
     * Select by condition, ignore [KiteConfig.selectiveStrategy] value
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
     * Select by query wrapper
     *
     * @param queryWrapper QueryWrapper
     * @return Entity list
     */
    fun selectWrapper(queryWrapper: QueryWrapper<T>): List<T>

    /**
     * Select by primary key
     *
     * @param id Entity id
     * @return Entity
     */
    fun selectById(id: Long): T?

    /**
     * Select one by query wrapper
     *
     * @param queryWrapper QueryWrapper
     * @return Entity
     */
    fun selectOneWrapper(queryWrapper: QueryWrapper<T>): T?

    /**
     * Select all with joins
     *
     * @return Entity list
     */
    fun selectWithJoins(): List<T>

    /**
     * Select all by order by with joins
     *
     * @param orderBy Order by
     * @return Entity list
     */
    fun selectWithJoins(orderBy: OrderItem<T>): List<T> {
        return select(arrayOf(orderBy))
    }

    /**
     * Select all by order by with joins
     *
     * @param orderBys Order by
     * @return Entity list
     */
    fun selectWithJoins(orderBys: Array<OrderItem<T>>): List<T>

    /**
     * Select all by order by with joins
     *
     * @param orderBys Order by list
     * @return Entity list
     */
    fun selectWithJoins(orderBys: List<OrderItem<T>>): List<T> {
        return select(orderBys.toTypedArray())
    }

    /**
     * Select by condition with joins, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param type Entity
     * @return Entity list
     */
    fun selectWithJoins(type: T): List<T>

    /**
     * Select by condition and order by with joins
     *
     * @param type Entity
     * @param orderBy Order by
     * @return Entity list
     */
    fun selectWithJoins(type: T, orderBy: OrderItem<T>): List<T> {
        return select(type, arrayOf(orderBy))
    }

    /**
     * Select by condition and order by with joins
     *
     * @param type Entity
     * @param orderBys Order by array
     * @return Entity list
     */
    fun selectWithJoins(type: T, orderBys: Array<OrderItem<T>>): List<T>

    /**
     * Select by condition and order by with joins
     *
     * @param type Entity
     * @param orderBys Order by list
     * @return Entity list
     */
    fun selectWithJoins(type: T, orderBys: List<OrderItem<T>>): List<T> {
        return select(type, orderBys.toTypedArray())
    }

    /**
     * Select by primary key with joins
     *
     * @param id Entity id
     * @return Entity
     */
    fun selectByIdWithJoins(id: Long): T?

    /**
     * Select by query wrapper
     *
     * @return QueryWrapper
     */
    fun queryWrapper(): QueryWrapper<T> {
        return QueryWrapper(this)
    }

    /**
     * Count all
     *
     * @return Count
     */
    fun count(): Long

    /**
     * Count by condition, ignore [KiteConfig.selectiveStrategy] value
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
     * Paginate by page number, page size and condition, ignore [KiteConfig.selectiveStrategy] value
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
     * Paginate by page number, page size, order by and condition, ignore [KiteConfig.selectiveStrategy] value
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
     * Paginate by page number, page size, order by and condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param type Entity
     * @param orderBys Order by array
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, type: T, orderBys: Array<OrderItem<T>>): Page<T>

    /**
     * Paginate by page number, page size, order by and condition, ignore [KiteConfig.selectiveStrategy] value
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
     * Paginate by request and condition, ignore [KiteConfig.selectiveStrategy] value
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
     * Paginate by request, order by and condition, ignore [KiteConfig.selectiveStrategy] value
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
     * Paginate by request, order by and condition, ignore [KiteConfig.selectiveStrategy] value
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
     * Paginate by request, order by and condition, ignore [KiteConfig.selectiveStrategy] value
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
        return request.getParameter(PageConfig.pageNumberParameter)?.toLong() ?: PageConfig.pageNumber
    }

    /**
     * Get page size from request
     *
     * @param request HttpServletRequest
     * @return Page size
     */
    fun getPageSize(request: HttpServletRequest): Long {
        return request.getParameter(PageConfig.pageSizeParameter)?.toLong() ?: PageConfig.pageSize
    }

}
