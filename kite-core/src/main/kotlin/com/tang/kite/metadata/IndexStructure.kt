package com.tang.kite.metadata

/**
 * Index physical data structure
 * Represents the underlying algorithm/storage type of the database index
 *
 * @author Tang
 */
enum class IndexStructure {

    /**
     * Statistic index
     */
    STATISTIC,

    /**
     * Clustered index
     */
    CLUSTERED,

    /**
     * Hash index
     */
    HASH,

    /**
     * Standard B-tree index
     */
    BTREE,

    /**
     * Other unknown index type
     */
    OTHER;

    companion object {

        @JvmStatic
        fun getIndexStructure(typeCode: Short): IndexStructure {
            return when (typeCode) {
                0.toShort() -> STATISTIC
                1.toShort() -> CLUSTERED
                2.toShort() -> HASH
                3.toShort() -> BTREE
                else -> OTHER
            }
        }

    }

}
