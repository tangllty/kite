package com.tang.kite.mapper

import com.tang.kite.config.KiteConfig
import com.tang.kite.config.PageConfig
import com.tang.kite.paginate.OrderItem
import com.tang.kite.paginate.Page
import com.tang.kite.wrapper.delete.DeleteWrapper
import com.tang.kite.wrapper.query.QueryWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
import jakarta.servlet.http.HttpServletRequest
import java.io.Serializable

/**
 * Base mapper
 *
 * @param T Entity type
 *
 * @author Tang
 */
interface BaseMapper<T : Any> {

    /**
     * Insert entity
     *
     * @param entity Entity
     * @return Inserted count
     */
    fun insert(entity: T): Int

    /**
     * Insert entity selective, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entity Entity
     * @return Inserted count
     */
    fun insertSelective(entity: T): Int

    /**
     * Insert entity values, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity list
     * @param batchSize Batch size
     * @return Inserted count
     */
    fun insertValues(entities: Iterable<T>, batchSize: Int): Int

    /**
     * Insert entity values, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity array
     * @param batchSize Batch size
     * @return Inserted count
     */
    fun insertValues(entities: Array<T>, batchSize: Int): Int {
        return insertValues(entities.toList(), batchSize)
    }

    /**
     * Insert entity values, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity list
     * @return Inserted count
     */
    fun insertValues(entities: Iterable<T>): Int {
        return insertValues(entities, KiteConfig.batchSize)
    }

    /**
     * Insert entity values, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity array
     * @return Inserted count
     */
    fun insertValues(entities: Array<T>): Int {
        return insertValues(entities.toList())
    }

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
     * @param entities Entity array
     * @param batchSize Batch size
     * @return Inserted count
     */
    fun batchInsert(entities: Array<T>, batchSize: Int): Int {
        return batchInsert(entities.toList(), batchSize)
    }

    /**
     * Batch insert entity
     *
     * @param entities Entity list
     * @return Inserted count
     */
    fun batchInsert(entities: Iterable<T>): Int {
        return batchInsert(entities, KiteConfig.batchSize)
    }

    /**
     * Batch insert entity
     *
     * @param entities Entity array
     * @return Inserted count
     */
    fun batchInsert(entities: Array<T>): Int {
        return batchInsert(entities.toList())
    }

    /**
     * Batch insert entity selective, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity list
     * @param batchSize Batch size
     * @return Inserted count
     */
    fun batchInsertSelective(entities: Iterable<T>, batchSize: Int): Int

    /**
     * Batch insert entity selective, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity array
     * @param batchSize Batch size
     * @return Inserted count
     */
    fun batchInsertSelective(entities: Array<T>, batchSize: Int): Int {
        return batchInsertSelective(entities.toList())
    }

    /**
     * Batch insert entity selective, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity list
     * @return Inserted count
     */
    fun batchInsertSelective(entities: Iterable<T>): Int {
        return batchInsertSelective(entities, KiteConfig.batchSize)
    }

    /**
     * Batch insert entity selective, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity array
     * @return Inserted count
     */
    fun batchInsertSelective(entities: Array<T>): Int {
        return batchInsertSelective(entities.toList())
    }

    /**
     * Update entity by primary key
     *
     * @param entity Entity
     * @return Updated count
     */
    fun update(entity: T): Int

    /**
     * Update entity by where entity condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * The primary key will also be updated, if set in the value entity
     *
     * @param entity Value entity
     * @param conditionEntity Condition entity
     * @return Updated count
     */
    fun update(entity: T, conditionEntity: T): Int

    /**
     * Update entity selective by primary key, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entity Entity
     * @return Updated count
     */
    fun updateSelective(entity: T): Int

    /**
     * Update entity selective by where entity condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * The primary key will also be updated, if set in the value entity
     *
     * @param entity Value entity
     * @param conditionEntity Condition entity
     * @return Updated count
     */
    fun updateSelective(entity: T, conditionEntity: T): Int

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
     * Batch update entity by primary key
     *
     * @param entities Entity list
     * @param batchSize Batch size
     * @return Updated count
     */
    fun batchUpdate(entities: Iterable<T>, batchSize: Int): Int

