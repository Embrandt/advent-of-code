import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

data class Race(val time: Int, val record: Int)

fun main() {
    fun calculateX(race: Race): Pair<Double, Double> {
        //        x = sqrt((p/2)^2-q)-p/2
//        sqrt(p^2/4-4q/4)-p/2 = sqrt(p^2-4q)/2-p/2 = (sqrt(p^2-4q)-p)/2
        val det = race.time * race.time - 4 * race.record
        val first = ((-race.time + sqrt(det.toDouble())) / -2)

        val second = ((-race.time - sqrt(det.toDouble())) / -2)
        return Pair(first, second)
    }

    fun calculateChances(min: Double, max: Double): Int {
        var int = 0
        while ((ceil(min) + int < max)) {
            int += 1
        }
        if (floor(min) == min) {
            int -= 1
        }
        return int
    }

    fun part1(input: List<String>): Int {
        val times = input[0].toNumberList()
        val distances = input[1].toNumberList()

        return times.asSequence().withIndex().map { (index, time) -> Race(time, distances[index]) }
            .map { calculateX(it) }
            .map { calculateChances(it.first, it.second) }
            .reduce { num1, num2 -> num1 * num2 }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val part1Result = part1(testInput)
    check(part1Result == 288) { "Part 1 wrong result: $part1Result" }
//    val part2Result = part2(testInput)
//    check(part2Result == -1) { "Part 2 wrong result: $part2Result" }


    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
