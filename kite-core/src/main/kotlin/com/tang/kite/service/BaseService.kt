package com.tang.kite.service

import com.tang.kite.config.KiteConfig
import com.tang.kite.mapper.BaseMapper
import com.tang.kite.paginate.OrderItem
import com.tang.kite.paginate.Page
import com.tang.kite.wrapper.delete.DeleteWrapper
import com.tang.kite.wrapper.query.QueryWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
import java.io.Serializable

/**
 * Base service interface
 *
 * @author Tang
 */
interface BaseService<T : Any> : BaseMapper<T> {

    fun getMapper(): BaseMapper<T>

    /**
     * Insert a single entity
     *
     * @param entity The entity to be inserted
     * @return Number of affected rows
     */
    override fun insert(entity: T): Int {
        return getMapper().insert(entity)
    }

    /**
     * Insert a single entity selectively (only non-null fields are inserted, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param entity The entity to be inserted
     * @return Number of affected rows
     */
    override fun insertSelective(entity: T): Int {
        return getMapper().insertSelective(entity)
    }

    /**
     * Batch insert entities with VALUES syntax
     *
     * @param entities The collection of entities to be inserted
     * @param batchSize Number of inserts per batch
     * @return Total number of successfully inserted rows
     */
    override fun insertValues(entities: Iterable<T>, batchSize: Int): Int {
        return getMapper().insertValues(entities, batchSize)
    }

    /**
     * Batch insert entities with single INSERT syntax
     *
     * @param list The collection of entities to be inserted
     * @param batchSize Number of inserts per batch
     * @return Total number of successfully inserted rows
     */
    override fun batchInsert(list: Iterable<T>, batchSize: Int): Int {
        return getMapper().batchInsert(list, batchSize)
    }

    /**
     * Update an entity by primary key
     *
     * @param entity The entity to be updated
     * @return Number of affected rows
     */
    override fun update(entity: T): Int {
        return getMapper().update(entity)
    }

    /**
     * Update entity by condition entity (full-field update, all fields of condition entity are used as query conditions)
     * (only non-null fields are updated, ignoring [KiteConfig.selectiveStrategy])
     *
     * The primary key will also be updated if set in the value entity
     *
     * @param entity The entity to be updated
     * @param conditionEntity The condition entity for update
     * @return Number of affected rows
     */
    override fun update(entity: T, conditionEntity: T): Int {
        return getMapper().update(entity, conditionEntity)
    }

    /**
     * Update an entity selectively by primary key (only non-null fields are updated, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param entity The entity to be updated
     * @return Number of affected rows
     */
    override fun updateSelective(entity: T): Int {
        return getMapper().updateSelective(entity)
    }

    /**
     * Update entity by condition entity (selective update, all fields of condition entity are used as query conditions)
     * (only non-null fields are updated, ignoring [KiteConfig.selectiveStrategy])
     *
     * The primary key will also be updated if set in the value entity
     *
     * @param entity The entity to be updated
     * @param conditionEntity The condition entity for update
     * @return Number of affected rows
     */
    override fun updateSelective(entity: T, conditionEntity: T): Int {
        return getMapper().updateSelective(entity, conditionEntity)
    }

    /**
     * Update entity by [UpdateWrapper]
     *
     * @param updateWrapper The [UpdateWrapper]
     * @return Number of affected rows
     */
    override fun updateWrapper(updateWrapper: UpdateWrapper<T>): Int {
        return getMapper().updateWrapper(updateWrapper)
    }

    /**
     * Batch update entities by primary key
     *
     * @param entities The collection of entities to be updated
     * @param batchSize Number of updates per batch
     * @return Total number of successfully updated rows
     */
    override fun batchUpdate(entities: Iterable<T>, batchSize: Int): Int {
        return getMapper().batchUpdate(entities, batchSize)
    }

    /**
     * Delete entities by entity conditions (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     * If all fields of the entity are null, all table data will be deleted
     *
     * @param entity The condition entity for deletion
     * @return Number of affected rows
     */
    override fun delete(entity: T): Int {
        return getMapper().delete(entity)
    }

    /**
     * Delete an entity by primary key
     *
     * @param id The primary key value of the entity
     * @return Number of affected rows
     */
    override fun deleteById(id: Serializable): Int {
        return getMapper().deleteById(id)
    }

    /**
     * Delete entities by primary keys
     *
     * @param ids The collection of primary key values
     * @return Number of affected rows
     */
    override fun deleteByIds(ids: Iterable<Serializable>): Int {
        return getMapper().deleteByIds(ids)
    }

    /**
     * Delete entities by [DeleteWrapper]
     *
     * @param deleteWrapper The [DeleteWrapper]
     * @return Number of affected rows
     */
    override fun deleteWrapper(deleteWrapper: DeleteWrapper<T>): Int {
        return getMapper().deleteWrapper(deleteWrapper)
    }

    /**
     * Query all table data
     *
     * @return The collection of entities
     */
    override fun select(): List<T> {
        return getMapper().select()
    }

    /**
     * Query all table data and sort by multiple fields
     *
     * @param orderBys Array of sort conditions
     * @return The collection of entities
     */
    override fun select(orderBys: Array<OrderItem<T>>): List<T> {
        return getMapper().select(orderBys)
    }

    /**
     * Query data by entity conditions (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param entity The condition entity for query
     * @return The collection of entities
     */
    override fun select(entity: T): List<T> {
        return getMapper().select(entity)
    }

    /**
     * Query data by entity conditions and sort by multiple fields
     *
     * @param entity The condition entity for query
     * @param orderBys Array of sort conditions
     * @return The collection of entities
     */
    override fun select(entity: T, orderBys: Array<OrderItem<T>>): List<T> {
        return getMapper().select(entity, orderBys)
    }

