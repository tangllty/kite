package com.tang.kite.sql.ast

import com.tang.kite.metadata.ColumnMeta
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.datatype.DataType
import com.tang.kite.sql.dialect.PostgresqlDialect
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Tang
 */
class DdlTest {

    private val dialect = PostgresqlDialect()

    @Test
    fun testCreateTable() {
        val createTable = SqlNode.CreateTable(
            table = TableReference("account"),
            columns = mutableListOf(
                ColumnMeta(
                    columnName = "id",
                    typeName = DataType.INT,
                    nullable = false,
                    primaryKey = true,
                    autoIncrement = true,
                    columnSize = 19
                ),
                ColumnMeta(
                    columnName = "username",
                    typeName = DataType.VARCHAR,
                    nullable = false,
                    columnSize = 50
                ),
                ColumnMeta(
                    columnName = "email",
                    typeName = DataType.VARCHAR,
                    columnSize = 100
                ),
                ColumnMeta(
                    columnName = "created_at",
                    typeName = DataType.TIMESTAMP,
                    nullable = false,
                    columnSize = 0,
                    defaultValue = "current_timestamp"
                )
            ),
            constraints = mutableListOf(
                TableConstraint.Unique(
                    name = "uk_username",
                    columns = listOf("username")
                )
            )
        )

        val sql = createTable.getFirstSql(dialect)
        val expected = "create table account (id int not null generated always as identity primary key, username varchar(50) not null, email varchar(100), created_at timestamp not null default current_timestamp, constraint uk_username unique (username))"
        assertEquals(expected, sql)
    }

    @Test
    fun testCreateTableWithAllConstraints() {
        val createTable = SqlNode.CreateTable(
            table = TableReference("orders"),
            columns = mutableListOf(
                ColumnMeta(
                    columnName = "id",
                    typeName = DataType.INT,
                    nullable = false,
                    primaryKey = true,
                    autoIncrement = true,
                    columnSize = 19
                ),
                ColumnMeta(
                    columnName = "user_id",
                    typeName = DataType.INT,
                    nullable = false,
                    columnSize = 19
                ),
                ColumnMeta(
                    columnName = "amount",
                    typeName = DataType.DECIMAL,
                    nullable = false,
                    columnSize = 10,
                    decimalDigits = 2
                )
            ),
            constraints = mutableListOf(
                TableConstraint.PrimaryKey(
                    name = "pk_orders",
                    columns = listOf("id")
                ),
                TableConstraint.ForeignKey(
                    name = "fk_orders_account",
                    columns = listOf("user_id"),
                    referencedTable = "account",
                    referencedColumns = listOf("id")
                )
            )
        )

        val sql = createTable.getFirstSql(dialect)
        val expected = "create table orders (id int not null generated always as identity primary key, user_id int not null, amount decimal(10, 2) not null, constraint pk_orders primary key (id), constraint fk_orders_account foreign key (user_id) references account (id))"
        assertEquals(expected, sql)
    }

    @Test
    fun testAlterTableAddColumn() {
        val alterTable = SqlNode.AlterTable(
            table = TableReference("account"),
            operations = mutableListOf(
                AlterOperation.AddColumn(
                    column = ColumnMeta(
                        columnName = "phone",
                        typeName = DataType.VARCHAR,
                        columnSize = 20,
                        nullable = true
                    )
                )
            )
        )

        val sql = alterTable.getFirstSql(dialect)
        val expected = "alter table account add column phone varchar(20)"
        assertEquals(expected, sql)
    }

    @Test
    fun testAlterTableDropColumn() {
        val alterTable = SqlNode.AlterTable(
            table = TableReference("account"),
            operations = mutableListOf(
                AlterOperation.DropColumn(
                    columnName = "phone"
                )
            )
        )

        val sql = alterTable.getFirstSql(dialect)
        val expected = "alter table account drop column phone"
        assertEquals(expected, sql)
    }

