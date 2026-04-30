package com.tang.kite.boot.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties

/**
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
@ConfigurationProperties(prefix = SchemaProperties.SCHEMA_PREFIX)
class SpringBoot3SchemaProperties : SchemaProperties()
