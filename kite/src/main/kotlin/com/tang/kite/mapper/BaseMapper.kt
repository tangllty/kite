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
 * Generic base mapper interface for data access layer, encapsulates core database operations
 * including single-table/join query, CRUD, pagination, sorting, selective field update/insert and batch operations.
 *
 * @param T The type of the database entity mapped to the table
 * @author Tang
 */
interface BaseMapper<T : Any> {

    /**
     * Insert a single entity
     *
     * @param entity The entity to be inserted
     * @return Number of affected rows
     */
    fun insert(entity: T): Int

    /**
     * Insert a single entity selectively (only non-null fields are inserted, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param entity The entity to be inserted
     * @return Number of affected rows
     */
    fun insertSelective(entity: T): Int

    /**
     * Batch insert entities with VALUES syntax
     *
     * @param entities The collection of entities to be inserted
     * @param batchSize Number of inserts per batch
     * @return Total number of successfully inserted rows
     */
    fun insertValues(entities: Iterable<T>, batchSize: Int): Int

    /**
     * Batch insert entities with VALUES syntax
     *
     * @param entities The array of entities to be inserted
     * @param batchSize Number of inserts per batch
     * @return Total number of successfully inserted rows
     */
    fun insertValues(entities: Array<T>, batchSize: Int): Int {
        return insertValues(entities.toList(), batchSize)
    }

    /**
     * Batch insert entities with VALUES syntax (uses [KiteConfig.batchSize] as default batch size)
     *
     * @param entities The collection of entities to be inserted
     * @return Total number of successfully inserted rows
     */
    fun insertValues(entities: Iterable<T>): Int {
        return insertValues(entities, KiteConfig.batchSize)
    }

    /**
     * Batch insert entities with VALUES syntax (uses [KiteConfig.batchSize] as default batch size)
     *
     * @param entities The array of entities to be inserted
     * @return Total number of successfully inserted rows
     */
    fun insertValues(entities: Array<T>): Int {
        return insertValues(entities.toList())
    }

    /**
     * Batch insert entities with single INSERT syntax
     *
     * @param list The collection of entities to be inserted
     * @param batchSize Number of inserts per batch
     * @return Total number of successfully inserted rows
     */
    fun batchInsert(list: Iterable<T>, batchSize: Int): Int

    /**
     * Batch insert entities with single INSERT syntax
     *
     * @param entities The array of entities to be inserted
     * @param batchSize Number of inserts per batch
     * @return Total number of successfully inserted rows
     */
    fun batchInsert(entities: Array<T>, batchSize: Int): Int {
        return batchInsert(entities.toList(), batchSize)
    }

    /**
     * Batch insert entities with single INSERT syntax (uses [KiteConfig.batchSize] as default batch size)
     *
     * @param entities The collection of entities to be inserted
     * @return Total number of successfully inserted rows
     */
    fun batchInsert(entities: Iterable<T>): Int {
        return batchInsert(entities, KiteConfig.batchSize)
    }

    /**
     * Batch insert entities with single INSERT syntax (uses [KiteConfig.batchSize] as default batch size)
     *
     * @param entities The array of entities to be inserted
     * @return Total number of successfully inserted rows
     */
    fun batchInsert(entities: Array<T>): Int {
        return batchInsert(entities.toList())
    }

    /**
     * Update an entity by primary key
     *
     * @param entity The entity to be updated
     * @return Number of affected rows
     */
    fun update(entity: T): Int

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
    fun update(entity: T, conditionEntity: T): Int

    /**
     * Update an entity selectively by primary key (only non-null fields are updated, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param entity The entity to be updated
     * @return Number of affected rows
     */
    fun updateSelective(entity: T): Int

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
    fun updateSelective(entity: T, conditionEntity: T): Int

    /**
     * Update entity by [UpdateWrapper]
     *
     * @param updateWrapper The [UpdateWrapper]
     * @return Number of affected rows
     */
    fun updateWrapper(updateWrapper: UpdateWrapper<T>): Int

