package com.tang.kite.generator.utils.serialversion

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.lang.reflect.Modifier
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Utility class for generating serialVersionUID
 * Calculates value based on class name and field information using SHA-1 hashing algorithm
 *
 * @author Tang
 */
object SerialVersionGenerator {

    /**
     * Generates serialVersionUID for a class
     *
     * @param className Simple name of the class (e.g., "User")
     * @param fields List of field information objects
     * @return Generated serialVersionUID
     * @throws NoSuchAlgorithmException If SHA algorithm is unavailable (theoretically unlikely)
     */
    fun generate(className: String, fields: List<FieldInfo>): Long {
        // 1. Prepare field and method information (sorted for consistency)
        val sortedFields = fields.sortedBy { it.name }
        val fieldEntries = sortedFields.map { createFieldEntry(it) }
        val methodEntries = generateDefaultMethods(sortedFields).sorted()

        // 2. Write class structure information to byte stream
        val byteData = ByteArrayOutputStream().use { byteArrayOutputStream ->
            DataOutputStream(byteArrayOutputStream).use { dataOutputStream ->
                dataOutputStream.writeUTF(className)
                dataOutputStream.writeInt(Modifier.PUBLIC) // Class uses public modifier by default

                // Write field information
                fieldEntries.forEach { writeFieldEntry(dataOutputStream, it) }

                // Write method information
                methodEntries.forEach { writeMethodEntry(dataOutputStream, it) }
            }
            byteArrayOutputStream.toByteArray()
        }

        // 3. Calculate hash and convert to Long
        return computeHashAsLong(byteData)
    }

    /**
     * Creates field entry string in format: "modifier:type:name"
     */
    private fun createFieldEntry(field: FieldInfo) =
        "${Modifier.PRIVATE}:${field.type}:${field.name}"

    /**
     * Writes field information to data output stream
     */
    private fun writeFieldEntry(dataOutputStream: DataOutputStream, entry: String) {
        val parts = entry.split(":", limit = 3)
        dataOutputStream.writeInt(parts[0].toInt())
        dataOutputStream.writeUTF(parts[1])
        dataOutputStream.writeUTF(parts[2])
    }

    /**
     * Generates default getter and setter method entries
     */
    private fun generateDefaultMethods(fields: List<FieldInfo>): List<String> {
        return fields.flatMap { field ->
            val capitalizedName = field.name.replaceFirstChar { it.uppercaseChar() }
            listOf(
                // Getter method: public modifier:returnType:methodName()
                "${Modifier.PUBLIC}:${field.type}:get$capitalizedName()",
                // Setter method: public modifier:void:methodName(paramType)
                "${Modifier.PUBLIC}:void:set$capitalizedName(${field.type})"
            )
        }
    }

    /**
     * Writes method information to data output stream
     */
    private fun writeMethodEntry(dataOutputStream: DataOutputStream, entry: String) {
        val parts = entry.split(":", limit = 3)
        dataOutputStream.writeInt(parts[0].toInt())
        dataOutputStream.writeUTF(parts[1])
        dataOutputStream.writeUTF(parts[2])
    }

    /**
     * Computes SHA-1 hash of byte data and converts to Long type
     */
    private fun computeHashAsLong(data: ByteArray): Long {
        val md = MessageDigest.getInstance("SHA")
        val hash = md.digest(data)

        var result = 0L
        for (i in 0 until 8) {
            result = (result shl 8) or (hash[i].toInt() and 0xFF).toLong()
        }
        return result
    }

}
