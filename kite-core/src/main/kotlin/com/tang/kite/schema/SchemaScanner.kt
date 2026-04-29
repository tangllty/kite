package com.tang.kite.schema

import com.tang.kite.logging.getLogger
import com.tang.kite.utils.Reflects
import java.io.File
import kotlin.reflect.KClass

/**
 * Schema scanner for automatic database schema management based on entity classes
 *
 * @author Tang
 */
object SchemaScanner {

    private val logger = getLogger

    private const val CLASS_SUFFIX = ".class"

    private const val PACKAGE_SEPARATOR = '.'

    private const val PATH_SEPARATOR = '/'

    /**
     * Scan entity classes from the given package patterns
     * Supports wildcards: * (single package), ** (multiple packages) and flexible wildcards with * anywhere in pattern part
     *
     * @param scanPackages package patterns to scan
     * @return set of entity classes
     */
    @JvmStatic
    fun scanEntityClasses(scanPackages: Set<String>): Set<KClass<*>> {
        val entityClasses = mutableSetOf<KClass<*>>()
        scanPackages.forEach { packagePattern ->
            try {
                val matchedPackages = findMatchedPackages(packagePattern)
                matchedPackages.forEach { realPackage ->
                    val classNames = findClassesInPackage(realPackage)
                    classNames.forEach { className ->
                        loadAndCheckEntity(className, entityClasses)
                    }
                }
            } catch (e: Exception) {
                logger.error("Failed to scan package pattern: $packagePattern", e)
            }
        }
        return entityClasses
    }

    /**
     * Find all real packages that match the wildcard pattern
     * Pure JDK implementation, no third-party dependencies
     */
    private fun findMatchedPackages(packagePattern: String): Set<String> {
        val matchedPackages = mutableSetOf<String>()
        val rootPath = getRootPathWithoutWildcard(packagePattern)
        val classLoader = Thread.currentThread().contextClassLoader
        try {
            val resources = classLoader.getResources(rootPath)
            while (resources.hasMoreElements()) {
                val resource = resources.nextElement()
                if (resource.protocol == "file") {
                    val rootDir = File(resource.file)
                    if (rootDir.exists()) {
                        val currentPackage = rootPath.replace(PATH_SEPARATOR, PACKAGE_SEPARATOR)
                        scanDirAndMatchPackage(rootDir, currentPackage, packagePattern, matchedPackages)
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to resolve matched packages for pattern: $packagePattern", e)
        }
        return matchedPackages
    }

    /**
     * Recursively scan directories and match package names against the wildcard pattern
     */
    private fun scanDirAndMatchPackage(dir: File, currentPackage: String, pattern: String, matchedPackages: MutableSet<String>) {
        if (matchWildcard(currentPackage, pattern)) {
            matchedPackages.add(currentPackage)
        }
        val files = dir.listFiles() ?: return
        files.forEach { file ->
            if (file.isDirectory) {
                val subPackage = if (currentPackage.isEmpty()) {
                    file.name
                } else {
                    "$currentPackage${PACKAGE_SEPARATOR}${file.name}"
                }
                scanDirAndMatchPackage(file, subPackage, pattern, matchedPackages)
            }
        }
    }

    /**
     * Wildcard matching engine
     * Supports:
     * - * (match one package part)
     * - ** (match multiple package parts)
     * - Flexible wildcards with * anywhere in pattern part
     */
    private fun matchWildcard(packageName: String, pattern: String): Boolean {
        val patternParts = pattern.split(PACKAGE_SEPARATOR)
        val packageParts = packageName.split(PACKAGE_SEPARATOR)

        var pIdx = 0
        var pkgIdx = 0
        var starIdx = -1
        var pkgStarIdx = 0
        while (pkgIdx < packageParts.size) {
            if (pIdx < patternParts.size) {
                val patternPart = patternParts[pIdx]
                val packagePart = packageParts[pkgIdx]
                when {
                    patternPart == packagePart -> {
                        pIdx++
                        pkgIdx++
                    }
                    patternPart == "*" -> {
                        pIdx++
                        pkgIdx++
                    }
                    patternPart == "**" -> {
                        starIdx = pIdx
                        pkgStarIdx = pkgIdx
                        pIdx++
                    }
                    patternPart.contains("*") -> {
                        val regexPattern = patternPart
                            .replace(".", "\\.")
                            .replace("*", ".*")

                        if (packagePart.matches(Regex(regexPattern))) {
                            pIdx++
                            pkgIdx++
                        } else {
                            if (starIdx != -1) {
                                pIdx = starIdx + 1
                                pkgStarIdx++
                                pkgIdx = pkgStarIdx
                            } else {
                                return false
                            }
                        }
                    }
                    starIdx != -1 -> {
                        pIdx = starIdx + 1
                        pkgStarIdx++
                        pkgIdx = pkgStarIdx
                    }
                    else -> return false
                }
            } else if (starIdx != -1) {
                pIdx = starIdx + 1
                pkgStarIdx++
                pkgIdx = pkgStarIdx
            } else {
                return false
            }
        }
        while (pIdx < patternParts.size && patternParts[pIdx] == "**") {
            pIdx++
        }
        return pIdx == patternParts.size
    }

    /**
     * Get the root path before the first wildcard character
     */
    private fun getRootPathWithoutWildcard(pattern: String): String {
        val wildcardIndex = pattern.indexOfAny(charArrayOf('*'))
        if (wildcardIndex == -1) {
            return pattern.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR)
        }
        val lastSeparatorIndex = pattern.lastIndexOf(PACKAGE_SEPARATOR, wildcardIndex)
        val rootPackage = if (lastSeparatorIndex == -1) {
            pattern.substring(0, wildcardIndex)
        } else {
            pattern.substring(0, lastSeparatorIndex)
        }
        return rootPackage.removeSuffix(".").replace(PACKAGE_SEPARATOR, PATH_SEPARATOR)
    }

    /**
     * Load class and check if it is an annotated entity
     */
    private fun loadAndCheckEntity(className: String, entityClasses: MutableSet<KClass<*>>) {
        try {
            val clazz = Class.forName(className)
            val kotlinClass = clazz.kotlin
            if (Reflects.isEntity(kotlinClass)) {
                entityClasses.add(kotlinClass)
            }
        } catch (e: Exception) {
            logger.error("Failed to load or check class: $className", e)
        }
    }

    /**
     * Find all class names in the specified package
     */
    private fun findClassesInPackage(packageName: String): Set<String> {
        val classes = mutableSetOf<String>()
        val packagePath = packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR)
        val classLoader = Thread.currentThread().contextClassLoader

        runCatching {
            val resources = classLoader.getResources(packagePath)
            while (resources.hasMoreElements()) {
                val resource = resources.nextElement()
                if (resource.protocol == "file") {
                    val directory = File(resource.file)
                    if (directory.exists()) {
                        findClassesInDirectory(directory, packageName, classes)
                    }
                }
            }
        }.onFailure {
            logger.error("Failed to find classes in package: $packageName", it)
        }

        return classes
    }

    /**
     * Recursively find .class files in directory
     */
    private fun findClassesInDirectory(directory: File, packageName: String, classes: MutableSet<String>) {
        val files = directory.listFiles() ?: return
        files.forEach { file ->
            if (file.isDirectory.not() && file.name.endsWith(CLASS_SUFFIX)) {
                val className = file.name.removeSuffix(CLASS_SUFFIX)
                classes.add("$packageName${PACKAGE_SEPARATOR}$className")
            }
        }
    }

}
