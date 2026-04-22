package com.tang.kite.sql.statement

import com.tang.kite.constants.SqlString.LEFT_BRACKET
import com.tang.kite.constants.SqlString.RIGHT_BRACKET
import com.tang.kite.constants.SqlString.SPACE
import com.tang.kite.sql.SqlNode
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.enumeration.ComparisonOperator
import java.lang.StringBuilder

/**
 * @author Tang
 */
class SubqueryStatement(

    val sqlNode: SqlNode.Select,

    val comparisonOperator: ComparisonOperator,

    val dialect: SqlDialect

) {

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        val sqlStatement = sqlNode.getSqlStatement(dialect)
        sql.append("$LEFT_BRACKET$SPACE${sqlStatement.sql}$SPACE$RIGHT_BRACKET")
        parameters.addAll(sqlStatement.parameters)
    }

}
