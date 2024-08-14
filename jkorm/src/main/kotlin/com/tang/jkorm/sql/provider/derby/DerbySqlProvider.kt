package com.tang.jkorm.sql.provider.derby

import com.tang.jkorm.sql.provider.AbstractSqlProvider
import com.tang.jkorm.utils.Reflects

/**
 * Derby SQL provider
 *
 * @author Tang
 */
class DerbySqlProvider : AbstractSqlProvider() {

    override fun <T> paginate(type: Class<T>, entity: Any?, orderBys: Array<Pair<String, Boolean>>, pageNumber: Long, pageSize: Long): String {
        val sql = StringBuilder()
        sql.append("select * from ${Reflects.getTableName(type)}")
        if (entity != null) {
            sql.append(" where ")
            type.declaredFields.forEach {
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
            if (orderBys.isNotEmpty()) {
                sql.append(" order by ")
                orderBys.forEach {
                    sql.append(it.first).append(if (it.second) " asc," else " desc,")
                }
                sql.deleteCharAt(sql.length - 1)
            }
        }
        sql.append(" OFFSET ").append((pageNumber - 1) * pageSize).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY")
        return getSql(sql)
    }

}
