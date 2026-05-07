package com.tang.kite.config

import com.tang.kite.annotation.field.CreateTime
import com.tang.kite.annotation.field.UpdateTime
import com.tang.kite.config.defaults.DefaultSelectiveStrategy
import com.tang.kite.config.logical.LogicalDeletionConfig
import com.tang.kite.config.optimistic.OptimisticLockConfig
import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.config.table.TableConfig
import com.tang.kite.config.tenant.TenantConfig
import com.tang.kite.enumeration.SqlType
import com.tang.kite.handler.field.FieldHandler
import com.tang.kite.handler.field.FieldMetaKey
import com.tang.kite.handler.field.TimeFieldHandler
import com.tang.kite.handler.result.ResultHandler
import com.tang.kite.sql.dialect.SqlDialect

/**
 * Kite properties class includes core properties for Kite framework.
 *
 * @author Tang
 */
object KiteConfig {

    /**
     * Whether to display the banner during application startup.
     */
    @JvmStatic
    var banner = true

    /**
     * The strategy for selective methods.
     */
    @JvmStatic
    var selectiveStrategy: SelectiveStrategy = DefaultSelectiveStrategy

    /**
     * The batch size for operations like inserts or updates.
     */
    @JvmStatic
    var batchSize = 1000

    /**
     * SQL dialects for different databases.
     */
    @JvmStatic
    var dialects: MutableMap<String, SqlDialect> = mutableMapOf()

    /**
     * Field handlers for handling field types and annotations.
     */
    @JvmStatic
    var fieldHandlers: MutableMap<FieldMetaKey, FieldHandler> = mutableMapOf(
        FieldMetaKey(CreateTime::class, SqlType.INSERT) to TimeFieldHandler,
        FieldMetaKey(UpdateTime::class, SqlType.UPDATE) to TimeFieldHandler
    )

    /**
     * Result handlers for handling result types.
     */
    @JvmStatic
    var resultHandlers: MutableMap<Class<*>, ResultHandler> = mutableMapOf()

    /**
     * Page properties for pagination configuration.
     */
    @JvmStatic
    val page = PageConfig

    /**
     * SQL properties for SQL configuration.
     */
    @JvmStatic
    val sql = SqlConfig

    /**
     * Table properties for table configuration.
     */
    @JvmStatic
    val table = TableConfig

    /**
     * Logical deletion properties for logical deletion configuration.
     */
    @JvmStatic
    val logicalDelete = LogicalDeletionConfig

    /**
     * Tenant properties for tenant configuration.
     */
    @JvmStatic
    val tenant = TenantConfig

    /**
     * Schema properties for schema configuration.
     */
    @JvmStatic
    val schema = SchemaConfig

    /**
     * Optimistic lock properties for optimistic locking configuration.
     */
    @JvmStatic
    val optimisticLock = OptimisticLockConfig

}
