## 1.0.13

### Bug Fixes

### Dependencies

 * Bump gradle/actions from 4.4.1 to 4.4.2
 * Bump actions/checkout from 4 to 5
 * Remove guava

### Features

 * Enhance `setValue` method to support additional data types for `PreparedStatement`
 * Support automatic fill handlers
 * Make wrapper chain operations support fill handlers
 * Refactor utility methods to use `Reflects` instead of `Fields`
 * Refactor SQL execution methods to use a common template for improved readability and maintainability
 * Refactor result set handling and join operations for improved clarity and consistency

## 1.0.12

### Bug Fixes

 * Modify `DefaultSelectiveStrategy` rule to ignore only null values

### Dependencies

 * Bump gradle from 8.14.1 to 9.0.0

### Features

 * Refactor Column and `FunctionColumn` classes to support nullable column names
 * Add batch insert methods with customizable batch size for improved performance
 * Add `DeleteWrapper` and related classes for enhanced delete operations
 * Change ID parameter type from Long to Serializable in mapper methods
 * Add `deleteByIds` methods for batch deletion of entities
 * Auto convert between Int and Long types in mapper methods
 * Refactor wrapper classes to improve initialization and condition handling
 * Refactor result handling logic to improve clarity and maintainability
 * Improve the testing of the time package
 * Support `enum` type in result handling
 * Support custom `ResultHandler` for flexible result processing
 * `SqlSession.getMapper` now supports `KClass<T>` for enhanced Kotlin compatibility
 * Add `insertValues`, `batchUpdate`, and `batchUpdateSelective` methods for batch operations

## 1.0.11

### Bug Fixes

### Dependencies

 * Bump gradle/actions from 4.4.0 to 4.4.1

### Features

 * Refactor `QueryWrapper` and `UpdateWrapper` logic to improve readability and maintainability
 * Refactor wrapper classes to remove unused type parameters for improved clarity
 * Refactor wrapper classes to use nullable instances and improve instance retrieval logic

## 1.0.10

### Bug Fixes

 * Fix `Fields.getValue` not throwing an exception when the map does not contain the key
 * Fix generic type error in `orderByAsc` and `orderByDesc` methods
 * Fix value cannot be null error

### Dependencies

 * Bump logback from 1.5.17 to 1.5.18
 * Bump guava from 33.4.0-jre to 33.4.8-jre
 * Bump spring from 6.2.3 to 6.2.7
 * Bump spring-boot from 3.4.3 to 3.5.0
 * Bump gradle from 8.9 to 8.14.1

### Features

 * Support expression language
 * Support SQL with annotations
 * Support SQLite database
 * Add `orderByAsc` and `orderByDesc` methods for various column types
 * Refactor field retrieval to include superclass fields in `Fields.kt`

## 1.0.9

### Bug Fixes

 * Fix Kotlin List generic type issue where List<out T> is not List<T>

### Dependencies

 * Bump gradle/actions from 4.3.1 to 4.4.0

### Features

 * Support SQL column aliasing
 * Support SQL function
 * Support for SQL annotations

## 1.0.8

### Bug Fixes

 * Fix empty WHERE clause handling in AbstractSqlProvider

### Dependencies

### Features

 * Refactor KiteConfig to split into multiple configuration classes for simplified configuration logic
 * Enhance getFields to include superclass fields and exclude serialVersionUID

## 1.0.7

### Bug Fixes

 * Fix SQL duration logging to ensure execution time is logged when enabled
 * Fix `KiteProperties` cannot apply properties to `KiteConfig` error
 * Fix empty WHERE clause handling in AbstractSqlProvider

### Dependencies

### Features

 * Support SQL duration logging
 * Refactor KiteConfig usage to eliminate INSTANCE references
 * Enhance logging in transaction management and SQL execution
 * Refactor data source implementation to use unpooled connections and introduce pooled data source classes
 * Refactor data source properties handling and enhance field value setting logic
 * Refactor SQL provider methods to improve readability and consistency
 * Add upload artifact to Sonatype Central task
 * Refactor KiteConfig to split into multiple configuration classes for simplified configuration logic
 * Enhance getFields to include superclass fields and exclude serialVersionUID

