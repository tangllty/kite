package com.tang.jkorm.sql

import com.tang.jkorm.utils.Reflects

/**
 * @author Tang
 */
object SqlProvider {

    private fun getSql(sql: StringBuilder): String {
        return sql.toString().lowercase()
    }

    fun insert(entity: Any): String {
        val clazz = entity::class.java
        val sql = StringBuilder()
        sql.append("insert into ${Reflects.getTableName(clazz)} (")
        clazz.declaredFields.forEach {
            sql.append(it.name).append(",")
        }
        sql.deleteCharAt(sql.length - 1)
        sql.append(") values (")
        clazz.declaredFields.forEach {
            Reflects.makeAccessible(it, entity)
            if (it.type == String::class.java) {
                sql.append("'").append(it.get(entity)).append("',")
            } else {
                sql.append(it.get(entity)).append(",")
            }
        }
        sql.deleteCharAt(sql.length - 1)
        sql.append(")")
        return getSql(sql)
    }

    private fun selectiveStrategy(any: Any?): Boolean {
        if (any == null) {
            return false
        }
        if (any is String) {
            return any.isNotEmpty()
        }
        if (any is Int) {
            return any != 0
        }
        return true
    }

    private fun getFieldValueMap(entity: Any): Map<String, Any> {
        val clazz = entity::class.java
        val map = mutableMapOf<String, Any>()
        clazz.declaredFields.forEach {
            Reflects.makeAccessible(it, entity)
            map[it.name] = it.get(entity)
        }
        return map
    }

    fun insertSelective(entity: Any): String {
        val clazz = entity::class.java
        val sql = StringBuilder()
        sql.append("insert into ${Reflects.getTableName(clazz)} (")
        val fieldMap = getFieldValueMap(entity)
        fieldMap.forEach {
            if (selectiveStrategy(it.value)) {
                sql.append(it.key).append(",")
            }
        }
        sql.deleteCharAt(sql.length - 1)
        sql.append(") values (")
        fieldMap.forEach {
            if (selectiveStrategy(it.value)) {
                if (it.value is String) {
                    sql.append("'").append(it.value).append("',")
                } else {
                    sql.append(it.value).append(",")
                }
            }
        }
        sql.deleteCharAt(sql.length - 1)
        sql.append(")")
        return getSql(sql)
    }

    fun update(entity: Any): String {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val idField = Reflects.getIdField(clazz)
        sql.append("update ${Reflects.getTableName(clazz)} set ")
        clazz.declaredFields
            .filter { it.name != idField.name }
            .forEach {
                Reflects.makeAccessible(it, entity)
                if (it.type == String::class.java) {
                    sql.append(it.name).append("='").append(it.get(entity)).append("',")
                } else {
                    sql.append(it.name).append("=").append(it.get(entity)).append(",")
                }
        }
        sql.deleteCharAt(sql.length - 1)
        Reflects.makeAccessible(idField, entity)
        sql.append(" where ${idField.name} = ${idField.get(entity)}")
        return getSql(sql)
    }

    fun updateSelective(entity: Any): String {
        val clazz = entity::class.java
        val sql = StringBuilder()
        sql.append("update ${Reflects.getTableName(clazz)} set ")
        val idField = Reflects.getIdField(clazz)
        clazz.declaredFields
            .filter { it.name != idField.name }
            .forEach {
                Reflects.makeAccessible(it, entity)
                if (selectiveStrategy(it.get(entity))) {
                    if (it.type == String::class.java) {
                        sql.append(it.name).append("='").append(it.get(entity)).append("',")
                    } else {
                        sql.append(it.name).append("=").append(it.get(entity)).append(",")
                    }
                }
            }
        sql.deleteCharAt(sql.length - 1)
        Reflects.makeAccessible(idField, entity)
        sql.append(" where ${idField.name} = ${idField.get(entity)}")
        return getSql(sql)
    }

    fun <T> delete(clazz: Class<T>, entity: Any): String {
        val sql = StringBuilder()
        val idField = Reflects.getIdField(clazz)
        Reflects.makeAccessible(idField, entity)
        sql.append("delete from ${Reflects.getTableName(clazz)} where ${idField.name} = ${idField.get(entity)}")
        return getSql(sql)
    }

    fun <T> select(clazz: Class<T>, entity: Any?): String {
        val sql = StringBuilder()
        sql.append("select * from ${Reflects.getTableName(clazz)}")
        if (entity == null) {
            return getSql(sql)
        }
        sql.append(" where ")
        clazz.declaredFields.forEach {
            Reflects.makeAccessible(it, entity)
            val value = it.get(entity)
            if (selectiveStrategy(value)) {
                sql.append(it.name).append("=")
                if (it.type == String::class.java) {
                    sql.append("'").append(value).append("' and ")
                } else {
                    sql.append(value).append(" and ")
                }
            }
        }
        sql.delete(sql.length - 5, sql.length)
        return getSql(sql)
    }

}
