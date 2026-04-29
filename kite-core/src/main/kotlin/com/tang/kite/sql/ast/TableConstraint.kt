package com.tang.kite.sql.ast

/**
 * Table constraint for DDL operations
 *
 * @author Tang
 */
sealed class TableConstraint {

    data class PrimaryKey(

        val columns: List<String>,

        val name: String? = null

    ) : TableConstraint()

    data class Unique(

        val columns: List<String>,

        val name: String? = null

    ) : TableConstraint()

    data class ForeignKey(

        val columns: List<String>,

        val referencedTable: String,

        val referencedColumns: List<String>,

        val name: String? = null,

        val onDelete: ForeignKeyAction? = null,

        val onUpdate: ForeignKeyAction? = null

    ) : TableConstraint()

    data class Check(

        val expression: String,

        val name: String? = null

    ) : TableConstraint()

}