    /**
     * Create an instance of [UpdateWrapper]
     *
     * @return An [UpdateWrapper] instance
     */
    fun updateWrapper(): UpdateWrapper<T> {
        return UpdateWrapper(this)
    }

    /**
     * Batch update entities by primary key
     *
     * @param entities The collection of entities to be updated
     * @param batchSize Number of updates per batch
     * @return Total number of successfully updated rows
     */
    fun batchUpdate(entities: Iterable<T>, batchSize: Int): Int

    /**
     * Batch update entities by primary key
     *
     * @param entities The array of entities to be updated
     * @param batchSize Number of updates per batch
     * @return Total number of successfully updated rows
     */
    fun batchUpdate(entities: Array<T>, batchSize: Int): Int {
        return batchUpdate(entities.toList(), batchSize)
    }

    /**
     * Batch update entities by primary key (uses [KiteConfig.batchSize] as default batch size)
     *
     * @param entities The collection of entities to be updated
     * @return Total number of successfully updated rows
     */
    fun batchUpdate(entities: Iterable<T>): Int {
        return batchUpdate(entities, KiteConfig.batchSize)
    }

    /**
     * Batch update entities by primary key (uses [KiteConfig.batchSize] as default batch size)
     *
     * @param entities The array of entities to be updated
     * @return Total number of successfully updated rows
     */
    fun batchUpdate(entities: Array<T>): Int {
        return batchUpdate(entities.toList())
    }

    /**
     * Delete entities by entity conditions (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     * If all fields of the entity are null, all table data will be deleted
     *
     * @param entity The condition entity for deletion
     * @return Number of affected rows
     */
    fun delete(entity: T): Int

    /**
     * Delete an entity by primary key
     *
     * @param id The primary key value of the entity
     * @return Number of affected rows
     */
    fun deleteById(id: Serializable): Int

    /**
     * Delete entities by primary keys
     *
     * @param ids The collection of primary key values
     * @return Number of affected rows
     */
    fun deleteByIds(ids: Iterable<Serializable>): Int

    /**
     * Delete entities by primary keys
     *
     * @param ids The array of primary key values
     * @return Number of affected rows
     */
    fun deleteByIds(ids: Array<Serializable>): Int {
        return deleteByIds(ids.toList())
    }

    /**
     * Delete entities by [DeleteWrapper]
     *
     * @param deleteWrapper The [DeleteWrapper]
     * @return Number of affected rows
     */
    fun deleteWrapper(deleteWrapper: DeleteWrapper<T>): Int

    /**
     * Create an instance of [DeleteWrapper]
     *
     * @return An [DeleteWrapper] instance
     */
    fun deleteWrapper(): DeleteWrapper<T> {
        return DeleteWrapper(this)
    }

    /**
     * Query all table data
     *
     * @return The collection of entities
     */
    fun select(): List<T>

    /**
     * Query all table data and sort by the specified field
     *
     * @param orderBy Sort condition
     * @return The collection of entities
     */
    fun select(orderBy: OrderItem<T>): List<T> {
        return select(arrayOf(orderBy))
    }

    /**
     * Query all table data and sort by multiple fields
     *
     * @param orderBys Array of sort conditions
     * @return The collection of entities
     */
    fun select(orderBys: Array<OrderItem<T>>): List<T>

    /**
     * Query all table data and sort by multiple fields
     *
     * @param orderBys List of sort conditions
     * @return The collection of entities
     */
    fun select(orderBys: List<OrderItem<T>>): List<T> {
        return select(orderBys.toTypedArray())
    }

    /**
     * Query data by entity conditions (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param entity The condition entity for query
     * @return The collection of entities
     */
    fun select(entity: T): List<T>

    /**
     * Query data by entity conditions and sort by the specified field
     *
     * @param entity The condition entity for query
     * @param orderBy The sort condition
     * @return The collection of entities
     */
    fun select(entity: T, orderBy: OrderItem<T>): List<T> {
        return select(entity, arrayOf(orderBy))
    }

