## 1.0.2

### Bug Fixes

 * Fix lateinit property updateSetWrapper and updateWhereWrapper has not been initialized error

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
