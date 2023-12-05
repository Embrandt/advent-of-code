import kotlin.math.max
import kotlin.math.min

data class Num(val lineIndex: Int, val internalRange: IntRange, val data: String)
data class Symbol(val lineIndex: Int, val charIndex: Int, val data: Char)

fun main() {
    fun symbolsAdjacentToNumber(
        num: Num,
        coordinates: Array<String>,
        symbolsToConsider: (Char) -> Boolean
    ): MutableList<Symbol> {
        val maxAbove = max(0, num.lineIndex - 1)
        val maxBelow = min(coordinates.size - 1, num.lineIndex + 1)
        val maxLeft = max(0, num.internalRange.first - 1)
        val maxRight = min(coordinates[0].length - 1, num.internalRange.last + 1)
        val occupied = "[^(\\d)|.]".toRegex()
        val above = coordinates[maxAbove]
        val below = coordinates[maxBelow]
        val left = coordinates[num.lineIndex][maxLeft]
        val right = coordinates[num.lineIndex][maxRight]

        val adjacentCharSequence = StringBuilder()
        val adjacentSymbols: MutableList<Symbol> = mutableListOf()
        if (num.lineIndex != 0) {
            above.withIndex().filter { (index, char) -> index in maxLeft..maxRight && symbolsToConsider(char) }
                .mapTo(adjacentSymbols) { (index, char) -> Symbol(maxAbove, index, char) }

            adjacentCharSequence.append(above)
        }
        if (num.lineIndex != coordinates.size - 1) {
            below.withIndex().filter { (index, char) -> index in maxLeft..maxRight && symbolsToConsider(char) }
                .mapTo(adjacentSymbols) { (index, char) -> Symbol(maxBelow, index, char) }
            adjacentCharSequence.append(below)
        }
        if (maxLeft != num.internalRange.first) {
            if (symbolsToConsider(left)) {
                adjacentSymbols.add(Symbol(num.lineIndex, maxLeft, left))
            }
            adjacentCharSequence.append(left)
        }
        if (maxRight != num.internalRange.last) {
            if (symbolsToConsider(right)) {
                adjacentSymbols.add(Symbol(num.lineIndex, maxRight, right))
            }
            adjacentCharSequence.append(right)
        }

        occupied.containsMatchIn(adjacentCharSequence)
        return adjacentSymbols
    }

    fun isPart(num: Num, coordinates: Array<String>): Boolean {
        return symbolsAdjacentToNumber(
            num,
            coordinates
        ) { character -> character !in ".\n" && !character.isDigit() }
            .isNotEmpty()
    }

    fun part1(input: List<String>): Int {
        val toArray = input.toTypedArray()
        val regex = "(\\d+)".toRegex()

        return input
            .withIndex()
            .flatMap { line ->
                regex.findAll(line.value)
                    .map { match ->
                        Num(line.index, match.range, match.value)
                    }
            }
            .filter { isPart(it, toArray) }
            .map { it.data.toInt() }
            .toList()
            .sum()
    }

    fun mapSymbolsToNumbers(number: Num, coordinates: Array<String>, symbolMap: MutableMap<Symbol, MutableList<Int>>) {
        symbolsAdjacentToNumber(number, coordinates) { it == '*' }
            .forEach { symbol ->
                val numberList = symbolMap.getOrDefault(symbol, mutableListOf())
                numberList.add(number.data.toInt())
                symbolMap[symbol] = numberList
            }
    }

    fun part2(input: List<String>): Int {
        val toArray = input.toTypedArray()
        val regex = "(\\d+)".toRegex()
        val symbolMap = mutableMapOf<Symbol, MutableList<Int>>()
        input
            .withIndex()
            .flatMap { line ->
                regex.findAll(line.value)
                    .map { match ->
                        Num(line.index, match.range, match.value)
                    }
            }
            .forEach { number -> mapSymbolsToNumbers(number, toArray, symbolMap) }
        return symbolMap
            .filter { (_, value) -> value.size == 2 }
            .map { (_, value) -> value[0] * value[1] }
            .sum()

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
