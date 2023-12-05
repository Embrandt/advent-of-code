import kotlin.math.pow

data class Card(val winningNumbers: Set<Int>, val ownNumbers: Set<Int>)

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            val numbers = it.removePrefix("Card ").split(":").get(1).split("|")
            val winningNumbers = numbers[0].removeSurrounding(" ").windowed(2,3).map { string -> string.trim().toInt() }
            val ownNumbers = numbers[1].removePrefix(" ").windowed(2,3).map{string -> string.trim().toInt()}
            Card(winningNumbers.toSet(),ownNumbers.toSet())
        }
            .map {
            card ->
            val winNumbers = card.ownNumbers.toMutableSet()
            winNumbers.retainAll(card.winningNumbers.toSet())
            if (winNumbers.size == 0) {
                return@map 0
            }
            val points : Int = 2.toFloat().pow(winNumbers.size-1).toInt()
            points
        }.sum()

    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13) { "Part 1 wrong result: ${part1(testInput)}" }
//    check(part2(testInput) == -1)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
