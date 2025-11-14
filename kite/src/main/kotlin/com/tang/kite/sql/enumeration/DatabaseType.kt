package com.tang.kite.sql.enumeration

/**
 * Database type
 *
 * @author Tang
 */
enum class DatabaseType(

    val url: String,

    val description: String

) {

    POSTGRESQL("postgresql", "PostgreSQL"),

    MYSQL("mysql", "MySQL"),

    SQLITE("sqlite", "SQLite"),

    DERBY("derby", "Derby"),

    H2("h2", "H2");

    companion object {

        fun of(url: String) = entries.firstOrNull { it.url == url }

    }

}
