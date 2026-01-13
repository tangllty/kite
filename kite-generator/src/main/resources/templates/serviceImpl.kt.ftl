package ${config.packageName}.${service.packagePath}.impl

<#if !service.implSuperClass??>
import com.tang.kite.spring.service.impl.ServiceImpl
</#if>
import org.springframework.stereotype.Service
import ${config.packageName}.${entity.packagePath}.${table.className}
import ${config.packageName}.${mapper.packagePath}.${table.className}Mapper
import ${config.packageName}.${service.packagePath}.${table.className}Service
<#if service.implSuperClass??>
import ${service.implSuperClass.qualifiedName}
</#if>

/**
 * Implementation of business service for ${table.className} entity
<#if table.comment?? && table.comment != "">
 * ${table.comment}
</#if>
<#if config.author??>
 *
 * @author ${config.author}
</#if>
 */
@Service
class ${table.className}ServiceImpl : <#if service.implSuperClass??>${service.implSuperClass.simpleName}<#else>ServiceImpl</#if><${table.className}Mapper, ${table.className}>(), ${table.className}Service {
}
