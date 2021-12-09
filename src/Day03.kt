fun main() {
    fun part1(input: List<String>): Int {
        val tempList = (0.until(input[0].length)).map { 0 }.toMutableList()
        input
            .map { it.toCharArray().map { it.digitToInt() } }
            .forEach { toAdd ->
                tempList.forEachIndexed { index, _ ->
                    tempList[index] += toAdd[index]
                }
            }

        val gamma = tempList.map { it > input.size / 2 }
            .map { if (it) "1" else "0" }
            .joinToString(separator = "")
            .toInt(2)

        val epsilon = tempList.map { it > input.size / 2 }
            .map { if (it) "0" else "1" }
            .joinToString(separator = "")
            .toInt(2)
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        fun findValue(input: List<String>, mostCommon: Boolean): String {
            var currentList = input

            for (index in input[0].indices) {
                if (currentList.size == 1) {
                    return currentList[0]
                }
                val numOnes = currentList.count { it[index] == '1' }
                val shouldKeepOnes = (mostCommon && numOnes >= currentList.size / 2.0) ||
                        (!mostCommon && numOnes < currentList.size / 2.0)

                currentList = currentList
                    .filterNot { binNum -> binNum[index] == '1' && !shouldKeepOnes }
                    .filterNot { binNum -> binNum[index] == '0' && shouldKeepOnes }
            }
            return currentList[0]
        }

        val oxygenRating = findValue(input, true).toInt(2)
        val co2Rating = findValue(input, false).toInt(2)
        return oxygenRating * co2Rating
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput), 198)

    val input = readInput("Day03")
    println(part1(input))

    check(part2(testInput), 230)
    println(part2(input))
}
