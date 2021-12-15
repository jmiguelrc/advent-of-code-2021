fun main() {
    data class Point(val x: Int, val y: Int)
    data class Fold(val isAlongX: Boolean, val coord: Int)

    fun Point.foldX(xCoordinate: Int) = Point(
        if (this.x <= xCoordinate) this.x else (2 * xCoordinate - this.x),
        this.y
    )

    fun Point.foldY(yCoordinate: Int) = Point(
        this.x,
        if (this.y <= yCoordinate) this.y else (2 * yCoordinate - this.y)
    )

    fun Point.fold(fold: Fold): Point = if (fold.isAlongX) this.foldX(fold.coord) else this.foldY(fold.coord)

    fun parsePoints(input: List<String>) = input
        .filterNot { it.isEmpty() || it.contains("fold") }
        .map { it.split(",") }
        .map { Point(it[0].toInt(), it[1].toInt()) }
        .toSet()

    fun parseFoldInstructions(input: List<String>) = input
        .filter { it.contains("fold") }
        .map { it.removePrefix("fold along ") }
        .map {
            if (it.contains("x")) {
                Fold(true, it.removePrefix("x=").toInt())
            } else {
                Fold(false, it.removePrefix("y=").toInt())
            }
        }

    fun part1(input: List<String>): Int {
        val points = parsePoints(input)
        val foldInstructions = parseFoldInstructions(input)

        return points
            .map { it.fold(foldInstructions[0]) }
            .toSet()
            .size
    }

    fun part2(input: List<String>): Int {
        fun printPoints(points: Set<Point>) {
            val maxX = points.maxOf { it.x }
            val maxY = points.maxOf { it.y }

            for (y in 0..maxY) {
                println()
                for (x in 0..maxX) {
                    if (Point(x, y) in points) {
                        print("#")
                    } else {
                        print(" ")
                    }
                }
            }
        }

        var points = parsePoints(input)
        val foldInstructions = parseFoldInstructions(input)

        foldInstructions.forEach { foldInst -> points = points.map { it.fold(foldInst) }.toSet() }

        printPoints(points)

        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput), 17)

    val input = readInput("Day13")
    println(part1(input))
    part2(input)
}
