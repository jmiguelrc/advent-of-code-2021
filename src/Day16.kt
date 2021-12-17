open class Packet(val version: Int, val packetTypeId: Int, val subPackets: List<Packet>, val size: Int) {
    fun totalVersionSum(): Int = this.version + this.subPackets.sumOf { it.totalVersionSum() }

    open fun value(): Long = when (packetTypeId) {
        0 -> subPackets.sumOf { it.value() }
        1 -> subPackets.foldRight(1L) { packet, acc -> acc * packet.value() }
        2 -> subPackets.minOf { it.value() }
        3 -> subPackets.maxOf { it.value() }
        5 -> if (subPackets[0].value() > subPackets[1].value()) 1 else 0
        6 -> if (subPackets[0].value() < subPackets[1].value()) 1 else 0
        7 -> if (subPackets[0].value() == subPackets[1].value()) 1 else 0
        else -> throw IllegalStateException("Unexpected value for packetTypeID: $packetTypeId")
    }

    companion object {
        fun parse(binaryRepresentation: String): Packet {
            val version = binaryRepresentation.substring(0 until 3).toInt(2)
            val packetTypeId = binaryRepresentation.substring(3 until 6).toInt(2)
            if (packetTypeId == 4) {
                return parseDataPacket(binaryRepresentation, version)
            }
            val lengthTypeId = binaryRepresentation[6].digitToInt(2)
            return if (lengthTypeId == 0) {
                parseOperatorPacketLT0(binaryRepresentation, version, packetTypeId)
            } else {
                parseOperatorPacketLT1(binaryRepresentation, version, packetTypeId)
            }
        }

        private fun parseDataPacket(binaryRepresentation: String, version: Int): DataPacket {
            val lastStartGroupIdx = (6..5000 step 5)
                .first { binaryRepresentation[it] == '0' }

            val litValue = (6..lastStartGroupIdx step 5)
                .joinToString("") { binaryRepresentation.substring(it + 1, it + 5) }
                .toLong(2)

            val lastRelevantDigit = lastStartGroupIdx + 5
            return DataPacket(version, litValue, lastRelevantDigit)
        }

        private fun parseOperatorPacketLT1(binaryRepresentation: String, version: Int, packetTypeId: Int): Packet {
            val numSubPackets = binaryRepresentation.substring(7, 7 + 11).toInt(2)
            val subPackets = mutableListOf<Packet>()
            var currentIdx = 18
            repeat(numSubPackets) {
                val subPacket = parse(binaryRepresentation.substring(currentIdx))
                subPackets.add(subPacket)
                currentIdx += subPacket.size
            }
            return Packet(version, packetTypeId, subPackets, currentIdx)
        }

        private fun parseOperatorPacketLT0(binaryRepresentation: String, version: Int, packetTypeId: Int): Packet {
            val totalLengthInBits = binaryRepresentation.substring(7, 7 + 15).toInt(2)
            val subPackets = mutableListOf<Packet>()
            var currentIdx = 22
            while (currentIdx < (totalLengthInBits + 22)) {
                val subPacket = parse(binaryRepresentation.substring(currentIdx))
                subPackets.add(subPacket)
                currentIdx += subPacket.size
            }

            return Packet(version, packetTypeId, subPackets, currentIdx)
        }
    }
}

class DataPacket(version: Int, val literalValue: Long, packetSize: Int) :
    Packet(version, 4, emptyList(), packetSize) {
    override fun value() = literalValue
}

fun main() {
    fun parseInput(input: List<String>): Packet {
        val binaryRepresentation = input[0]
            .map { it.digitToInt(16).toString(2).padStart(4, '0') }
            .joinToString("")
        return Packet.parse(binaryRepresentation)
    }

    fun part1(input: List<String>): Int {
        val packet = parseInput(input)
        return packet.totalVersionSum()
    }

    fun part2(input: List<String>): Long {
        val packet = parseInput(input)
        return packet.value()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput), 20)

    val input = readInput("Day16")
    println(part1(input))

    check(part2(testInput), 1)
    println(part2(input))
}
