package com.tang.kite.generator.info

import com.tang.kite.metadata.TableMeta

/**
 * Table information
 *
 * @author Tang
 */
data class TableInfo(

    val tableMeta: TableMeta,

    val className: String? = null,

    val variableName: String? = null,

    val mappingName: String? = null,

    val serialVersionUID: Long? = null,

    val columns: List<ColumnInfo> = emptyList()

) {

    fun getImportList(): List<String> {
        return columns.filter {
            it.targetType.packageName != null
        }.map {
            it.targetType.getFullName()
        }.distinct()
            .sortedBy { it }
    }

}
