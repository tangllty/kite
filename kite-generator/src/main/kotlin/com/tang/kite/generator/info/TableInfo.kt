package com.tang.kite.generator.info

/**
 * Table information
 *
 * @author Tang
 */
data class TableInfo(

    val tableName: String,

    val comment: String,

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
