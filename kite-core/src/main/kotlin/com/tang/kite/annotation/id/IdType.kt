package com.tang.kite.annotation.id

import com.tang.kite.annotation.id.strategy.IdStrategy

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
