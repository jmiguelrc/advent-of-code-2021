fun main() {
    data class Position(val x: Int, val y: Int)
    data class Segment(val initPos: Position, val finalPos: Position) {
        val occupiedPositions: Set<Position>

        init {
            if (initPos.x == finalPos.x) {
                occupiedPositions = nonOrderedRange(initPos.y, finalPos.y).map { Position(initPos.x, it) }.toSet()
            } else if (initPos.y == finalPos.y) {
                occupiedPositions = nonOrderedRange(initPos.x, finalPos.x).map { Position(it, initPos.y) }.toSet()
            } else {
                occupiedPositions = nonOrderedRange(initPos.x, finalPos.x).zip(nonOrderedRange(initPos.y, finalPos.y))
                    .map { Position(it.first, it.second) }.toSet()
            }
        }

        private fun nonOrderedRange(v1: Int, v2: Int) = if (v1 > v2) v1 downTo v2 else v1..v2
    }

    fun part1(input: List<String>): Int {
        val segments = input
            .map {
                val positionsStr = it.split(" -> ")
                val startPosition = positionsStr[0].split(",").map { it.toInt() }
                val endPosition = positionsStr[1].split(",").map { it.toInt() }
                listOf(Position(startPosition[0], startPosition[1]), Position(endPosition[0], endPosition[1]))
            }
            .filter { it[0].x == it[1].x || it[0].y == it[1].y }
            .map { Segment(it[0], it[1]) }

        val positionsOccupied = mutableMapOf<Position, Int>()
        segments
            .flatMap { it.occupiedPositions }
            .forEach { positionsOccupied[it] = positionsOccupied[it]?.plus(1) ?: 1 }

        return positionsOccupied.count { it.value > 1 }
    }

    fun part2(input: List<String>): Int {
        val segments = input
            .map {
                val positionsStr = it.split(" -> ")
                val startPosition = positionsStr[0].split(",").map { it.toInt() }
                val endPosition = positionsStr[1].split(",").map { it.toInt() }
                listOf(Position(startPosition[0], startPosition[1]), Position(endPosition[0], endPosition[1]))
            }
            .map { Segment(it[0], it[1]) }

        val positionsOccupied = mutableMapOf<Position, Int>()
        segments
            .flatMap { it.occupiedPositions }
            .forEach { positionsOccupied[it] = positionsOccupied[it]?.plus(1) ?: 1 }

        return positionsOccupied.count { it.value > 1 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput), 5)

    val input = readInput("Day05")
    println(part1(input))

    check(part2(testInput), 12)
    println(part2(input))
}
