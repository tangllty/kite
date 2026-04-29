package com.tang.kite.schema

import com.tang.kite.schema.scanner.OneAClass
import com.tang.kite.schema.scanner.OneBClass
import com.tang.kite.schema.scanner.entity.OneEntityClass
import com.tang.kite.schema.scanner.twenty.TwentyAClass
import com.tang.kite.schema.scanner.twenty.TwentyBClass
import com.tang.kite.schema.scanner.twenty.entity.TwentyEntityClass
import com.tang.kite.schema.scanner.two.TwoAClass
import com.tang.kite.schema.scanner.two.TwoBClass
import com.tang.kite.schema.scanner.two.entity.TwoEntityClass
import com.tang.kite.schema.scanner.two.three.ThreeAClass
import com.tang.kite.schema.scanner.two.three.ThreeBClass
import com.tang.kite.schema.scanner.two.three.entity.ThreeEntityClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class SchemaScannerTest {

    @Test
    fun shouldScanExactPackage() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner"))
        assertTrue { entityClasses.contains(OneAClass::class) }
        assertTrue { entityClasses.contains(OneBClass::class) }
        assertEquals(2, entityClasses.size)
    }

    @Test
    fun shouldScanSingleLevelEntityPackages() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.*.entity"))
        assertTrue { entityClasses.contains(TwoEntityClass::class) }
        assertTrue { entityClasses.contains(TwentyEntityClass::class) }
        assertEquals(2, entityClasses.size)
    }

    @Test
    fun shouldScanRecursiveEntityPackages() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.**.entity"))
        assertTrue { entityClasses.contains(OneEntityClass::class) }
        assertTrue { entityClasses.contains(TwoEntityClass::class) }
        assertTrue { entityClasses.contains(ThreeEntityClass::class) }
        assertTrue { entityClasses.contains(TwentyEntityClass::class) }
        assertEquals(4, entityClasses.size)
    }

    @Test
    fun shouldScanMultiplePackagePatterns() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf(
            "com.tang.kite.schema.scanner",
            "com.tang.kite.schema.scanner.**.entity"
        ))
        assertTrue { entityClasses.contains(OneAClass::class) }
        assertTrue { entityClasses.contains(OneBClass::class) }
        assertTrue { entityClasses.contains(OneEntityClass::class) }
        assertTrue { entityClasses.contains(TwoEntityClass::class) }
        assertTrue { entityClasses.contains(ThreeEntityClass::class) }
        assertTrue { entityClasses.contains(TwentyEntityClass::class) }
        assertEquals(6, entityClasses.size)
    }

    @Test
    fun shouldScanSpecificSubPackage() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.two"))
        assertTrue { entityClasses.contains(TwoAClass::class) }
        assertTrue { entityClasses.contains(TwoBClass::class) }
        assertEquals(2, entityClasses.size)
    }

    @Test
    fun shouldScanDeepSubPackage() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.two.three"))
        assertTrue { entityClasses.contains(ThreeAClass::class) }
        assertTrue { entityClasses.contains(ThreeBClass::class) }
        assertEquals(2, entityClasses.size)
    }

    @Test
    fun shouldScanPackageWithSingleWildcard() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.two.*"))
        assertTrue { entityClasses.contains(ThreeAClass::class) }
        assertTrue { entityClasses.contains(ThreeBClass::class) }
        assertTrue { entityClasses.contains(TwoEntityClass::class) }
        assertEquals(3, entityClasses.size)
    }

    @Test
    fun shouldHandleEmptyPackage() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf(""))
        assertEquals(0, entityClasses.size)
    }

    @Test
    fun shouldHandleNonExistentPackage() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.four"))
        assertEquals(0, entityClasses.size)
    }

    @Test
    fun shouldHandleInvalidPattern() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("invalid..pattern"))
        assertEquals(0, entityClasses.size)
    }

    @Test
    fun shouldScanPackageWithRecursiveWildcard() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.two.**"))
        assertTrue { entityClasses.contains(TwoAClass::class) }
        assertTrue { entityClasses.contains(TwoBClass::class) }
        assertTrue { entityClasses.contains(TwoEntityClass::class) }
        assertTrue { entityClasses.contains(ThreeAClass::class) }
        assertTrue { entityClasses.contains(ThreeBClass::class) }
        assertTrue { entityClasses.contains(ThreeEntityClass::class) }
        assertEquals(6, entityClasses.size)
    }

    @Test
    fun shouldScanRootPackageWithRecursiveWildcard() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.**"))
        assertTrue { entityClasses.contains(OneAClass::class) }
        assertTrue { entityClasses.contains(OneBClass::class) }
        assertTrue { entityClasses.contains(OneEntityClass::class) }
        assertTrue { entityClasses.contains(TwoAClass::class) }
        assertTrue { entityClasses.contains(TwoBClass::class) }
        assertTrue { entityClasses.contains(TwoEntityClass::class) }
        assertTrue { entityClasses.contains(ThreeAClass::class) }
        assertTrue { entityClasses.contains(ThreeBClass::class) }
        assertTrue { entityClasses.contains(ThreeEntityClass::class) }
        assertTrue { entityClasses.contains(TwentyAClass::class) }
        assertTrue { entityClasses.contains(TwentyBClass::class) }
        assertTrue { entityClasses.contains(TwentyEntityClass::class) }
        assertEquals(12, entityClasses.size)
    }

    @Test
    fun shouldScanPackagesWithPrefixWildcard() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.t*"))
        assertTrue { entityClasses.contains(TwoAClass::class) }
        assertTrue { entityClasses.contains(TwoBClass::class) }
        assertTrue { entityClasses.contains(TwentyAClass::class) }
        assertTrue { entityClasses.contains(TwentyBClass::class) }
        assertEquals(4, entityClasses.size)
    }

    @Test
    fun shouldScanEntityPackagesWithPrefixWildcard() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.t*.entity"))
        assertTrue { entityClasses.contains(TwoEntityClass::class) }
        assertTrue { entityClasses.contains(TwentyEntityClass::class) }
        assertEquals(2, entityClasses.size)
    }

    @Test
    fun shouldScanPackagesWithFlexibleWildcard() {
        val entityClasses = SchemaScanner.scanEntityClasses(setOf("com.tang.kite.schema.scanner.t*o"))
        assertTrue { entityClasses.contains(TwoAClass::class) }
        assertTrue { entityClasses.contains(TwoBClass::class) }
        assertEquals(2, entityClasses.size)
    }

}