    @Test
    fun testAlterTableModifyColumn() {
        val alterTable = SqlNode.AlterTable(
            table = TableReference("account"),
            operations = mutableListOf(
                AlterOperation.ModifyColumn(
                    column = ColumnMeta(
                        columnName = "email",
                        typeName = DataType.VARCHAR,
                        columnSize = 150,
                        nullable = false
                    )
                )
            )
        )

        val sql = alterTable.getFirstSql(dialect)
        val expected = "alter table account modify email varchar(150) not null"
        assertEquals(expected, sql)
    }

    @Test
    fun testAlterTableMultipleOperations() {
        val alterTable = SqlNode.AlterTable(
            table = TableReference("account"),
            operations = mutableListOf(
                AlterOperation.AddColumn(
                    column = ColumnMeta(
                        columnName = "phone",
                        typeName = DataType.VARCHAR,
                        columnSize = 20,
                        nullable = true
                    )
                ),
                AlterOperation.ModifyColumn(
                    column = ColumnMeta(
                        columnName = "email",
                        typeName = DataType.VARCHAR,
                        columnSize = 150,
                        nullable = false
                    )
                ),
                AlterOperation.DropColumn(
                    columnName = "old_column"
                )
            )
        )

        val sqlList = alterTable.getSqlList(dialect)
        val expected1 = "alter table account add column phone varchar(20)"
        assertEquals(expected1, sqlList[0])
        val expected2 = "alter table account modify email varchar(150) not null"
        assertEquals(expected2, sqlList[1])
        val expected3 = "alter table account drop column old_column"
        assertEquals(expected3, sqlList[2])
    }

    @Test
    fun testDropTable() {
        val dropTable = SqlNode.DropTable(
            table = TableReference("account"),
            cascade = false
        )

        val sql = dropTable.getFirstSql(dialect)
        val expected = "drop table account"
        assertEquals(expected, sql)
    }

    @Test
    fun testDropTableWithCascade() {
        val dropTable = SqlNode.DropTable(
            table = TableReference("account"),
            cascade = true
        )

        val sql = dropTable.getFirstSql(dialect)
        val expected = "drop table account cascade"
        assertEquals(expected, sql)
    }

    @Test
    fun testCreateIndex() {
        val createIndex = SqlNode.CreateIndex(
            indexName = "idx_account_email",
            table = TableReference("account"),
            columns = listOf("email")
        )

        val sql = createIndex.getFirstSql(dialect)
        val expected = "create index idx_account_email on account (email)"
        assertEquals(expected, sql)
    }

    @Test
    fun testCreateUniqueIndex() {
        val createIndex = SqlNode.CreateIndex(
            indexName = "uk_account_username",
            table = TableReference("account"),
            columns = listOf("username"),
            unique = true
        )

        val sql = createIndex.getFirstSql(dialect)
        val expected = "create unique index uk_account_username on account (username)"
        assertEquals(expected, sql)
    }

    @Test
    fun testCreateCompositeIndex() {
        val createIndex = SqlNode.CreateIndex(
            indexName = "idx_account_name_email",
            table = TableReference("account"),
            columns = listOf("last_name", "first_name", "email")
        )

        val sql = createIndex.getFirstSql(dialect)
        val expected = "create index idx_account_name_email on account (last_name, first_name, email)"
        assertEquals(expected, sql)
    }

    @Test
    fun testDropIndex() {
        val dropIndex = SqlNode.DropIndex(
            indexName = "idx_account_email",
            table = TableReference("account")
        )

        val sql = dropIndex.getFirstSql(dialect)
        val expected = "drop index idx_account_email"
        assertEquals(expected, sql)
    }

    @Test
    fun testDropIndexWithoutTable() {
        val dropIndex = SqlNode.DropIndex(
            indexName = "idx_account_email",
            table = null
        )

        val sql = dropIndex.getFirstSql(dialect)
        val expected = "drop index idx_account_email"
        assertEquals(expected, sql)
    }

    @Test
    fun testTruncateTable() {
        val truncateTable = SqlNode.TruncateTable(
            table = TableReference("account"),
            cascade = false
        )

        val sql = truncateTable.getFirstSql(dialect)
        val expected = "truncate table account"
        assertEquals(expected, sql)
    }

