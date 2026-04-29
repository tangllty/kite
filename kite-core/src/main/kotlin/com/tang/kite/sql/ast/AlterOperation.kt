package com.tang.kite.sql.ast

import com.tang.kite.metadata.ColumnMeta

/**
 * Alter table operations for DDL
 *
 * @author Tang
 */
sealed class AlterOperation {

    data class AddColumn(

        val column: ColumnMeta

    ) : AlterOperation()

    data class DropColumn(

        val columnName: String,

        val cascade: Boolean = false

    ) : AlterOperation()

    data class ModifyColumn(

        val column: ColumnMeta

    ) : AlterOperation()

    data class RenameColumn(

        val oldName: String,

        val newName: String

    ) : AlterOperation()

    data class AddConstraint(

        val constraint: TableConstraint

    ) : AlterOperation()

    data class DropConstraint(

        val constraintName: String,

        val cascade: Boolean = false

    ) : AlterOperation()

}
