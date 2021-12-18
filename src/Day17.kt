fun main() {
    data class Velocity(val x: Int, val y: Int)

    fun Velocity.step() = Velocity(
        if (x > 0) x - 1 else 0,
        y - 1
    )

    data class Position(val x: Int, val y: Int)
    data class PositionRange(val xRange: IntRange, val yRange: IntRange)

    fun Position.inRange(range: PositionRange) = x in range.xRange && y in range.yRange

    data class Probe(var velocity: Velocity, var currentPos: Position)

    fun Probe.step() {
        this.currentPos = Position(currentPos.x + velocity.x, currentPos.y + velocity.y)
        this.velocity = velocity.step()
    }

    fun parseTargetArea(input: List<String>): PositionRange {
        val coords = input[0].split(": ")[1].split(", ")
        val xRangeStr = coords[0].removePrefix("x=").split("..")
        val targetXRange = xRangeStr[0].toInt()..xRangeStr[1].toInt()

        val yRangeStr = coords[1].removePrefix("y=").split("..")
        val targetYRange = yRangeStr[0].toInt()..yRangeStr[1].toInt()
        return PositionRange(targetXRange, targetYRange)
    }

    fun getAllPossibleProbes(): List<Probe> {
        val minVelToFind = -1000
        val maxVelToFind = 1000

        return (minVelToFind..maxVelToFind)
            .flatMap { xVel -> (minVelToFind..maxVelToFind).map { Velocity(xVel, it) } }
            .map { Probe(it, Position(0, 0)) }
    }

    fun getMaxYPosition(input: List<String>): Int {
        val targetArea = parseTargetArea(input)

        return getAllPossibleProbes()
            .maxOf {
                var maxY = 0
                var reachedTarget = false
                for (i in 0..1000) {
                    it.step()
                    maxY = if (it.currentPos.y > maxY) it.currentPos.y else maxY
                    reachedTarget = reachedTarget || it.currentPos.inRange(targetArea)
                    if (reachedTarget && maxY > it.currentPos.y) break
                }
                if (reachedTarget) maxY else 0
            }
    }

    fun countValidInitialVels(input: List<String>): Int {
        val targetArea = parseTargetArea(input)

        return getAllPossibleProbes()
            .count {
                var reachesTarget = false
                for (i in 0..1000) {
                    it.step()
                    if (it.currentPos.inRange(targetArea)) {
                        reachesTarget = true
                        break
                    }
                }
                reachesTarget
            }
    }

    fun part1(input: List<String>): Int {
        return getMaxYPosition(input)
    }

    fun part2(input: List<String>): Int {
        return countValidInitialVels(input)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput), 45)
    check(part2(testInput), 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
