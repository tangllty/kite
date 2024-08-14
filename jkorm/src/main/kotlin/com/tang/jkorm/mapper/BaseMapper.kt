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
     * Insert entity selective
     *
     * @param type Entity
     */
    fun insertSelective(type: T): Int

    /**
     * Update entity
     *
     * @param type Entity
     */
    fun update(type: T): Int

    /**
     * Update entity selective
     *
     * @param type Entity
     */
    fun updateSelective(type: T): Int

    /**
     * Delete entity
     *
     * @param type Entity
     */
    fun delete(type: T): Int

    /**
     * Delete entity by id
     *
     * @param id Entity id
     */
    fun deleteById(id: Long): Int

    /**
     * Select all
     */
    fun select(): List<T>

    /**
     * Select by condition
     *
     * @param type Entity
     */
    fun select(type: T): List<T>

    /**
     * Select by id
     *
     * @param id Entity id
     */
    fun selectById(id: Long): T?

    /**
     * Count all
     */
    fun count(): Long

    /**
     * Count by condition
     *
     * @param type Entity
     */
    fun count(type: T): Long

    /**
     * Paginate
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     */
    fun paginate(pageNumber: Long, pageSize: Long): Page<T>

    /**
     * Paginate
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param type Entity
     */
    fun paginate(pageNumber: Long, pageSize: Long, type: T): Page<T>

    /**
     * Paginate
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBys Order by
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBys: Array<Pair<String, Boolean>>): Page<T>

    /**
     * Paginate
     *
     * @param pageNumber Page number
     * @param pageSize Page size
     * @param orderBys Order by
     * @param type Entity
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBys: Array<Pair<String, Boolean>>, type: T): Page<T>

}
