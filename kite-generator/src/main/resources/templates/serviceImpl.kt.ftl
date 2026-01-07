package ${config.packageName}.${service.packagePath}.impl

import com.tang.kite.spring.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import ${config.packageName}.${entity.packagePath}.${table.className}
import ${config.packageName}.${mapper.packagePath}.${table.className}Mapper
import ${config.packageName}.${service.packagePath}.${table.className}Service

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
@Service
class ${table.className}ServiceImpl : ServiceImpl<${table.className}Mapper, ${table.className}>(), ${table.className}Service {
}
