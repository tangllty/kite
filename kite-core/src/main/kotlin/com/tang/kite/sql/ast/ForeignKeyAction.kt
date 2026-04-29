package com.tang.kite.sql.ast

/**
 * Foreign key referential actions for ON DELETE and ON UPDATE clauses
 * Defines the behavior of child records when a parent record is deleted or updated
 *
 * @author Tang
 */
enum class ForeignKeyAction {

    /**
     * Cascade the delete or update operation to child records
     * When parent is deleted/updated, child records are automatically deleted/updated
     */
    CASCADE,

    /**
     * Set the foreign key column to NULL when parent record is deleted/updated
     * Requires foreign key column to be nullable
     */
    SET_NULL,

    /**
     * Set the foreign key column to its default value when parent record is deleted/updated
     * Requires a default value to be defined for the foreign key column
     */
    SET_DEFAULT,

    /**
     * Prevent deletion/update of parent record if child records exist
     * Rejects the operation immediately to maintain referential integrity
     */
    RESTRICT,

    /**
     * Default action: similar to RESTRICT, but check is performed at the end of the statement
     * No explicit action is taken; database will reject the operation if child records exist
     */
    NO_ACTION

}
