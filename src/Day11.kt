fun main() {
    data class Point(val x: Int, val y: Int)

    fun Point.adjacents() = listOf(
        Point(x, y + 1),
        Point(x, y - 1),
        Point(x + 1, y + 1),
        Point(x - 1, y + 1),
        Point(x + 1, y - 1),
        Point(x - 1, y - 1),
        Point(x + 1, y),
        Point(x - 1, y),
    )

    data class Grid(val grid: MutableMap<Point, Int>)

    fun Grid.incrementPoint(p: Point, alreadyFlashed: MutableSet<Point>) {
        if (p !in grid.keys || p in alreadyFlashed) {
            return
        }
        val currentValue = grid[p]!!
        if (currentValue != 9) {
            grid[p] = currentValue + 1
        } else {
            grid[p] = 0
            alreadyFlashed.add(p)
            p.adjacents().forEach { incrementPoint(it, alreadyFlashed) }
        }
    }

    fun Grid.step(): Int {
        val alreadyFlashed = mutableSetOf<Point>()
        grid.keys.forEach { incrementPoint(it, alreadyFlashed) }
        return alreadyFlashed.size
    }

    fun createGrid(input: List<String>): Grid {
        val gridMap = input
            .flatMapIndexed { rowIdx, row ->
                row.mapIndexed { colIdx, ch -> Point(rowIdx, colIdx) to ch.digitToInt() }
            }
            .toMap()
            .toMutableMap()

        return Grid(gridMap)
    }

    fun part1(input: List<String>): Int {
        val grid = createGrid(input)
        return (1..100).sumOf { grid.step() }
    }

    fun part2(input: List<String>): Int {
        val grid = createGrid(input)
        var i = 1
        while (grid.step() != 100) {
            i++
        }
        return i
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput), 1656)

    val input = readInput("Day11")
    println(part1(input))

    check(part2(testInput), 195)
    println(part2(input))
}
