package ${config.packageName}.${controller.packagePath}

import com.tang.kite.paginate.Page
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ${config.packageName}.${entity.packagePath}.${table.className}
import ${config.packageName}.${service.packagePath}.${table.className}Service
<#if controller.superClass??>
import ${controller.superClass.qualifiedName}
</#if>

/**
 * REST API controller for ${table.className} entity operations
<#if table.comment?? && table.comment != "">
 * ${table.comment}
</#if>
<#if config.author??>
 *
 * @author ${config.author}
</#if>
 */
@RestController
@RequestMapping("/${table.mappingName}")
class ${table.className}Controller(private val ${table.variableName}Service: ${table.className}Service)<#if controller.superClass??> : ${controller.superClass.simpleName}()</#if> {

    @GetMapping("/list")
    fun list(${table.variableName}: ${table.className}): ResponseEntity<List<${table.className}>> {
        return ResponseEntity.ok(${table.variableName}Service.select(${table.variableName}))
    }

    @GetMapping("/page")
    fun page(request: HttpServletRequest, ${table.variableName}: ${table.className}): ResponseEntity<Page<${table.className}>> {
        return ResponseEntity.ok(${table.variableName}Service.paginate(request, ${table.variableName}))
    }

    @GetMapping("/get/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<${table.className}> {
        return ResponseEntity.ok(${table.variableName}Service.selectById(id))
    }

    @PostMapping
    fun save(@RequestBody ${table.variableName}: ${table.className}): ResponseEntity<Int> {
        return ResponseEntity.ok(${table.variableName}Service.insertSelective(${table.variableName}))
    }

    @PutMapping
    fun update(@RequestBody ${table.variableName}: ${table.className}): ResponseEntity<Int> {
        return ResponseEntity.ok(${table.variableName}Service.updateSelective(${table.variableName}))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Int> {
        return ResponseEntity.ok(${table.variableName}Service.deleteById(id))
    }

}
