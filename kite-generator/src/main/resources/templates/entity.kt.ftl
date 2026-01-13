package ${config.packageName}.${entity.packagePath}

<#if entity.withTableAnnotation>
import com.tang.kite.annotation.Table
</#if>
<#if entity.withColumnAnnotation>
import com.tang.kite.annotation.Column
</#if>
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import java.io.Serializable
<#list table.getImportList() as import>
import ${import}
</#list>
<#if entity.superClass??>
import ${entity.superClass.qualifiedName}
</#if>

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
<#if entity.withTableAnnotation>
@Table("${table.tableName}")
</#if>
class ${table.className} : <#if entity.superClass??>${entity.superClass.simpleName}(), </#if>Serializable {

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
    <#if entity.withColumnAnnotation>
    @Column("${column.columnName}")
    </#if>
    var ${column.propertyName}: ${column.targetType.className}<#if !column.nullable && column.defaultValue??> = ${column.defaultValue}</#if><#if column.nullable || !column.defaultValue??>? = null</#if>

</#list>
}
