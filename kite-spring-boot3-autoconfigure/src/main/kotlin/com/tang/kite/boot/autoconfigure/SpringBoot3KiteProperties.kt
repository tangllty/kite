package com.tang.kite.boot.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Tang
 */
@ConfigurationProperties(prefix = KiteProperties.KITE_PREFIX)
class SpringBoot3KiteProperties : KiteProperties()
