package ${config.packageName}.${mapper.packagePath}

import com.tang.kite.mapper.BaseMapper
import ${config.packageName}.${entity.packagePath}.${table.className}

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
interface ${table.className}Mapper : BaseMapper<${table.className}> {
}
