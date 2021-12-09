fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        for (i in 1..input.lastIndex) {
            if (input[i].toInt() > input[i - 1].toInt()) count++
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        var previousSum: Int = input[0].toInt() + input[1].toInt() + input[2].toInt()
        for (i in 3..input.lastIndex) {
            val currentSum = input[i].toInt() + previousSum - (input[i - 3].toInt())
            if (currentSum > previousSum) count++
            previousSum = currentSum
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput), 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
