package com.tang.kite.metadata

/**
 * Index physical data structure
 * Represents the underlying algorithm/storage type of the database index
 *
 * @author Tang
 */
enum class IndexStructure {

    /**
     * Standard B-tree index
     */
    BTREE,

    /**
     * Hash index
     */
    HASH,

    /**
     * Clustered index
     */
    CLUSTERED,

    /**
     * Other unknown index type
     */
    OTHER

}
