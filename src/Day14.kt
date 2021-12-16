fun main() {

    fun calcTemplateScoreAfterIter(template: String, numSteps: Int, pairInsertions: Map<String, Char>): Long {
        data class CacheKey(val c1: Char, val c2: Char, val numStep: Int)

        val cache: MutableMap<CacheKey, Map<Char, Long>> = mutableMapOf()

        fun calcCharDistribution(pair: Pair<Char, Char>, numSteps: Int, pairInsertions: Map<String, Char>): Map<Char, Long> {
            val cacheKey = CacheKey(pair.first, pair.second, numSteps)
            if (cacheKey in cache) {
                return cache[cacheKey]!!
            }
            if (numSteps == 0) {
                cache[cacheKey] = mapOf(pair.first to 1)
                return cache[cacheKey]!!
            }

            val charCount = mutableMapOf<Char, Long>()
            val newChar = pairInsertions[pair.toList().joinToString("")]!!
            listOf(pair.first, newChar, pair.second)
                .zipWithNext()
                .map { calcCharDistribution(it, numSteps - 1, pairInsertions) }
                .forEach { it.forEach { (k, v) -> charCount[k] = (charCount[k] ?: 0) + v } }

            cache[cacheKey] = charCount
            return charCount
        }

        val charCount = mutableMapOf<Char, Long>()

        template.zipWithNext()
            .map { calcCharDistribution(it, numSteps, pairInsertions) }
            .forEach { it.forEach { (k, v) -> charCount[k] = charCount[k]?.plus(v) ?: v } }

        charCount[template.last()] = (charCount[template.last()] ?: 0) + 1
        val maxChars = charCount.maxOf { it.value }
        val minChars = charCount.minOf { it.value }

        return maxChars - minChars
    }

    fun parsePairInsertions(input: List<String>) = input
        .filter { it.contains("->") }
        .map { it.split(" -> ") }
        .associate { it[0] to it[1].toCharArray()[0] }

    fun calculateScore(input: List<String>, numSteps: Int): Long {
        val template = input[0]
        val pairInsertions = parsePairInsertions(input)

        return calcTemplateScoreAfterIter(template, numSteps, pairInsertions)
    }

    fun part1(input: List<String>): Long {
        return calculateScore(input, 10)
    }

    fun part2(input: List<String>): Long {
        return calculateScore(input, 40)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    //check(part1(testInput), 1588)

    val input = readInput("Day14")
    println(part1(input))
    check(part1(input), 3213)

    check(part2(testInput), 2188189693529L)
    println(part2(input))
}
