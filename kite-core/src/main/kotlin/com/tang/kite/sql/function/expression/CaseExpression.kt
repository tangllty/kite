package com.tang.kite.sql.function.expression

import com.tang.kite.config.SqlConfig.getSql
import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
import com.tang.kite.sql.function.ColumnArg
import com.tang.kite.sql.function.FunctionRender
import com.tang.kite.sql.function.LiteralArg
import com.tang.kite.sql.function.SqlKeyword
import kotlin.reflect.KProperty1

/**
 * Case expression
 *
 * @author Tang
 */
class CaseExpression() : FunctionRender {

    private var baseColumn: ColumnArg? = null
    private val whenClauses = mutableListOf<Pair<FunctionRender, FunctionRender>>()
    private var elseClause: FunctionRender? = null

    constructor(column: Column) : this() {
        this.baseColumn = ColumnArg(column)
    }

    constructor(column: KProperty1<*, *>) : this(Column(column))

    constructor(column: SFunction<*, *>) : this(Column(column))

    fun `when`(value: Any, result: Any): CaseExpression {
        whenClauses.add(Pair(LiteralArg(value), LiteralArg(result)))
        return this
    }

    fun `when`(column: Column, result: Any): CaseExpression {
        whenClauses.add(Pair(ColumnArg(column), LiteralArg(result)))
        return this
    }

    fun `when`(column: KProperty1<*, *>, result: Any): CaseExpression {
        return `when`(Column(column), result)
    }

    fun `when`(column: SFunction<*, *>, result: Any): CaseExpression {
        return `when`(Column(column), result)
    }

    fun `when`(condition: FunctionRender, result: Any): CaseExpression {
        whenClauses.add(Pair(condition, LiteralArg(result)))
        return this
    }

    fun `else`(value: Any): CaseExpression {
        this.elseClause = LiteralArg(value)
        return this
    }

    fun `else`(column: Column): CaseExpression {
        this.elseClause = ColumnArg(column)
        return this
    }

    fun `else`(column: KProperty1<*, *>): CaseExpression {
        return `else`(Column(column))
    }

    fun `else`(column: SFunction<*, *>): CaseExpression {
        return `else`(Column(column))
    }

    override fun render(): String {
        val sb = StringBuilder(getSql(SqlKeyword.CASE))

        if (baseColumn != null) {
            sb.append(" ").append(baseColumn?.render())
        }

        for ((condition, result) in whenClauses) {
            sb.append(getSql(" ${SqlKeyword.WHEN} ")).append(condition.render())
              .append(getSql(" ${SqlKeyword.THEN} ")).append(result.render())
        }

        if (elseClause != null) {
            sb.append(getSql(" ${SqlKeyword.ELSE} ")).append(elseClause?.render())
        }

        sb.append(getSql(" ${SqlKeyword.END}"))
        return sb.toString()
    }

}
