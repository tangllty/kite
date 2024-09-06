package com.tang.jkorm.spring.registrar

import com.tang.jkorm.logging.LOGGER
import com.tang.jkorm.mapper.BaseMapper
import com.tang.jkorm.spring.annotation.MapperScan
import com.tang.jkorm.spring.beans.MapperFactoryBean
import com.tang.jkorm.spring.constants.BeanNames
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.annotation.AnnotationAttributes
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

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val name = MapperScan::class.java.name
        val attributes = importingClassMetadata.getAnnotationAttributes(name)
        val fromMap = AnnotationAttributes.fromMap(attributes) ?: return
        val basePackages = fromMap.getStringArray("basePackages")
        val beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerRegistrarPostProcessor::class.java)
        beanDefinition.addConstructorArgValue(basePackages)
        registry.registerBeanDefinition(MapperScannerRegistrarPostProcessor::class.java.canonicalName, beanDefinition.beanDefinition)
    }

    class MapperScannerRegistrarPostProcessor(

        val basePackages: Array<String>

    ) : BeanDefinitionRegistryPostProcessor {

        private val metadataReaderFactory = CachingMetadataReaderFactory()

        override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
            basePackages.forEach { basePackage ->
                val resourcePath = ClassUtils.convertClassNameToResourcePath(basePackage)
                val locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resourcePath + "/**/*.class"
                val resources = PathMatchingResourcePatternResolver().getResources(locationPattern)
                resources.forEach { resource ->
                    val metadataReader = metadataReaderFactory.getMetadataReader(resource)
                    val className = metadataReader.classMetadata.className
                    val clazz = Class.forName(className)
                    if (BaseMapper::class.java.isAssignableFrom(clazz)) {
                        registerBeanDefinition(registry, className)
                    }
                }
            }
        }

        private fun registerBeanDefinition(registry: BeanDefinitionRegistry, className: String) {
            val builder = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean::class.java)
            builder.addConstructorArgValue(className)
            builder.addConstructorArgReference(BeanNames.SQL_SESSION_FACTORY)
            registry.registerBeanDefinition(className, builder.beanDefinition)
            LOGGER.debug("Registered mapper bean definition: $className")
        }

    }

}