    /**
     * Batch update entity by primary key
     *
     * @param entities Entity array
     * @param batchSize Batch size
     * @return Updated count
     */
    fun batchUpdate(entities: Array<T>, batchSize: Int): Int {
        return batchUpdate(entities.toList(), batchSize)
    }

    /**
     * Batch update entity by primary key
     *
     * @param entities Entity list
     * @return Updated count
     */
    fun batchUpdate(entities: Iterable<T>): Int {
        return batchUpdate(entities, KiteConfig.batchSize)
    }

    /**
     * Batch update entity by primary key
     *
     * @param entities Entity array
     * @return Updated count
     */
    fun batchUpdate(entities: Array<T>): Int {
        return batchUpdate(entities.toList())
    }

    /**
     * Batch update entity selective by primary key, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity list
     * @param batchSize Batch size
     * @return Updated count
     */
    fun batchUpdateSelective(entities: Iterable<T>, batchSize: Int): Int

    /**
     * Batch update entity selective by primary key, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity array
     * @param batchSize Batch size
     * @return Updated count
     */
    fun batchUpdateSelective(entities: Array<T>, batchSize: Int): Int {
        return batchUpdateSelective(entities.toList(), batchSize)
    }

    /**
     * Batch update entity selective by primary key, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity list
     * @return Updated count
     */
    fun batchUpdateSelective(entities: Iterable<T>): Int {
        return batchUpdateSelective(entities, KiteConfig.batchSize)
    }

    /**
     * Batch update entity selective by primary key, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entities Entity array
     * @return Updated count
     */
    fun batchUpdateSelective(entities: Array<T>): Int {
        return batchUpdateSelective(entities.toList())
    }

    /**
     * Delete entity by condition, all fields are used as condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param entity Entity
     * @return Deleted count
     */
    fun delete(entity: T): Int

    /**
     * Delete entity by primary key
     *
     * @param id Entity id
     * @return Deleted count
     */
    fun deleteById(id: Serializable): Int

    /**
     * Delete entity by primary keys
     *
     * @param ids Entity ids
     * @return Deleted count
     */
    fun deleteByIds(ids: Iterable<Serializable>): Int

    /**
     * Delete entity by primary keys
     *
     * @param ids Entity ids
     * @return Deleted count
     */
    fun deleteByIds(ids: Array<Serializable>): Int {
        return deleteByIds(ids.toList())
    }

    /**
     * Delete entity by delete wrapper
     */
    fun deleteWrapper(deleteWrapper: DeleteWrapper<T>): Int

    /**
     * Delete entity by delete wrapper
     */
    fun deleteWrapper(): DeleteWrapper<T> {
        return DeleteWrapper(this)
    }

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
     * @param entity Entity
     * @return Entity list
     */
    fun select(entity: T): List<T>

    /**
     * Select by condition and order by
     *
     * @param entity Entity
     * @param orderBy Order by
     * @return Entity list
     */
    fun select(entity: T, orderBy: OrderItem<T>): List<T> {
        return select(entity, arrayOf(orderBy))
    }

    /**
     * Select by condition and order by
     *
     * @param entity Entity
     * @param orderBys Order by array
     * @return Entity list
     */
    fun select(entity: T, orderBys: Array<OrderItem<T>>): List<T>

    /**
     * Select by condition and order by
     *
     * @param entity Entity
     * @param orderBys Order by list
     * @return Entity list
     */
    fun select(entity: T, orderBys: List<OrderItem<T>>): List<T> {
        return select(entity, orderBys.toTypedArray())
    }

    /**
     * Select by query wrapper
     *
     * @param queryWrapper QueryWrapper
     * @return Entity list
     */
    fun queryWrapper(queryWrapper: QueryWrapper<T>): List<T>

    /**
     * Select by primary key
     *
     * @param id Entity id
     * @return Entity
     */
    fun selectById(id: Serializable): T?

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
     * @param entity Entity
     * @return Entity list
     */
    fun selectWithJoins(entity: T): List<T>

    /**
     * Select by condition and order by with joins
     *
     * @param entity Entity
     * @param orderBy Order by
     * @return Entity list
     */
    fun selectWithJoins(entity: T, orderBy: OrderItem<T>): List<T> {
        return select(entity, arrayOf(orderBy))
    }

