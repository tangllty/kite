package com.tang.kite.boot.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Spring Boot 3.x compatible Kite properties classes.
 * These classes re-declare `@ConfigurationProperties` to work around Spring Boot 3.x
 * limitations with `@ConfigurationProperties` on open classes.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = KiteProperties.KITE_PREFIX)
class SpringBoot3KiteProperties : KiteProperties()

@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = PageProperties.PAGE_PREFIX)
class SpringBoot3PageProperties : PageProperties()

@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = SqlProperties.SQL_PREFIX)
class SpringBoot3SqlProperties : SqlProperties()

@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = TableProperties.TABLE_PREFIX)
class SpringBoot3TableProperties : TableProperties()

@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = LogicalDeletionProperties.LOGICAL_DELETE_PREFIX)
class SpringBoot3LogicalDeletionProperties : LogicalDeletionProperties()

@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = TenantProperties.TENANT_PREFIX)
class SpringBoot3TenantProperties : TenantProperties()

@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = DataSourceProperties.DATA_SOURCE_PREFIX)
class SpringBoot3DataSourceProperties : DataSourceProperties()

@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = DataSourceConfigProperties.DATA_SOURCE_CONFIG_PREFIX)
class SpringBoot3DataSourceConfigProperties : DataSourceConfigProperties()

@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = SchemaProperties.SCHEMA_PREFIX)
class SpringBoot3SchemaProperties : SchemaProperties()


@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = OptimisticLockProperties.OPTIMISTIC_LOCK_PREFIX)
class SpringBoot3OptimisticLockProperties : OptimisticLockProperties()
