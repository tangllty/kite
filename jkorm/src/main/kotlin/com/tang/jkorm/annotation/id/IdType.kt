package com.tang.jkorm.annotation.id

import com.tang.jkorm.annotation.id.strategy.IdStrategy

/**
 * [Id.type] enum
 *
 * @author Tang
 */
enum class IdType {

    /**
     * Auto increment
     */
    AUTO,

    /**
     * Use [IdStrategy] to generate id
     */
    GENERATOR,

    /**
     * Do not generate id automatically
     */
    NONE

}
