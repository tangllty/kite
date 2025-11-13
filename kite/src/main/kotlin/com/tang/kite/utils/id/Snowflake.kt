package com.tang.kite.utils.id

/**
 * @author Tang
 */
class Snowflake {

    private val dataCenterId: Long

    private val machineId: Long

    private var sequence: Long = 0L

    private var lastTimestamp: Long = -1L

    companion object {

        private const val EPOCH = 1480166465631L


        private const val START_TIMESTAMP = 1480166465631L

        private const val SEQUENCE_BIT = 12L

        private const val MACHINE_BIT = 5L

        private const val DATA_CENTER_BIT = 5L

        private const val MAX_SEQUENCE = (-1L shl SEQUENCE_BIT.toInt()).inv()

        private const val MAX_MACHINE_NUM = (-1L shl MACHINE_BIT.toInt()).inv()

        private const val MAX_DATA_CENTER_NUM = (-1L shl DATA_CENTER_BIT.toInt()).inv()

        private const val MACHINE_LEFT = SEQUENCE_BIT

        private const val DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT

        private const val TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT

    }

    constructor(dataCenterId: Long, machineId: Long) {
        require(!(dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0)) { "dataCenterId must range from 0 to $MAX_DATA_CENTER_NUM" }
        require(!(machineId > MAX_MACHINE_NUM || machineId < 0)) { "machineId must range from 0 to $MAX_MACHINE_NUM" }
        this.dataCenterId = dataCenterId
        this.machineId = machineId
    }

    private fun getTimestamp(): Long {
        return System.currentTimeMillis()
    }

    private fun getNextTimestamp(): Long {
        var timestamp = getTimestamp()
        while (timestamp <= lastTimestamp) {
            timestamp = getTimestamp()
        }
        return timestamp
    }

    @Synchronized
    fun nextId(): Long {
        var currentTimestamp = getTimestamp()
        require(currentTimestamp >= lastTimestamp) { "Clock moved backwards. Refusing to generate id" }
        if (currentTimestamp == lastTimestamp) {
            sequence = sequence + 1 and MAX_SEQUENCE
            if (sequence == 0L) {
                currentTimestamp = getNextTimestamp()
            }
        } else {
            sequence = 0
        }
        lastTimestamp = currentTimestamp
        return (currentTimestamp - EPOCH shl TIMESTAMP_LEFT.toInt()
            or (dataCenterId shl DATA_CENTER_LEFT.toInt())
            or (machineId shl MACHINE_LEFT.toInt())
            or sequence)
    }

}
