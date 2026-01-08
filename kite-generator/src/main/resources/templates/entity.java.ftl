package ${config.packageName}.${entity.packagePath};

<#if entity.withTableAnnotation>
import com.tang.kite.annotation.Table;
</#if>
<#if entity.withColumnAnnotation>
import com.tang.kite.annotation.Column;
</#if>
import com.tang.kite.annotation.id.Id;
import com.tang.kite.annotation.id.IdType;
import java.io.Serializable;
<#list table.getImportList() as import>
import ${import};
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
<#if entity.withTableAnnotation>
@Table("${table.tableName}")
</#if>
public class ${table.className} implements Serializable {

<#if entity.withSerialVersionUID>
    @java.io.Serial
    private static final long serialVersionUID = ${table.serialVersionUID?c}L;
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
    private ${column.targetType.className}<#if column.nullable || !column.defaultValue??> ${column.propertyName};</#if><#if !column.nullable && column.defaultValue??> ${column.propertyName} = ${column.defaultValue};</#if>

</#list>
<#list table.columns as column>
    public ${column.targetType.className} get${column.propertyName?cap_first}() {
        return ${column.propertyName};
    }

    public void set${column.propertyName?cap_first}(${column.targetType.className} ${column.propertyName}) {
        this.${column.propertyName} = ${column.propertyName};
    }

</#list>
}
