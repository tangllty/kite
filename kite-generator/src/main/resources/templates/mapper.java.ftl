package ${config.packageName}.${mapper.packagePath};

<#if !mapper.superClass??>
import com.tang.kite.mapper.BaseMapper;
</#if>
import ${config.packageName}.${entity.packagePath}.${table.className};
<#if mapper.superClass??>
import ${mapper.superClass.qualifiedName};
</#if>

/**
 * Mapper interface for database table ${table.tableName}
<#if table.comment?? && table.comment != "">
 * ${table.comment}
</#if>
<#if config.author??>
 *
 * @author ${config.author}
</#if>
 */
public interface ${table.className}Mapper extends <#if mapper.superClass??>${mapper.superClass.simpleName}<#else>BaseMapper</#if><${table.className}> {
}
