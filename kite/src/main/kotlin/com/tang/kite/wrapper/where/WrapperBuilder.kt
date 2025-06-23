package com.tang.kite.wrapper.where

/**
 * Where wrapper builder
 *
 * @author Tang
 */
interface WrapperBuilder<T, W> {

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    fun build(): W

}
