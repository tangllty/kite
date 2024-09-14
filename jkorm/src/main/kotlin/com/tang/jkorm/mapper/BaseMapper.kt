package com.tang.jkorm.mapper

import com.tang.jkorm.paginate.Page

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
     */
    fun insert(type: T): Int

    /**
     * Insert entity selective, ignore null value
     *
     * @param type Entity
     */
    fun insertSelective(type: T): Int

    fun batchInsert(list: Iterable<T>): Int

    fun batchInsert(list: Array<T>): Int {
        return batchInsert(list.toList())
    }

    fun batchInsertSelective(list: Iterable<T>): Int

    fun batchInsertSelective(list: Array<T>): Int {
        return batchInsertSelective(list.toList())
    }

    /**
     * Update entity by primary key
     *
     * @param type Entity
     */
    fun update(type: T): Int

    /**
     * Update entity by where entity condition, ignore null value
     *
     * The primary key will also be updated, if set in the value entity
     *
     * @param type Value entity
     * @param condition Condition entity
     */
    fun update(type: T, condition: T): Int

    /**
     * Update entity selective by primary key, ignore null value
     *
     * @param type Entity
     */
    fun updateSelective(type: T): Int

    /**
     * Delete entity by condition, all fields are used as condition, ignore null value
     *
     * @param type Entity
     */
    fun delete(type: T): Int

    /**
     * Delete entity by primary key
     *
     * @param id Entity id
     */
    fun deleteById(id: Long): Int

    /**
     * Select all
     */
    fun select(): List<T>

    /**
     * Select by condition, ignore null value
     *
     * @param type Entity
     */
    fun select(type: T): List<T>

    /**
     * Select by primary key
     *
     * @param id Entity id
     */
    fun selectById(id: Long): T?

    /**
     * Count all
     */
    fun count(): Long

    /**
     * Count by condition, ignore null value
     *
     * @param type Entity
     */
    fun count(type: T): Long

    /**
     * Paginate by page number and page size
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     */
    fun paginate(pageNumber: Long, pageSize: Long): Page<T>

    /**
     * Paginate by page number, page size and condition, ignore null value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param type Entity
     */
    fun paginate(pageNumber: Long, pageSize: Long, type: T): Page<T>

    /**
     * Paginate by page number, page size and order by
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBys Order by
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBys: Array<Pair<String, Boolean>>): Page<T>

    /**
     * Paginate by page number, page size and order by
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBys Order by
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBys: List<Pair<String, Boolean>>): Page<T> {
        return paginate(pageNumber, pageSize, orderBys.toTypedArray())
    }

    /**
     * Paginate by page number, page size, order by and condition, ignore null value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBys Order by
     * @param type Entity
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBys: Array<Pair<String, Boolean>>, type: T): Page<T>

    /**
     * Paginate by page number, page size, order by and condition, ignore null value
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBys Order by
     * @param type Entity
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBys: List<Pair<String, Boolean>>, type: T): Page<T> {
        return paginate(pageNumber, pageSize, orderBys.toTypedArray(), type)
    }

}
