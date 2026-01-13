package ${config.packageName}.${service.packagePath};

<#if !service.superClass??>
import com.tang.kite.service.BaseService;
</#if>
import ${config.packageName}.${entity.packagePath}.${table.className};
<#if service.superClass??>
import ${service.superClass.qualifiedName};
</#if>

/**
 * Business service interface for ${table.className} entity
<#if table.comment?? && table.comment != "">
 * ${table.comment}
</#if>
<#if config.author??>
 *
 * @author ${config.author}
</#if>
 */
public interface ${table.className}Service extends <#if service.superClass??>${service.superClass.simpleName}<#else>BaseService</#if><${table.className}> {
}
