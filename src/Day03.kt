import kotlin.math.max
import kotlin.math.min

data class Number(val lineIndex: Int, val internalRange: IntRange, val data: Int)

fun main() {
    fun isPart(number: Number, coordinates: Array<String>): Boolean {
        val maxAbove = max(0, number.lineIndex - 1)
        val maxBelow = min(coordinates.size - 1, number.lineIndex + 1)
        val maxLeft = max(0, number.internalRange.first - 1)
        val maxRight = min(coordinates[0].length - 1, number.internalRange.last + 1)
        val occupied = "[^(\\d)|.]".toRegex()
        val above = coordinates[maxAbove].substring(maxLeft, maxRight + 1)
        val below = coordinates[maxBelow].substring(maxLeft, maxRight + 1)
        val left = coordinates[number.lineIndex][maxLeft]
        val right = coordinates[number.lineIndex][maxRight]

        val adjacentCharSequence = StringBuilder()
        if (number.lineIndex != 0) {
            adjacentCharSequence.append(above)
        }
        if (number.lineIndex != coordinates.size - 1) {
            adjacentCharSequence.append(below)
        }
        if (maxLeft != number.internalRange.first) {
            adjacentCharSequence.append(left)
        }
        if (maxRight != number.internalRange.last) {
            adjacentCharSequence.append(right)
        }
        return occupied.containsMatchIn(adjacentCharSequence)
    }

    fun part1(input: List<String>): Int {
        val toArray = input.toTypedArray()
        val regex = "(\\d+)".toRegex()

        return input.withIndex().flatMap { line -> regex.findAll(line.value).map { match -> Number(line.index, match.range, match.value.toInt()) }.filter { isPart(it, toArray) } }
                .map { it.data }.toList().sum()
    }


    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
//    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
