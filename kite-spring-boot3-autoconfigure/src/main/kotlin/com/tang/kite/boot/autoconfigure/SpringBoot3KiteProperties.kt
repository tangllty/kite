package com.tang.kite.boot.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = KiteProperties.KITE_PREFIX)
class SpringBoot3KiteProperties : KiteProperties()
