fun main() {
    data class Position(val x: Int, val y: Int)
    data class Board(val heightMap: Map<Position, Int>)

    fun Position.adjacents() = listOf(
        Position(this.x, this.y + 1),
        Position(this.x, this.y - 1),
        Position(this.x + 1, this.y),
        Position(this.x - 1, this.y),
    )

    fun Board.isMinimum(position: Position) = position.adjacents()
        .filter { it in this.heightMap.keys }
        .all { this.heightMap[position]!! < this.heightMap[it]!! }

    fun Board.findMinimums(): List<Position> = heightMap.keys.filter { isMinimum(it) }

    fun Board.findBasin(currentPoint: Position, alreadyInBasin: Set<Position> = setOf()): Set<Position> {
        var inBasin: Set<Position> = alreadyInBasin.toMutableSet().apply { add(currentPoint) }
        currentPoint.adjacents()
            .filter { it !in inBasin && it in this.heightMap.keys && this.heightMap[it] != 9 }
            .forEach { inBasin = findBasin(it, inBasin) }
        return inBasin
    }

    fun createBoard(input: List<String>): Board {
        val boardMap = input
            .flatMapIndexed { rowIdx, s ->
                s.toCharArray()
                    .mapIndexed { colIdx, ch -> Pair(Position(rowIdx, colIdx), ch.digitToInt()) }
            }
            .toMap()

        return Board(boardMap)
    }

    fun part1(input: List<String>): Int {
        val board = createBoard(input)
        return board.findMinimums()
            .sumOf { 1 + board.heightMap[it]!! }
    }

    fun part2(input: List<String>): Int {
        val board = createBoard(input)
        return board.findMinimums()
            .map { board.findBasin(it).size }
            .sorted()
            .takeLast(3)
            .reduceRight { i, acc -> i * acc }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput), 15)

    val input = readInput("Day09")
    println(part1(input))

    check(part2(testInput), 1134)
    println(part2(input))
}
