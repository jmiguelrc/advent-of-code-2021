import java.util.concurrent.ConcurrentHashMap

fun main() {

    data class RequestKey(val remDays: Int, val numDays: Int)
    class ComputeTotalLanternFishes {
        private val cache = ConcurrentHashMap<RequestKey, Long>()

        fun howManyTotalAfterDaysWithCache(remDays: Int, numDays: Int): Long {
            return howManyTotalAfterDays(RequestKey(remDays, numDays))
        }

        private fun howManyTotalAfterDays(reqKey: RequestKey): Long {
            if (cache.containsKey(reqKey)) {
                return cache[reqKey]!!
            }
            var currentRemDays = reqKey.remDays
            var currentNumDaysElapsed = reqKey.numDays
            var total = 1L
            while (currentNumDaysElapsed > currentRemDays) {
                currentNumDaysElapsed -= currentRemDays
                currentRemDays = 7
                total += howManyTotalAfterDays(RequestKey(9, currentNumDaysElapsed))
            }
            cache[reqKey] = total
            return total
        }
    }


    fun howManyAfterXDays(input: List<String>, numDays: Int): Long {
        val computeTotalLanternFishes = ComputeTotalLanternFishes()

        return input[0].split(",")
            .map { it.toInt() }
            .sumOf { computeTotalLanternFishes.howManyTotalAfterDaysWithCache(it, numDays) }
    }

    fun part1(input: List<String>): Long {
        return howManyAfterXDays(input, 80)
    }

    fun part2(input: List<String>): Long {
        return howManyAfterXDays(input, 256)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput), 5934)

    val input = readInput("Day06")
    println(part1(input))

    check(part2(testInput), 26984457539)
    println(part2(input))
}
