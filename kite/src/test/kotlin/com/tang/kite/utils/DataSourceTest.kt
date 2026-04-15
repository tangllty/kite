package com.tang.kite.utils

import com.tang.kite.annotation.Table
import com.tang.kite.annotation.datasource.DataSource
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.mapper.BaseMapper

/**
 * @author Tang
 */
@Table("account")
class EntityAccount(

    @Id(type = IdType.AUTO)
    var id: Long? = null,
    var username: String? = null,
    var password: String? = null

)

@DataSource("ds3")
@Table("account")
class DataSourceEntityAccount(

    @Id(type = IdType.AUTO)
    var id: Long? = null,
    var username: String? = null,
    var password: String? = null

)

@DataSource("ds2")
interface DataSourceClassAccountMapper : BaseMapper<EntityAccount>

interface DataSourceMethodAccountMapper : BaseMapper<DataSourceEntityAccount> {

    @DataSource("ds1")
    override fun select(): List<DataSourceEntityAccount>

}
