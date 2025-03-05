## 1.0.5

### Bug Fixes

 * Fix `class com.tang.kite.session.defaults.DefaultSqlSession cannot access a member of class com.tang.kite.session.entity.AccountOneToMany with modifiers "private"`

### Dependencies

### Features

 * Support one-to-one association query
 * Support one-to-many association query
 * Support h2 database
 * Add SQL log enabled property

## 1.0.4

### Bug Fixes

 * Fix This annotation is not applicable to target 'interface'. Applicable targets: type usage error
 * Fix expected value not correct error
 * Fix syntax error caused by `order by` having aliases

### Dependencies

 * Bump logback from 1.5.12 to 1.5.16
 * Bump guava from 33.3.1-jre to 33.4.0-jre
 * Bump spring from 6.1.14 to 6.2.2
 * Bump springBoot from 3.3.5 to 3.4.1

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
 * Bump springBoot from 3.3.2 to 3.3.5

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
