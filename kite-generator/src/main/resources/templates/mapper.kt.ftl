package ${config.packageName}.${mapper.packagePath}

import com.tang.kite.mapper.BaseMapper
import ${config.packageName}.${entity.packagePath}.${table.className}

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
interface ${table.className}Mapper : BaseMapper<${table.className}> {
}
