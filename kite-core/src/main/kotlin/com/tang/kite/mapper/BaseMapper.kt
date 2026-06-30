package com.tang.kite.mapper

import com.tang.kite.config.KiteConfig
import com.tang.kite.paginate.OrderItem
import com.tang.kite.paginate.Page
import com.tang.kite.table.TableManager
import com.tang.kite.utils.Reflects
import com.tang.kite.wrapper.delete.DeleteWrapper
import com.tang.kite.wrapper.query.QueryWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
import java.io.Serializable
import java.util.function.Function
import java.util.function.Supplier

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
        return selectWithJoins(arrayOf(orderBy))
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
        return selectWithJoins(orderBys.toTypedArray())
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
        return selectWithJoins(entity, arrayOf(orderBy))
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
        return selectWithJoins(entity, orderBys.toTypedArray())
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
     * Count the number of rows by [QueryWrapper]
     *
     * @param countWrapper The [QueryWrapper] instance
     * @return Number of matched rows
     */
    fun countWrapper(countWrapper: QueryWrapper<T>): Long

    /**
     * Create an instance of [QueryWrapper] for chained query condition construction
     *
     * @return A [QueryWrapper] instance
     */
    fun countWrapper(): QueryWrapper<T> {
        return QueryWrapper(this)
    }

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
     * Execute operations with a dynamic table name
     *
     * @param tableName the table name to use
     * @param block the operations to execute
     * @return the result of the block
     */
    fun <R> with(tableName: String, block: () -> R): R {
        return TableManager.with(tableName, block)
    }

    /**
     * Execute operations with a dynamic table name (Java API - no return value)
     *
     * @param tableName The target table name to use during execution
     * @param runnable The operations to execute under the specified table context
     */
    fun with(tableName: String, runnable: Runnable) {
        TableManager.with(tableName, runnable)
    }

    /**
     * Execute operations with a dynamic table name (Java Supplier API)
     *
     * @param tableName The target table name to use during execution
     * @param supplier The supplier to produce the result under the specified table context
     * @return The result provided by the supplier
     */
    fun <R> with(tableName: String, supplier: Supplier<R>): R {
        return TableManager.with(tableName, supplier)
    }

    /**
     * Execute operations with a table name suffix
     *
     * Automatically resolves the base table name from the mapper's entity type and appends the suffix.
     * Generated table name format: `baseTableName_suffix`
     *
     * @param suffix The suffix to append to the table name
     * @param block The operations to execute under the suffixed table context
     * @return The result returned by the block
     */
    fun <R> withSuffix(suffix: String, block: () -> R): R {
        return TableManager.withSuffix(getEntityClazz(), suffix, block)
    }

    /**
     * Execute operations with a table name suffix (Java API - no return value)
     *
     * @param suffix The suffix to append to the table name
     * @param runnable The operations to execute under the suffixed table context
     */
    fun withSuffix(suffix: String, runnable: Runnable) {
        TableManager.withSuffix(getEntityClazz(), suffix, runnable)
    }

    /**
     * Execute operations with a table name suffix (Java Supplier API)
     *
     * @param suffix The suffix to append to the table name
     * @param supplier The supplier to produce the result under the suffixed table context
     * @return The result provided by the supplier
     */
    fun <R> withSuffix(suffix: String, supplier: Supplier<R>): R {
        return TableManager.withSuffix(getEntityClazz(), suffix, supplier)
    }

    /**
     * Execute operations with a custom table name transformation
     *
     * Automatically resolves the base table name from the mapper's entity type and applies the transformation.
     *
     * @param transformer Transformation function that receives the base table name and returns the new table name
     * @param block The operations to execute under the transformed table context
     * @return The result returned by the block
     */
    fun <R> withTransform(transformer: (tableName: String) -> String, block: () -> R): R {
        return TableManager.withTransform(getEntityClazz(), transformer, block)
    }

    /**
     * Execute operations with a custom table name transformation (Java API - no return value)
     *
     * @param transformer Transformation function that receives the base table name and returns the new table name
     * @param runnable The operations to execute under the transformed table context
     */
    fun withTransform(transformer: Function<String, String>, runnable: Runnable) {
        TableManager.withTransform(getEntityClazz(), transformer, runnable)
    }

    /**
     * Execute operations with a custom table name transformation (Java Supplier API)
     *
     * @param transformer Transformation function that receives the base table name and returns the new table name
     * @param supplier The supplier to produce the result under the transformed table context
     * @return The result provided by the supplier
     */
    fun <R> withTransform(transformer: Function<String, String>, supplier: Supplier<R>): R {
        return TableManager.withTransform(getEntityClazz(), transformer, supplier)
    }

    /**
     * Create a table with the specified name
     *
     * @param tableName The name of the table to create
     * @return True if creation successful, false otherwise
     */
    fun createTable(tableName: String): Boolean

    /**
     * Create a table with the specified suffix
     *
     * @param suffix The suffix to append to the table name
     * @return True if creation successful, false otherwise
     */
    fun createTableWithSuffix(suffix: String): Boolean {
        return createTable(Reflects.getTableName(getEntityClazz()) + "_" + suffix)
    }

    /**
     * Create a table with a custom transformation
     *
     * @param tableNameTransformer Lambda that receives the base table name and returns the new table name
     * @return True if creation successful, false otherwise
     */
    fun createTableWithTransform(tableNameTransformer: (tableName: String) -> String): Boolean {
        return createTable(tableNameTransformer(Reflects.getTableName(getEntityClazz())))
    }

    /**
     * Create a table with a custom transformation
     *
     * @param tableNameTransformer Transformation function that receives the base table name and returns the new table name
     * @return True if creation successful, false otherwise
     */
    fun createTableWithTransform(tableNameTransformer: Function<String, String>): Boolean {
        return createTableWithTransform { tableNameTransformer.apply(it) }
    }

    /**
     * Drop a table with the specified name
     *
     * @param tableName The name of the table to drop
     * @return True if drop successful, false otherwise
     */
    fun dropTable(tableName: String): Boolean

    /**
     * Drop table with suffix based on entity base table name
     *
     * @param suffix Suffix append to base table name
     * @return True if drop success
     */
    fun dropTableWithSuffix(suffix: String): Boolean {
        return dropTable(Reflects.getTableName(getEntityClazz()) + "_" + suffix)
    }

    /**
     * Drop table with custom name transform (Kotlin lambda)
     *
     * @param tableNameTransformer Receive base table name, return final table name
     * @return True if drop success
     */
    fun dropTableWithTransform(tableNameTransformer: (baseTableName: String) -> String): Boolean {
        val baseTable = Reflects.getTableName(getEntityClazz())
        return dropTable(tableNameTransformer(baseTable))
    }

    /**
     * Drop table with custom name transform (Java Function compatible overload)
     *
     * @param tableNameTransformer Java Function transform table name
     * @return True if drop success
     */
    fun dropTableWithTransform(tableNameTransformer: Function<String, String>): Boolean {
        return dropTableWithTransform { tableNameTransformer.apply(it) }
    }

    /**
     * Truncate table by specified table name
     * Clear all table data, retain table structure
     *
     * @param tableName Target table name
     * @return True if truncate success
     */
    fun truncateTable(tableName: String): Boolean

    /**
     * Truncate table with suffix based on entity base table name
     *
     * @param suffix Suffix append to base table name
     * @return True if truncate success
     */
    fun truncateTableWithSuffix(suffix: String): Boolean {
        return truncateTable(Reflects.getTableName(getEntityClazz()) + "_" + suffix)
    }

    /**
     * Truncate table with custom name transform (Kotlin lambda)
     *
     * @param tableNameTransformer Receive base table name, return final table name
     * @return True if truncate success
     */
    fun truncateTableWithTransform(tableNameTransformer: (baseTableName: String) -> String): Boolean {
        val baseTable = Reflects.getTableName(getEntityClazz())
        return truncateTable(tableNameTransformer(baseTable))
    }

    /**
     * Truncate table with custom name transform (Java Function compatible overload)
     *
     * @param tableNameTransformer Java Function transform table name
     * @return True if truncate success
     */
    fun truncateTableWithTransform(tableNameTransformer: Function<String, String>): Boolean {
        return truncateTableWithTransform { tableNameTransformer.apply(it) }
    }

    /**
     * Resolves the entity class type from the mapper's generic type parameter.
     *
     * Uses reflection to extract the entity type `T` from the `BaseMapper<T>` interface implementation.
     *
     * @return The Class object representing the entity type `T`
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> BaseMapper<T>.getEntityClazz(): Class<T> {
        val mapperInterface = this.javaClass.interfaces.first { BaseMapper::class.java.isAssignableFrom(it) } as Class<BaseMapper<T>>
        return Reflects.getGenericType(mapperInterface)
    }

}
