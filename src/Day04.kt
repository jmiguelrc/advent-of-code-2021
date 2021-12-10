fun main() {
    data class Position(val row: Int, val col: Int)

    data class BingoBoard(val columns: List<MutableList<Int>>, val rows: List<MutableList<Int>>, val numbersPos: Map<Int, Position>)

    fun BingoBoard.markNumber(num: Int) {
        numbersPos[num]?.let {
            this.columns[it.col].remove(num)
            this.rows[it.row].remove(num)
        }
    }

    fun BingoBoard.hasBingo() = this.columns.any { it.isEmpty() } || this.rows.any { it.isEmpty() }

    fun makeBoard(rows: List<List<Int>>): BingoBoard {
        val cols = mutableListOf<MutableList<Int>>()
        val numPositions = mutableMapOf<Int, Position>()
        for (colIdx in rows[0].indices) {
            cols.add(colIdx, mutableListOf())
            for (rowIdx in rows.indices) {
                val rowVal = rows[rowIdx][colIdx]
                cols[colIdx].add(rowVal)
                numPositions[rowVal] = Position(rowIdx, colIdx)
            }
        }
        return BingoBoard(cols, rows.map { it.toMutableList() }, numPositions)
    }

    fun makeBingoBoards(input: List<String>): List<BingoBoard> {
        val boards = mutableListOf<BingoBoard>()
        val currentRows = mutableListOf<List<Int>>()
        for (i in input.indices) {
            if (input[i].isBlank()) {
                // Make board
                boards.add(makeBoard(currentRows))
                currentRows.clear()
                continue
            }
            currentRows.add(input[i].split(" ").filter { it.isNotBlank() }.map { it.toInt() })
        }
        return boards
    }

    fun part1(input: List<String>): Int {
        fun readDrawnNumbers(seq: String): List<Int> = seq.split(",").map { it.toInt() }.toList()

        val drawnNumbers = readDrawnNumbers(input[0])
        val bingoBoards = makeBingoBoards(input.subList(2, input.lastIndex + 1))

        for (drawnNumber in drawnNumbers) {
            bingoBoards.forEach {
                it.markNumber(drawnNumber)
                if (it.hasBingo()) {
                    return drawnNumber * it.rows.flatten().sum()
                }
            }
        }

        throw IllegalStateException("Should not have gotten here")
    }

    fun part2(input: List<String>): Int {
        fun readDrawnNumbers(seq: String): List<Int> = seq.split(",").map { it.toInt() }.toList()

        val drawnNumbers = readDrawnNumbers(input[0])
        val bingoBoards = makeBingoBoards(input.subList(2, input.lastIndex + 1))
        val wonBoards = mutableSetOf<Map<Int, Position>>()

        for (drawnNumber in drawnNumbers) {
            bingoBoards.forEach {
                it.markNumber(drawnNumber)
                if (it.numbersPos !in wonBoards && it.hasBingo()) {
                    if (bingoBoards.size - 1 == wonBoards.size) {
                        return drawnNumber * it.rows.flatten().sum()
                    }
                    wonBoards.add(it.numbersPos)
                }
            }
        }

        throw IllegalStateException("Should not have gotten here")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput), 4512)

    val input = readInput("Day04")
    println(part1(input))

    check(part2(testInput), 1924)
    println(part2(input))
}
