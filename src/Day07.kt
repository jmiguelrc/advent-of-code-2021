import kotlin.math.abs

fun main() {
    fun computeMinFuelCost(horizontalPositions: List<Int>, fuelCostFunction: (Int, Int) -> Long): Long {
        val minPos = horizontalPositions.minOrNull()!!
        val maxPos = horizontalPositions.maxOrNull()!!

        return (minPos..maxPos)
            .minOf { currentPos -> horizontalPositions.sumOf { fuelCostFunction(it, currentPos) } }
    }

    fun part1(input: List<String>): Long {
        val horizontalPositions = input[0].split(",").map { it.toInt() }
        return computeMinFuelCost(horizontalPositions) { startPos, currentAdjustPos -> abs(startPos - currentAdjustPos).toLong() }
    }

    fun part2(input: List<String>): Long {
        val horizontalPositions = input[0].split(",").map { it.toInt() }
        return computeMinFuelCost(horizontalPositions) { startPos, currentAdjustPos ->
            (1..(abs(startPos - currentAdjustPos)))
                .sum()
                .toLong()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput), 37)

    val input = readInput("Day07")
    println(part1(input))

    check(part2(testInput), 168)
    println(part2(input))
}