    /**
     * Select by condition and order by with joins
     *
     * @param entity Entity
     * @param orderBys Order by array
     * @return Entity list
     */
    fun selectWithJoins(entity: T, orderBys: Array<OrderItem<T>>): List<T>

    /**
     * Select by condition and order by with joins
     *
     * @param entity Entity
     * @param orderBys Order by list
     * @return Entity list
     */
    fun selectWithJoins(entity: T, orderBys: List<OrderItem<T>>): List<T> {
        return select(entity, orderBys.toTypedArray())
    }

    /**
     * Select by primary key with joins
     *
     * @param id Entity id
     * @return Entity
     */
    fun selectByIdWithJoins(id: Serializable): T?

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
     * @param entity Entity
     * @return Count
     */
    fun count(entity: T): Long

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
     * @param entity Entity
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, entity: T): Page<T>

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
     * @param entity Entity
     * @param orderBy Order by
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, entity: T, orderBy: OrderItem<T>): Page<T> {
        return paginate(pageNumber, pageSize, entity, arrayOf(orderBy))
    }

    /**
     * Paginate by page number, page size, order by and condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param entity Entity
     * @param orderBys Order by array
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, entity: T, orderBys: Array<OrderItem<T>>): Page<T>

    /**
     * Paginate by page number, page size, order by and condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param entity Entity
     * @param orderBys Order by list
     * @return Page
     */
    fun paginate(pageNumber: Long, pageSize: Long, entity: T, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(pageNumber, pageSize, entity, orderBys.toTypedArray())
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
     * @param entity Entity
     * @return Page
     */
    fun paginate(request: HttpServletRequest, entity: T): Page<T> {
        return paginate(getPageNumber(request), getPageSize(request), entity)
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
     * @param entity Entity
     * @param orderBy Order by
     * @return Page
     */
    fun paginate(request: HttpServletRequest, entity: T, orderBy: OrderItem<T>): Page<T> {
        return paginate(request, entity, arrayOf(orderBy))
    }

    /**
     * Paginate by request, order by and condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param request HttpServletRequest
     * @param entity Entity
     * @param orderBys Order by array
     * @return Page
     */
    fun paginate(request: HttpServletRequest, entity: T, orderBys: Array<OrderItem<T>>): Page<T> {
        return paginate(getPageNumber(request), getPageSize(request), entity, orderBys)
    }

    /**
     * Paginate by request, order by and condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param request HttpServletRequest
     * @param entity Entity
     * @param orderBys Order by list
     * @return Page
     */
    fun paginate(request: HttpServletRequest, entity: T, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(request, entity, orderBys.toTypedArray())
    }

    /**
     * Paginate with joins by page number and page size
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @return Page
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long): Page<T>

    /**
     * Paginate with joins by page number, page size and condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param entity Entity
     * @return Page
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, entity: T): Page<T>

    /**
     * Paginate with joins by page number, page size and order by
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBy Order by
     * @return Page
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, orderBy: OrderItem<T>): Page<T> {
        return paginateWithJoins(pageNumber, pageSize, arrayOf(orderBy))
    }

    /**
     * Paginate with joins by page number, page size and order by
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBys Order by array
     * @return Page
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, orderBys: Array<OrderItem<T>>): Page<T>

    /**
     * Paginate with joins by page number, page size and order by
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBys Order by list
     * @return Page
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, orderBys: List<OrderItem<T>>): Page<T> {
        return paginateWithJoins(pageNumber, pageSize, orderBys.toTypedArray())
    }

    /**
     * Paginate with joins by page number, page size, order by and condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param entity Entity
     * @param orderBy Order by
     * @return Page
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, entity: T, orderBy: OrderItem<T>): Page<T> {
        return paginateWithJoins(pageNumber, pageSize, entity, arrayOf(orderBy))
    }

    /**
     * Paginate with joins by page number, page size, order by and condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param entity Entity
     * @param orderBys Order by array
     * @return Page
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, entity: T, orderBys: Array<OrderItem<T>>): Page<T>

    /**
     * Paginate with joins by page number, page size, order by and condition, ignore [KiteConfig.selectiveStrategy] value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param entity Entity
     * @param orderBys Order by list
     * @return Page
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, entity: T, orderBys: List<OrderItem<T>>): Page<T> {
        return paginateWithJoins(pageNumber, pageSize, entity, orderBys.toTypedArray())
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
