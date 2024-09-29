package com.tang.jkorm.utils

/**
 * @author Tang
 */
class Pairs {

    companion object {

        /**
         * Pair
         *
         * @param first First
         * @param second Second
         * @return [Pair]
         */
        @JvmStatic
        fun <A, B> of(first: A, second: B): Pair<A, B> {
            return Pair(first, second)
        }

    }

}
