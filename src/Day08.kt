enum class Direction {
    R,
    L
}
typealias Location = Pair<String, String>

fun String.toLocation(): Location {
    val (left, right) = removePrefix("(").removeSuffix(")").split(", ")
    return Pair(left, right)
}

fun main() {

    fun followDirections(
        directions: List<Direction>,
        map: Map<String, Location>,
        start: String,
        endName: String
    ): Long {
        var currentLocation = start
        var stepsTaken = 0L

        while (!currentLocation.endsWith(endName)) {
            for (nextStep in directions) {
                if (currentLocation.endsWith(endName)) {
                    return stepsTaken
                }
                val possibleLocations = map.getValue(currentLocation)
                currentLocation = when (nextStep) {
                    Direction.R -> possibleLocations.second
                    Direction.L -> possibleLocations.first
                }
                stepsTaken += 1
            }

        }
        return stepsTaken
    }

    fun getLeastCommonMultiple(a: Long, b: Long): Long {
        var greatestDivisor = a
        var tempB = b
        var remainder: Long
        while (tempB != 0L) {
            remainder = greatestDivisor % tempB
            greatestDivisor = tempB
            tempB = remainder
        }
        return a * b / greatestDivisor
    }

    fun part1(input: List<String>): Long {
        val directions = input.first.map { Direction.valueOf(it.toString()) }
        val map = input.takeLast(input.size - 2).associate { line ->
            val (name, destinations) = line.split(" = ")

            name to destinations.toLocation()
        }
        return followDirections(directions, map, "AAA", "ZZZ")
    }

    fun part2(input: List<String>): Long {
        val directions = input.first.map { Direction.valueOf(it.toString()) }
        val map = input.takeLast(input.size - 2).associate { line ->
            val (name, destinations) = line.split(" = ")

            name to destinations.toLocation()
        }

        return map.filter { it.key.endsWith("A") }
            .map { followDirections(directions, map, it.key, "Z") }
            .reduce { minStepsForAll, stepsForThis -> getLeastCommonMultiple(minStepsForAll, stepsForThis) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput_part1 = readInput("Day08_part1_test")
    val part1Result = part1(testInput_part1)
    check(part1Result == 6L) { "Part 1 wrong result: $part1Result" }
    val testInput_part2 = readInput("Day08_part2_test")
    val part2Result = part2(testInput_part2)
    check(part2Result == 6L) { "Part 2 wrong result: $part2Result" }


    val input = readInput("Day08")
    check(part1(input) == 12599L)
    part1(input).println()
    part2(input).println()
}
