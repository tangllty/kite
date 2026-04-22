package com.tang.kite.wrapper.where

import com.tang.kite.wrapper.Wrapper

/**
 * Interface for building wrapper instances, providing a common build method
 *
 * @author Tang
 */
interface WrapperBuilder<T> {

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    fun build(): Wrapper<T>

}
