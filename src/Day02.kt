fun main() {

    fun part1(input: List<String>): Int {
        data class PositionP1(val horiz: Int, val depth: Int)

        fun PositionP1.forward(value: Int) = PositionP1(this.horiz + value, this.depth)
        fun PositionP1.down(value: Int) = PositionP1(this.horiz, this.depth + value)
        fun PositionP1.up(value: Int) = PositionP1(this.horiz, this.depth - value)
        fun PositionP1.total() = this.horiz * this.depth

        var currentPosition = PositionP1(0, 0)

        input.forEach {
            val instruction = it.split(" ")[0]
            val value = it.split(" ")[1].toInt()
            currentPosition = when (instruction) {
                "forward" -> currentPosition.forward(value)
                "down" -> currentPosition.down(value)
                "up" -> currentPosition.up(value)
                else -> throw IllegalArgumentException("Invaalid instruction $instruction")
            }
        }

        return currentPosition.total()
    }

    fun part2(input: List<String>): Int {
        data class PositionP2(val horiz: Int, val depth: Int, val aim: Int)

        fun PositionP2.forward(value: Int) = PositionP2(this.horiz + value, this.depth + this.aim * value, this.aim)
        fun PositionP2.down(value: Int) = PositionP2(this.horiz, this.depth, this.aim + value)
        fun PositionP2.up(value: Int) = PositionP2(this.horiz, this.depth, this.aim - value)
        fun PositionP2.total() = this.horiz * this.depth

        var currentPosition = PositionP2(0, 0, 0)

        input.forEach {
            val instruction = it.split(" ")[0]
            val value = it.split(" ")[1].toInt()
            currentPosition = when (instruction) {
                "forward" -> currentPosition.forward(value)
                "down" -> currentPosition.down(value)
                "up" -> currentPosition.up(value)
                else -> throw IllegalArgumentException("Invaalid instruction $instruction")
            }
        }

        return currentPosition.total()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput), 150)

    val input = readInput("Day02")
    println(part1(input))

    check(part2(testInput), 900)
    println(part2(input))
}