## 1.0.6

### Bug Fixes

* Fix `class com.tang.kite.session.defaults.DefaultSqlSession cannot access a member of class com.tang.kite.session.entity.AccountOneToMany with modifiers "private"`

### Dependencies

* Bump snakeyaml from 2.3 to 2.4
* Bump slf4j from 2.0.16 to 2.0.17
* Bump logback from 1.5.16 to 1.5.17
* Bump spring from 6.2.2 to 6.2.3
* Bump spring-boot from 3.4.1 to 3.4.3

### Features

* Support one-to-one association query
* Support one-to-many association query
* Support h2 database
* Add SQL log enabled property

## 1.0.5

### Bug Fixes

 * Fix syntax error caused by `order by` having aliases

### Dependencies

 * Bump gradle/actions from 4.2.2 to 4.3.0

### Features

 * Support left join for select query
 * Support right join and inner join for select query

## 1.0.4

### Bug Fixes

 * Fix This annotation is not applicable to target 'interface'. Applicable targets: type usage error
 * Fix expected value not correct error
 * Fix syntax error caused by `order by` having aliases

### Dependencies

 * Bump logback from 1.5.12 to 1.5.16
 * Bump guava from 33.3.1-jre to 33.4.0-jre
 * Bump spring from 6.1.14 to 6.2.2
 * Bump spring-boot from 3.3.5 to 3.4.1

### Features

 * Support spring managed transaction
 * Refactor `Id` annotation `autoIncrement` to `type`'
 * Support `ignore` property in `Column` annotation
 * Refactor `SqlProvider.paginate` method
 * Support left join for select query
 * Support right join and inner join for select query

## 1.0.3

### Features

 * Support nested where condition
 * Support more comparison operators
 * Support return generated key for insert statement
 * Support group by for where condition
 * Support order by for where condition
 * Support having for where condition

## 1.0.2

### Bug Fixes

 * Fix lateinit property updateSetWrapper and updateWhereWrapper has not been initialized error

### Dependencies

 * Bump snakeyaml from 2.2 to 2.3
 * Bump slf4j from 2.0.13 to 2.0.16
 * Bump logback from 1.5.6 to 1.5.12
 * Bump guava from 33.3.0-jre to 33.3.1-jre
 * Bump spring from 6.1.11 to 6.1.14
 * Bump spring-boot from 3.3.2 to 3.3.5

### Features

 * Add order by for select query
 * Add `UpdateWrapper` for update statement
 * Add `QueryWrapper` for a select statement
 * When the `QueryWrapper`'s column is empty, it will select all columns
 * Support for `select distinct`

## 1.0.1

### Bug Fixes

 * Fix selective strategy invalid error

## 1.0.0

### Bug Fixes

 * Fix get(...) must not be null error
 * Fix paginate class cast exception error
 * Fix delete method only delete by primary key
 * Fix `getBean` hash code mismatch error

### Features

 * Support function overloading
 * Support select condition query
 * Support count query
 * Support count condition query
 * Support pagination query
 * Support spring framework
 * Support spring boot
 * Prevent sql injection
 * Add `Column` annotation
 * Add overloading method for `update` method to update column by multiple condition
 * Log sql info
 * Add id generator strategy for `@Id` field
 * Add batch insert
 * Support get `pageNumber` and `pageSize` from `HttpServletRequest`
 * Support get `pageNumber` and `pageSize` from `RequestContextHolder`
 * Add banner
 * Support for configuration using spring boot
 * Add `Pairs` class to simplify `Pair` operation in Kotlin
 * Add `OrderItem` for paginate order by
 * Add `OrderItem` for paginate order by with generics
 * Add property reference in `OrderItem`
