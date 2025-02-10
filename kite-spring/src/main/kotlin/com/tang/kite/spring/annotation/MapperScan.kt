package com.tang.kite.spring.annotation

import com.tang.kite.spring.registrar.MapperScannerRegistrar
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.AliasFor

/**
 * Register a MapperScannerRegistrar to scan the mapper interfaces.
 *
 * @author Tang
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Import(MapperScannerRegistrar::class)
annotation class MapperScan(

    @get:AliasFor("basePackages")
    val value: Array<String> = [],

    @get:AliasFor("value")
    val basePackages: Array<String> = []

)
