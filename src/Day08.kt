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

    fun followDirections(directions: List<Direction>, map: Map<String, Location>): Int {
        var currentLocation = "AAA"
        var stepsTaken = 0

        while (currentLocation != "ZZZ") {
            for (nextStep in directions) {
                if (currentLocation == "ZZZ") {
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

    fun part1(input: List<String>): Int {
        val directions = input.first.map { Direction.valueOf(it.toString()) }
        val map = input.takeLast(input.size - 2).associate { line ->
            val (name, destinations) = line.split(" = ")

            name to destinations.toLocation()
        }
        return followDirections(directions, map)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput_part1 = readInput("Day08_part1_test")
    val part1Result = part1(testInput_part1)
    check(part1Result == 6) { "Part 1 wrong result: $part1Result" }
//    val testInput_part2 = readInput("Day08_part1_test")
//    val part2Result = part2(testInput_part2)
//    check(part2Result == 6) { "Part 2 wrong result: $part2Result" }


    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