    /**
     * Query data by [QueryWrapper]
     *
     * @param queryWrapper The [QueryWrapper] instance
     * @return The collection of entities
     */
    override fun queryWrapper(queryWrapper: QueryWrapper<T>): List<T> {
        return getMapper().queryWrapper(queryWrapper)
    }

    /**
     * Query an entity by primary key
     *
     * @param id The primary key value
     * @return The matched entity
     */
    override fun selectById(id: Serializable): T? {
        return getMapper().selectById(id)
    }

    /**
     * Query a single entity by [QueryWrapper]
     *
     * @param queryWrapper The [QueryWrapper] instance
     * @return The matched entity
     */
    override fun selectOneWrapper(queryWrapper: QueryWrapper<T>): T? {
        return getMapper().selectOneWrapper(queryWrapper)
    }

    /**
     * Query all table data with associated tables
     *
     * @return The collection of entities
     */
    override fun selectWithJoins(): List<T> {
        return getMapper().selectWithJoins()
    }

    /**
     * Query all table data with associated tables and sort by multiple fields
     *
     * @param orderBys Array of sort conditions
     * @return The collection of entities with join data
     */
    override fun selectWithJoins(orderBys: Array<OrderItem<T>>): List<T> {
        return getMapper().selectWithJoins(orderBys)
    }

    /**
     * Query data by entity conditions with associated tables (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param entity The condition entity for query
     * @return The collection of entities with join data
     */
    override fun selectWithJoins(entity: T): List<T> {
        return getMapper().selectWithJoins(entity)
    }

    /**
     * Query data by entity conditions with associated tables and sort by multiple fields
     *
     * @param entity The condition entity for query
     * @param orderBys Array of sort conditions
     * @return The collection of entities with join data
     */
    override fun selectWithJoins(entity: T, orderBys: Array<OrderItem<T>>): List<T> {
        return getMapper().selectWithJoins(entity, orderBys)
    }

    /**
     * Query an entity by primary key with associated tables
     *
     * @param id The primary key value
     * @return The matched entity with join data
     */
    override fun selectByIdWithJoins(id: Serializable): T? {
        return getMapper().selectByIdWithJoins(id)
    }

    /**
     * Count the total number of table rows
     *
     * @return Total number of rows
     */
    override fun count(): Long {
        return getMapper().count()
    }

    /**
     * Count the number of rows by entity conditions (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param entity The condition entity for counting
     * @return Number of matched rows
     */
    override fun count(entity: T): Long {
        return getMapper().count(entity)
    }

    /**
     * Count the number of rows by [QueryWrapper]
     *
     * @param countWrapper The [QueryWrapper] instance
     * @return Number of matched rows
     */
    override fun countWrapper(countWrapper: QueryWrapper<T>): Long {
        return getMapper().countWrapper(countWrapper)
    }

    /**
     * Pagination query for all table data
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @return Pagination result object
     */
    override fun paginate(pageNumber: Long, pageSize: Long): Page<T> {
        return getMapper().paginate(pageNumber, pageSize)
    }

    /**
     * Pagination query for data matching the entity conditions (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param entity The condition entity for query
     * @return Pagination result object
     */
    override fun paginate(pageNumber: Long, pageSize: Long, entity: T): Page<T> {
        return getMapper().paginate(pageNumber, pageSize, entity)
    }

    /**
     * Pagination query for all table data and sort by multiple fields
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param orderBys Array of sort conditions
     * @return Pagination result object
     */
    override fun paginate(pageNumber: Long, pageSize: Long, orderBys: Array<OrderItem<T>>): Page<T> {
        return getMapper().paginate(pageNumber, pageSize, orderBys)
    }

    /**
     * Pagination query for data matching the entity conditions and sort by multiple fields
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param entity The condition entity for query
     * @param orderBys Array of sort conditions
     * @return Pagination result object
     */
    override fun paginate(pageNumber: Long, pageSize: Long, entity: T, orderBys: Array<OrderItem<T>>): Page<T> {
        return getMapper().paginate(pageNumber, pageSize, entity, orderBys)
    }

    /**
     * Pagination query for all table data with associated tables
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @return Pagination result object with associated data
     */
    override fun paginateWithJoins(pageNumber: Long, pageSize: Long): Page<T> {
        return getMapper().paginateWithJoins(pageNumber, pageSize)
    }

    /**
     * Pagination query for data matching the entity conditions with associated tables (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param entity The condition entity for query
     * @return Pagination result object with associated data
     */
    override fun paginateWithJoins(pageNumber: Long, pageSize: Long, entity: T): Page<T> {
        return getMapper().paginateWithJoins(pageNumber, pageSize, entity)
    }

    /**
     * Pagination query for all table data with associated tables and sort by multiple fields
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param orderBys Array of sort conditions
     * @return Pagination result object with associated data
     */
    override fun paginateWithJoins(pageNumber: Long, pageSize: Long, orderBys: Array<OrderItem<T>>): Page<T> {
        return getMapper().paginateWithJoins(pageNumber, pageSize, orderBys)
    }

    /**
     * Pagination query for data matching the entity conditions with associated tables and sort by multiple fields (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param entity The condition entity for query
     * @param orderBys Array of sort conditions
     * @return Pagination result object with associated data
     */
    override fun paginateWithJoins(pageNumber: Long, pageSize: Long, entity: T, orderBys: Array<OrderItem<T>>): Page<T> {
        return getMapper().paginateWithJoins(pageNumber, pageSize, entity, orderBys)
    }

}