    /**
     * Query data by entity conditions and sort by multiple fields
     *
     * @param entity The condition entity for query
     * @param orderBys Array of sort conditions
     * @return The collection of entities
     */
    fun select(entity: T, orderBys: Array<OrderItem<T>>): List<T>

    /**
     * Query data by entity conditions and sort by multiple fields
     *
     * @param entity The condition entity for query
     * @param orderBys List of sort conditions
     * @return The collection of entities
     */
    fun select(entity: T, orderBys: List<OrderItem<T>>): List<T> {
        return select(entity, orderBys.toTypedArray())
    }

    /**
     * Query data by [QueryWrapper]
     *
     * @param queryWrapper The [QueryWrapper] instance
     * @return The collection of entities
     */
    fun queryWrapper(queryWrapper: QueryWrapper<T>): List<T>

    /**
     * Query an entity by primary key
     *
     * @param id The primary key value
     * @return The matched entity
     */
    fun selectById(id: Serializable): T?

    /**
     * Query a single entity by [QueryWrapper]
     *
     * @param queryWrapper The [QueryWrapper] instance
     * @return The matched entity
     */
    fun selectOneWrapper(queryWrapper: QueryWrapper<T>): T?

    /**
     * Query all table data with associated tables
     *
     * @return The collection of entities
     */
    fun selectWithJoins(): List<T>

    /**
     * Query all table data with associated tables and sort by the specified field
     *
     * @param orderBy The sort condition
     * @return The collection of entities
     */
    fun selectWithJoins(orderBy: OrderItem<T>): List<T> {
        return select(arrayOf(orderBy))
    }

    /**
     * Query all table data with associated tables and sort by multiple fields
     *
     * @param orderBys Array of sort conditions
     * @return The collection of entities with join data
     */
    fun selectWithJoins(orderBys: Array<OrderItem<T>>): List<T>

    /**
     * Query all table data with associated tables and sort by multiple fields
     *
     * @param orderBys List of sort conditions
     * @return The collection of entities with join data
     */
    fun selectWithJoins(orderBys: List<OrderItem<T>>): List<T> {
        return select(orderBys.toTypedArray())
    }

    /**
     * Query data by entity conditions with associated tables (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param entity The condition entity for query
     * @return The collection of entities with join data
     */
    fun selectWithJoins(entity: T): List<T>

    /**
     * Query data by entity conditions with associated tables and sort by the specified field
     *
     * @param entity The condition entity for query
     * @param orderBy The sort condition
     * @return The collection of entities with join data
     */
    fun selectWithJoins(entity: T, orderBy: OrderItem<T>): List<T> {
        return select(entity, arrayOf(orderBy))
    }

    /**
     * Query data by entity conditions with associated tables and sort by multiple fields
     *
     * @param entity The condition entity for query
     * @param orderBys Array of sort conditions
     * @return The collection of entities with join data
     */
    fun selectWithJoins(entity: T, orderBys: Array<OrderItem<T>>): List<T>

    /**
     * Query data by entity conditions with associated tables and sort by multiple fields
     *
     * @param entity The condition entity for query
     * @param orderBys List of sort conditions
     * @return The collection of entities with join data
     */
    fun selectWithJoins(entity: T, orderBys: List<OrderItem<T>>): List<T> {
        return select(entity, orderBys.toTypedArray())
    }

    /**
     * Query an entity by primary key with associated tables
     *
     * @param id The primary key value
     * @return The matched entity with join data
     */
    fun selectByIdWithJoins(id: Serializable): T?

    /**
     * Create an instance of [QueryWrapper] for chained query condition construction
     *
     * @return A [QueryWrapper] instance
     */
    fun queryWrapper(): QueryWrapper<T> {
        return QueryWrapper(this)
    }

    /**
     * Count the total number of table rows
     *
     * @return Total number of rows
     */
    fun count(): Long

    /**
     * Count the number of rows by entity conditions (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param entity The condition entity for counting
     * @return Number of matched rows
     */
    fun count(entity: T): Long

