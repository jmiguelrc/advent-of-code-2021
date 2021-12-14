fun main() {
    val closeChunkMapping = mapOf(')' to '(', '}' to '{', ']' to '[', '>' to '<')
    val openChunkMapping = closeChunkMapping.map { entry -> Pair(entry.value, entry.key) }.toMap()
    val closeChunkErrorScore = mapOf(')' to 3, '}' to 1197, ']' to 57, '>' to 25137)
    val closeChunkAutoCompleteScore = mapOf('(' to 1L, '[' to 2L, '{' to 3L, '<' to 4L)
    val openChunk = closeChunkMapping.values.toSet()

    fun getChunkSyntaxScore(str: String): Int {
        val openChunkStack = ArrayDeque<Char>()
        str.forEach {
            if (it in openChunk) {
                openChunkStack.addFirst(it)
            } else if (openChunkStack.isEmpty() || openChunkStack.first() != closeChunkMapping[it]!!) {
                return closeChunkErrorScore[it]!!
            } else {
                openChunkStack.removeFirst()
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { getChunkSyntaxScore(it) }
    }

    fun part2(input: List<String>): Long {
        fun getChunkAutocompletionScore(str: String): Long {
            val openChunkStack = ArrayDeque<Char>()
            str.forEach {
                if (it in openChunk) {
                    openChunkStack.addFirst(it)
                } else {
                    openChunkStack.removeFirst()
                }
            }

            return openChunkStack
                .map { closeChunkAutoCompleteScore[it]!! }
                .fold(0) { acc, value -> acc * 5 + value }
        }

        val filteredInputs = input
            .filter { getChunkSyntaxScore(it) == 0 }

        return filteredInputs
            .map { getChunkAutocompletionScore(it) }
            .sorted()[filteredInputs.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput), 26397)

    val input = readInput("Day10")
    println(part1(input))

    check(part2(testInput), 288957)
    println(part2(input))
}
