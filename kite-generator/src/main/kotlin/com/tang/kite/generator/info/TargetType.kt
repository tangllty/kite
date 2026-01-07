package com.tang.kite.generator.info

/**
 * @author Tang
 */
class TargetType {

    val packageName: String?

    val className: String

    constructor(className: String) {
        val lastDotIndex = className.lastIndexOf('.')
        if (lastDotIndex > 0) {
            this.className = className.substring(lastDotIndex + 1)
            this.packageName = className.substring(0, lastDotIndex)
        } else {
            this.packageName = null
            this.className = className
        }
    }

    fun getFullName(): String {
        return if (packageName == null) {
            className
        } else {
            "$packageName.$className"
        }
    }

}
