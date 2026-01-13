package ${config.packageName}.${controller.packagePath};

import com.tang.kite.paginate.Page;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ${config.packageName}.${entity.packagePath}.${table.className};
import ${config.packageName}.${service.packagePath}.${table.className}Service;
<#if controller.superClass??>
import ${controller.superClass.qualifiedName};
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
public class ${table.className}Controller<#if controller.superClass??> extends ${controller.superClass.simpleName}</#if> {

    private final ${table.className}Service ${table.variableName}Service;

    public ${table.className}Controller(${table.className}Service ${table.variableName}Service) {
        this.${table.variableName}Service = ${table.variableName}Service;
    }

    @GetMapping("/list")
    public ResponseEntity<List<${table.className}>> list(${table.className} ${table.variableName}) {
        return ResponseEntity.ok(${table.variableName}Service.select(${table.variableName}));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<${table.className}>> page(HttpServletRequest request, ${table.className} ${table.variableName}) {
        return ResponseEntity.ok(${table.variableName}Service.paginate(request, ${table.variableName}));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<${table.className}> get(@PathVariable Long id) {
        return ResponseEntity.ok(${table.variableName}Service.selectById(id));
    }

    @PostMapping
    public ResponseEntity<Integer> save(@RequestBody ${table.className} ${table.variableName}) {
        return ResponseEntity.ok(${table.variableName}Service.insertSelective(${table.variableName}));
    }

    @PutMapping
    public ResponseEntity<Integer> update(@RequestBody ${table.className} ${table.variableName}) {
        return ResponseEntity.ok(${table.variableName}Service.updateSelective(${table.variableName}));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) {
        return ResponseEntity.ok(${table.variableName}Service.deleteById(id));
    }

}
