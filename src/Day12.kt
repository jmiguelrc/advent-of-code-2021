fun main() {
    fun getCaveConnections(input: List<String>) = input
        .flatMap {
            val startAndEndConn = it.split("-")
            listOf(startAndEndConn[0] to startAndEndConn[1], startAndEndConn[1] to startAndEndConn[0])
        }
        .groupBy({ it.first }, { it.second })

    fun part1(input: List<String>): Int {
        fun findAllPaths(
            connections: Map<String, List<String>>,
            visited: Set<String> = mutableSetOf(),
            currentPath: List<String> = listOf("start")
        ): List<List<String>> {
            if (currentPath.last() == "end") {
                return listOf(currentPath)
            }
            return connections[currentPath.last()]
                ?.filter { it != "start" }
                ?.filter { it !in visited || it.uppercase() == it }
                ?.flatMap {
                    findAllPaths(connections,
                        visited.toMutableSet().apply { add(it) },
                        currentPath.toMutableList().apply { add(it) })
                } ?: listOf()
        }

        val connections: Map<String, List<String>> = getCaveConnections(input)

        val findAllPaths = findAllPaths(connections)
        return findAllPaths.size
    }

    fun part2(input: List<String>): Int {
        val connections: Map<String, List<String>> = getCaveConnections(input)

        fun findAllPaths(
            connections: Map<String, List<String>>,
            visited: Set<String> = mutableSetOf(),
            currentPath: List<String> = listOf("start"),
            alreadyVisitedTwiceSmallCave: Boolean = false
        ): List<List<String>> {
            if (currentPath.last() == "end") {
                return listOf(currentPath)
            }
            fun isSmallCave(cave: String) = cave != "start" && cave.uppercase() != cave
            return connections[currentPath.last()]
                ?.filter { it != "start" }
                ?.filter { !alreadyVisitedTwiceSmallCave || it !in visited || !isSmallCave(it) }
                ?.flatMap {
                    findAllPaths(
                        connections,
                        visited.toMutableSet().apply { add(it) },
                        currentPath.toMutableList().apply { add(it) },
                        alreadyVisitedTwiceSmallCave || (isSmallCave(it) && it in visited)
                    )
                } ?: listOf()
        }


        val findAllPaths = findAllPaths(connections)
        return findAllPaths.size
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    val testInputLarger = readInput("Day12_test_larger")
    check(part1(testInput), 10)
    check(part1(testInputLarger), 19)

    val input = readInput("Day12")
    println(part1(input))

    check(part2(testInput), 36)
    check(part2(testInputLarger), 103)
    println(part2(input))
}