    @Test
    fun testTruncateTableWithCascade() {
        val truncateTable = SqlNode.TruncateTable(
            table = TableReference("account"),
            cascade = true
        )

        val sql = truncateTable.getFirstSql(dialect)
        val expected = "truncate table account cascade"
        assertEquals(expected, sql)
    }

    @Test
    fun testColumnDefinitionWithAllProperties() {
        val column = ColumnMeta(
            columnName = "price",
            typeName = DataType.DECIMAL,
            nullable = false,
            defaultValue = "0.00",
            autoIncrement = false,
            primaryKey = false,
            unique = false,
            comment = "Product price",
            columnSize = 10,
            decimalDigits = 2
        )

        val createTable = SqlNode.CreateTable(
            table = TableReference("products"),
            columns = mutableListOf(column),
            constraints = mutableListOf()
        )

        val sql = createTable.getFirstSql(dialect)
        val expected = "create table products (price decimal(10, 2) not null default 0.00)"
        assertEquals(expected, sql)
    }

    @Test
    fun testTableConstraintPrimaryKey() {
        val constraint = TableConstraint.PrimaryKey(
            name = "pk_account",
            columns = listOf("id", "tenant_id")
        )

        val createTable = SqlNode.CreateTable(
            table = TableReference("account"),
            columns = mutableListOf(
                ColumnMeta(
                    columnName = "id",
                    typeName = DataType.INT,
                    nullable = false
                ),
                ColumnMeta(
                    columnName = "tenant_id",
                    typeName = DataType.INT,
                    nullable = false
                )
            ),
            constraints = mutableListOf(constraint)
        )

        val sql = createTable.getFirstSql(dialect)
        val expected = "create table account (id int not null, tenant_id int not null, constraint pk_account primary key (id, tenant_id))"
        assertEquals(expected, sql)
    }

    @Test
    fun testTableConstraintForeignKey() {
        val constraint = TableConstraint.ForeignKey(
            name = "fk_orders_customers",
            columns = listOf("customer_id"),
            referencedTable = "customers",
            referencedColumns = listOf("id")
        )

        val createTable = SqlNode.CreateTable(
            table = TableReference("orders"),
            columns = mutableListOf(
                ColumnMeta(
                    columnName = "id",
                    typeName = DataType.INT,
                    nullable = false,
                    primaryKey = true
                ),
                ColumnMeta(
                    columnName = "customer_id",
                    typeName = DataType.INT,
                    nullable = false
                )
            ),
            constraints = mutableListOf(constraint)
        )

        val sql = createTable.getFirstSql(dialect)
        val expected = "create table orders (id int not null primary key, customer_id int not null, constraint fk_orders_customers foreign key (customer_id) references customers (id))"
        assertEquals(expected, sql)
    }

    @Test
    fun testCreateTableWithComments() {
        val createTable = SqlNode.CreateTable(
            table = TableReference("products"),
            columns = mutableListOf(
                ColumnMeta(
                    columnName = "id",
                    typeName = DataType.INT,
                    nullable = false,
                    primaryKey = true,
                    autoIncrement = true,
                    comment = "产品ID"
                ),
                ColumnMeta(
                    columnName = "name",
                    typeName = DataType.VARCHAR,
                    columnSize = 100,
                    nullable = false,
                    comment = "产品名称"
                ),
                ColumnMeta(
                    columnName = "price",
                    typeName = DataType.DECIMAL,
                    columnSize = 10,
                    decimalDigits = 2,
                    nullable = false,
                    comment = "产品价格"
                )
            ),
            constraints = mutableListOf()
        )

        val sqlList = createTable.getSqlList(dialect)
        assertEquals(4, sqlList.size) // 创建表 + 3个注释语句

        val expectedTableSql = "create table products (id int not null generated always as identity primary key, name varchar(100) not null, price decimal(10, 2) not null)"
        assertEquals(expectedTableSql, sqlList[0])

        val expectedComment1 = "comment on column products.id is '产品ID'"
        assertEquals(expectedComment1, sqlList[1])

        val expectedComment2 = "comment on column products.name is '产品名称'"
        assertEquals(expectedComment2, sqlList[2])

        val expectedComment3 = "comment on column products.price is '产品价格'"
        assertEquals(expectedComment3, sqlList[3])
    }

}
