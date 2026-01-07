package ${config.packageName}.${entity.packagePath}

import com.tang.kite.annotation.Table
import com.tang.kite.annotation.Column
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import java.io.Serializable
<#list table.getImportList() as import>
import ${import}
</#list>

/**
<#if table.comment?? && table.comment != "">
 * ${table.comment} entity
</#if>
<#if table.comment?? && table.comment != "" && config.author??>
 *
</#if>
<#if config.author??>
 * @author ${config.author}
</#if>
 */
@Table("${table.tableName}")
class ${table.className} : Serializable {

<#if entity.withSerialVersionUID>
    companion object {

        @java.io.Serial
        private const val serialVersionUID = ${table.serialVersionUID?c}L

    }
</#if>

<#list table.columns as column>
    <#if column.primaryKey>
    @Id(type = IdType.AUTO)
    </#if>
    @Column("${column.columnName}")
    var ${column.propertyName}: ${column.targetType.className}<#if !column.nullable && column.defaultValue??> = ${column.defaultValue}</#if><#if column.nullable || !column.defaultValue??>? = null</#if>

</#list>
}
