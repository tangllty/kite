package com.tang.kite.wrapper.where

/**
 * Where wrapper builder
 *
 * @author Tang
 */
interface WhereBuilder<T, R, W> {

    /**
     * Build the wrapper
     *
     * @return W
     */
    fun build(): W

    /**
     * Execute the wrapper
     *
     * @return R
     */
    fun execute(): R

}
