package com.tang.kite.generator.utils.serialversion

import kotlin.test.Test

/**
 * @author Tang
 */
class SerialVersionGeneratorTest {

    @Test
    fun testGenerateSerialVersion() {
        val accountFields = listOf(
            FieldInfo("id", "Long"),
            FieldInfo("username", "String"),
            FieldInfo("password", "String"),
            FieldInfo("createTime", "java.time.LocalDateTime")
        )
        val newAccountFields = accountFields + listOf(FieldInfo("updateTime", "java.time.LocalDateTime"))

        val serialVersion = SerialVersionGenerator.generate("Account", accountFields)
        val newSerialVersion = SerialVersionGenerator.generate("Account", newAccountFields)
        assert(serialVersion == -8953747229619661760L)
        assert(serialVersion != newSerialVersion)
    }

}
