package com.tang.kite.wrapper.where

import com.tang.kite.wrapper.Wrapper

/**
 * Where wrapper builder
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
