import java.util.*

fun main() {
    data class Vertex(val x: Int, val y: Int)

    fun Vertex.connections() = listOf(
        Vertex(x - 1, y),
        Vertex(x + 1, y),
        Vertex(x, y + 1),
        Vertex(x, y - 1),
    )

    fun getMinDistance(board: Map<Vertex, Int>): MutableMap<Vertex, Long> {
        val unvisited = board.keys.toMutableSet()
        val distances = board.keys.associateWith { Long.MAX_VALUE }.toMutableMap()
        distances[Vertex(0, 0)] = 0
        val minDistanceHeap = PriorityQueue(Comparator.comparingLong<Pair<Vertex, Long>> { it.second }).apply {
            add(Vertex(0, 0) to 0)
        }

        while (unvisited.isNotEmpty()) {
            val currentVertex = minDistanceHeap.remove()!!.first

            unvisited.remove(currentVertex)
            currentVertex.connections()
                .filter { it in unvisited }
                .forEach { connectedVertex ->
                    val currentDist = distances[currentVertex]!! + board[connectedVertex]!!
                    if (currentDist < distances[connectedVertex]!!) {
                        distances[connectedVertex] = currentDist
                        minDistanceHeap.add(connectedVertex to currentDist)
                    }
                }
        }

        return distances
    }

    fun parseBoard(input: List<String>) = input
        .flatMapIndexed { rowIdx, row -> row.mapIndexed { colIdx, value -> Vertex(rowIdx, colIdx) to value.digitToInt() } }
        .toMap()

    fun part1(input: List<String>): Long {
        val board = parseBoard(input)

        val endVertex = board.keys.maxByOrNull { it.x + it.y }!!
        val distanceToEnd = getMinDistance(board)[endVertex]!!

        return distanceToEnd
    }

    fun makeBoardPt2(origBoard: Map<Vertex, Int>): Map<Vertex, Int> {
        val maxVertex = origBoard.keys.maxByOrNull { it.x + it.y }!!
        val numRows = maxVertex.x + 1
        val numCols = maxVertex.y + 1

        return (0 until (5 * numRows))
            .flatMap { xVal -> (0 until (5 * numCols)).map { yVal -> Vertex(xVal, yVal) } }
            .associateWith {
                val correspondingVertex = Vertex(
                    it.x % numRows,
                    it.y % numCols
                )
                val toAdd = it.x / numRows + it.y / numCols
                (origBoard[correspondingVertex]!! + toAdd) % 10 + (origBoard[correspondingVertex]!! + toAdd) / 10
            }
    }

    fun part2(input: List<String>): Long {
        val board = makeBoardPt2(parseBoard(input))
        val endVertex = board.keys.maxByOrNull { it.x + it.y }!!

        return getMinDistance(board)[endVertex]!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput), 40)

    val input = readInput("Day15")
    println(part1(input))

    check(part2(testInput), 315)
    println(part2(input))
}
