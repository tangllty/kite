package com.tang.jkorm.spring.registrar

import com.tang.jkorm.logging.LOGGER
import com.tang.jkorm.mapper.BaseMapper
import com.tang.jkorm.spring.annotation.MapperScan
import com.tang.jkorm.spring.beans.MapperFactoryBean
import com.tang.jkorm.spring.constants.BeanNames
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.util.ClassUtils

/**
 * Register a MapperScannerRegistrar to scan the mapper interfaces.
 *
 * @author Tang
 */
class MapperScannerRegistrar : ImportBeanDefinitionRegistrar {

    private val metadataReaderFactory = CachingMetadataReaderFactory()

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val name = MapperScan::class.java.name
        val attributes = importingClassMetadata.getAnnotationAttributes(name)
        val fromMap = AnnotationAttributes.fromMap(attributes) ?: return
        val basePackages = fromMap.getStringArray("basePackages")
        basePackages.forEach { basePackage ->
            val resources: Array<Resource> = PathMatchingResourcePatternResolver()
                .getResources((ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class"))
            resources.filter {
                val metadataReader = metadataReaderFactory.getMetadataReader(it)
                val className = metadataReader.classMetadata.className
                val clazz = Class.forName(className)
                clazz.interfaces.any { inter -> BaseMapper::class.java.isAssignableFrom(inter) }
            }.forEach {
                val metadataReader = metadataReaderFactory.getMetadataReader(it)
                val className = metadataReader.classMetadata.className
                val clazz = Class.forName(className)
                val interfaces = clazz.interfaces
                interfaces.filter { inter -> BaseMapper::class.java.isAssignableFrom(inter) }
                    .forEach { _ -> registerBeanDefinition(registry, className, clazz) }
            }
        }
    }

    private fun registerBeanDefinition(registry: BeanDefinitionRegistry, className: String, clazz: Class<*>) {
        val builder = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean::class.java)
        builder.addConstructorArgValue(clazz)
        builder.addConstructorArgReference(BeanNames.SQL_SESSION_FACTORY)
        registry.registerBeanDefinition(className, builder.beanDefinition)
        LOGGER.debug("Registered mapper bean definition: $className")
    }

}
