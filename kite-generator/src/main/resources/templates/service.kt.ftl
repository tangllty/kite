package ${config.packageName}.${service.packagePath}

import com.tang.kite.service.BaseService
import ${config.packageName}.${entity.packagePath}.${table.className}

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
interface ${table.className}Service : BaseService<${table.className}> {
}
