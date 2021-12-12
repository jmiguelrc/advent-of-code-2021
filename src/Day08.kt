import java.lang.Math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it.split(" | ")[1] }
            .flatMap { it.split(" ") }
            .count {
                it.length in listOf(2 /* 1 */, 4 /* 4 */, 3 /* 7*/, 7 /* 8*/)
            }
    }

    fun part2(input: List<String>): Int {
        fun decodeNumber(origLine: String): Int {
            val digitsRepresentation = origLine.split(" | ")[0].split(" ").map { it.toSet() }
            val digitToSignalMap = mutableMapOf<Int, Set<Char>>()

            digitToSignalMap[1] = digitsRepresentation.first { it.size == 2 }
            digitToSignalMap[4] = digitsRepresentation.first { it.size == 4 }
            digitToSignalMap[7] = digitsRepresentation.first { it.size == 3 }
            digitToSignalMap[8] = digitsRepresentation.first { it.size == 7 }

            val digitsWith5Segments = digitsRepresentation.filter { it.size == 5 }
            val digitsWith6Segments = digitsRepresentation.filter { it.size == 6 }

            digitToSignalMap[6] = digitsWith6Segments.first { (digitToSignalMap[7]!! - it).size == 1 }
            digitToSignalMap[9] = digitsWith6Segments.first { (digitToSignalMap[4]!! - it).isEmpty() }
            digitToSignalMap[0] = digitsWith6Segments.first { it !in digitToSignalMap.values }
            digitToSignalMap[5] = digitsWith5Segments.first { (digitToSignalMap[6]!! - it).size == 1 }
            digitToSignalMap[3] = digitsWith5Segments.first { (digitToSignalMap[5]!! - it).size == 1 }
            digitToSignalMap[2] = digitsWith5Segments.first { (digitToSignalMap[5]!! - it).size == 2 }

            val signalToDigit = digitToSignalMap.map { Pair(it.value, it.key) }.toMap()
            return origLine.split(" | ")[1].split(" ")
                .map { signalToDigit[it.toSet()]!! }
                .mapIndexed { index, digitValue -> pow(10.0, 3 - index.toDouble()).toInt() * digitValue }
                .sum()
        }
        return input.sumOf { decodeNumber(it) }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput), 26)

    val input = readInput("Day08")
    println(part1(input))

    check(part2(testInput), 61229)
    println(part2(input))
}
