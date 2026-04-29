package com.tang.kite.metadata

/**
 * @author Tang
 */
enum class IndexType {

    /**
     * Statistics row (not a real index)
     */
    STATISTIC,

    /**
     * Clustered index
     */
    CLUSTERED,

    /**
     * Hashed index
     */
    HASHED,

    /**
     * Other index (B-tree, etc.)
     */
    OTHER

}