    /**
     * Pagination query for all table data
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @return Pagination result object
     */
    fun paginate(pageNumber: Long, pageSize: Long): Page<T>

    /**
     * Pagination query for data matching the entity conditions (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param entity The condition entity for query
     * @return Pagination result object
     */
    fun paginate(pageNumber: Long, pageSize: Long, entity: T): Page<T>

    /**
     * Pagination query for all table data and sort by the specified field
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param orderBy The sort condition
     * @return Pagination result object
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBy: OrderItem<T>): Page<T> {
        return paginate(pageNumber, pageSize, arrayOf(orderBy))
    }

    /**
     * Pagination query for all table data and sort by multiple fields
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param orderBys Array of sort conditions
     * @return Pagination result object
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBys: Array<OrderItem<T>>): Page<T>

    /**
     * Pagination query for all table data and sort by multiple fields
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param orderBys List of sort conditions
     * @return Pagination result object
     */
    fun paginate(pageNumber: Long, pageSize: Long, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(pageNumber, pageSize, orderBys.toTypedArray())
    }

    /**
     * Pagination query for data matching the entity conditions and sort by the specified field
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param entity The condition entity for query
     * @param orderBy Sort condition
     * @return Pagination result object
     */
    fun paginate(pageNumber: Long, pageSize: Long, entity: T, orderBy: OrderItem<T>): Page<T> {
        return paginate(pageNumber, pageSize, entity, arrayOf(orderBy))
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
    fun paginate(pageNumber: Long, pageSize: Long, entity: T, orderBys: Array<OrderItem<T>>): Page<T>

    /**
     * Pagination query for data matching the entity conditions and sort by multiple fields (list overload version)
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param entity The condition entity for query
     * @param orderBys List of sort conditions
     * @return Pagination result object
     */
    fun paginate(pageNumber: Long, pageSize: Long, entity: T, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(pageNumber, pageSize, entity, orderBys.toTypedArray())
    }

    /**
     * Parse pagination parameters from Http request and execute pagination query
     * Default pagination parameter keys: pageNumber, pageSize, see [PageConfig] for configuration
     *
     * @param request Http request object (uses default values if parameters are missing)
     * @return Pagination result object
     */
    fun paginate(request: HttpServletRequest): Page<T> {
        return paginate(getPageNumber(request), getPageSize(request))
    }

    /**
     * Parse pagination parameters from Http request and query data matching the entity conditions
     *
     * @param request Http request object
     * @param entity The condition entity for query
     * @return Pagination result object
     */
    fun paginate(request: HttpServletRequest, entity: T): Page<T> {
        return paginate(getPageNumber(request), getPageSize(request), entity)
    }

    /**
     * Parse pagination parameters from Http request, query all table data and sort by the specified field
     *
     * @param request Http request object
     * @param orderBy The sort condition
     * @return Pagination result object
     */
    fun paginate(request: HttpServletRequest, orderBy: OrderItem<T>): Page<T> {
        return paginate(request, arrayOf(orderBy))
    }

    /**
     * Parse pagination parameters from Http request, query all table data and sort by multiple fields
     *
     * @param request Http request object
     * @param orderBys Array of sort conditions
     * @return Pagination result object
     */
    fun paginate(request: HttpServletRequest, orderBys: Array<OrderItem<T>>): Page<T> {
        return paginate(getPageNumber(request), getPageSize(request), orderBys)
    }

    /**
     * Parse pagination parameters from Http request, query all table data and sort by multiple fields (list overload version)
     *
     * @param request Http request object
     * @param orderBys List of sort conditions
     * @return Pagination result object
     */
    fun paginate(request: HttpServletRequest, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(request, orderBys.toTypedArray())
    }

    /**
     * Parse pagination parameters from Http request, query data matching the entity conditions and sort by the specified field
     *
     * @param request Http request object
     * @param entity The condition entity for query
     * @param orderBy The sort condition
     * @return Pagination result object
     */
    fun paginate(request: HttpServletRequest, entity: T, orderBy: OrderItem<T>): Page<T> {
        return paginate(request, entity, arrayOf(orderBy))
    }

    /**
     * Parse pagination parameters from Http request, query data matching the entity conditions and sort by multiple fields
     *
     * @param request Http request object
     * @param entity The condition entity for query
     * @param orderBys Array of sort conditions
     * @return Pagination result object
     */
    fun paginate(request: HttpServletRequest, entity: T, orderBys: Array<OrderItem<T>>): Page<T> {
        return paginate(getPageNumber(request), getPageSize(request), entity, orderBys)
    }

    /**
     * Parse pagination parameters from Http request, query data matching the entity conditions and sort by multiple fields (list overload version)
     *
     * @param request Http request object
     * @param entity The condition entity for query
     * @param orderBys List of sort conditions
     * @return Pagination result object
     */
    fun paginate(request: HttpServletRequest, entity: T, orderBys: List<OrderItem<T>>): Page<T> {
        return paginate(request, entity, orderBys.toTypedArray())
    }

    /**
     * Pagination query for all table data with associated tables
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @return Pagination result object with associated data
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long): Page<T>

    /**
     * Pagination query for data matching the entity conditions with associated tables (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param entity The condition entity for query
     * @return Pagination result object with associated data
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, entity: T): Page<T>

    /**
     * Pagination query for all table data with associated tables and sort by the specified field
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param orderBy The sort condition
     * @return Pagination result object with associated data
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, orderBy: OrderItem<T>): Page<T> {
        return paginateWithJoins(pageNumber, pageSize, arrayOf(orderBy))
    }

    /**
     * Pagination query for all table data with associated tables and sort by multiple fields
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param orderBys Array of sort conditions
     * @return Pagination result object with associated data
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, orderBys: Array<OrderItem<T>>): Page<T>

    /**
     * Pagination query for all table data with associated tables and sort by multiple fields
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param orderBys List of sort conditions
     * @return Pagination result object with associated data
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, orderBys: List<OrderItem<T>>): Page<T> {
        return paginateWithJoins(pageNumber, pageSize, orderBys.toTypedArray())
    }

    /**
     * Pagination query for data matching the entity conditions with associated tables and sort by the specified field (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param entity The condition entity for query
     * @param orderBy The sort condition
     * @return Pagination result object with associated data
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, entity: T, orderBy: OrderItem<T>): Page<T> {
        return paginateWithJoins(pageNumber, pageSize, entity, arrayOf(orderBy))
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
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, entity: T, orderBys: Array<OrderItem<T>>): Page<T>

    /**
     * Pagination query for data matching the entity conditions with associated tables and sort by multiple fields (all fields are used as query conditions, ignoring [KiteConfig.selectiveStrategy])
     *
     * @param pageNumber Page number (starts from 1)
     * @param pageSize Number of rows per page
     * @param entity The condition entity for query
     * @param orderBys List of sort conditions
     * @return Pagination result object with associated data
     */
    fun paginateWithJoins(pageNumber: Long, pageSize: Long, entity: T, orderBys: List<OrderItem<T>>): Page<T> {
        return paginateWithJoins(pageNumber, pageSize, entity, orderBys.toTypedArray())
    }

    /**
     * Parse page number parameter from Http request
     * Prioritizes [PageConfig.pageNumberParameter] in request, uses [PageConfig.pageNumber] as default if missing
     *
     * @param request Http request object
     * @return Parsed page number (Long type, default value is 1)
     */
    fun getPageNumber(request: HttpServletRequest): Long {
        return request.getParameter(PageConfig.pageNumberParameter)?.toLong() ?: PageConfig.pageNumber
    }

    /**
     * Parse page size parameter from Http request
     * Prioritizes [PageConfig.pageSizeParameter] in request, uses [PageConfig.pageSize] as default if missing
     *
     * @param request Http request object
     * @return Parsed page size (Long type, default value is 10)
     */
    fun getPageSize(request: HttpServletRequest): Long {
        return request.getParameter(PageConfig.pageSizeParameter)?.toLong() ?: PageConfig.pageSize
    }

}
