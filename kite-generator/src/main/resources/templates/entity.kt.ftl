package ${config.packageName}.${entity.packagePath}

import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import java.io.Serializable
<#list table.getImportList() as import>
import ${import}
</#list>

/**
 * Entity corresponding to database table ${table.tableName}
<#if table.comment?? && table.comment != "">
 * ${table.comment}
</#if>
<#if config.author??>
 *
 * @author ${config.author}
</#if>
 */
class ${table.className} : Serializable {

<#if entity.withSerialVersionUID>
    companion object {

        @java.io.Serial
        private const val serialVersionUID = ${table.serialVersionUID?c}L

    }
</#if>

<#list table.columns as column>
    <#if column.comment?? && column.comment != "">
    /**
     * ${column.comment}
     */
    </#if>
    <#if column.primaryKey>
    @Id(type = IdType.AUTO)
    </#if>
    var ${column.propertyName}: ${column.targetType.className}<#if !column.nullable && column.defaultValue??> = ${column.defaultValue}</#if><#if column.nullable || !column.defaultValue??>? = null</#if>

</#list>
}
