package ${config.packageName}.${service.packagePath};

import com.tang.kite.service.BaseService;
import ${config.packageName}.${entity.packagePath}.${table.className};

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
public interface ${table.className}Service extends BaseService<${table.className}> {
}
