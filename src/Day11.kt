import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun getExpandedUniverse(universe: List<String>, expansionTimes: Int): List<List<Char>> {
        val emptyRows = universe.withIndex().filter { line -> !line.value.contains("#") }.map { it.index }
        val emptyColumns = (0..<universe[0].length).toList() - universe.flatMap { s: String ->
            s.withIndex().filter { it.value == '#' }.map { it.index }
        }.toSet()
        val expandedInput = universe.map { line ->
            val expandedLine = line.toMutableList()
            val filler = List(expansionTimes) { '.' }
            for ((index, column) in emptyColumns.withIndex()) {
                expandedLine.addAll(column + index * expansionTimes, filler)
            }
            expandedLine.toList()
        }

        val expandedRows = expandedInput.toMutableList()
        val filler = List(expansionTimes) { List(universe[0].length + emptyColumns.size * (expansionTimes)) { '.' } }
        for ((index, row) in emptyRows.withIndex()) {
            expandedRows.addAll(row + index * expansionTimes, filler)
        }
        return expandedRows
    }

    fun part1(input: List<String>): Int {
        val expandedRows = getExpandedUniverse(input, 1)

        val galaxies = buildList {
            expandedRows.forEachIndexed { index, chars ->
                chars.forEachIndexed { innerIndex, c ->
                    if (c == '#') add(Pair(index, innerIndex))
                }
            }
        }

        return galaxies.mapIndexed { index, galaxy ->
            var sum = 0
            for (i in index + 1..<galaxies.size) {
                val partnerGalaxy = galaxies[i]
                val result = (partnerGalaxy.first - galaxy.first + abs(partnerGalaxy.second - galaxy.second))
                sum += result
            }
            sum
        }.sum()

    }

    fun part2(input: List<String>, expandFactor: Int): Long {
        val universe = getExpandedUniverse(input, 0)
        val emptyRows = input.withIndex().filter { line -> !line.value.contains("#") }.map { it.index }
        val emptyColumns = (0..<input[0].length).toList() - input.flatMap { s: String ->
            s.withIndex().filter { it.value == '#' }.map { it.index }
        }.toSet()

        val galaxies = buildList {
            universe.forEachIndexed { index, chars ->
                chars.forEachIndexed { innerIndex, c ->
                    if (c == '#') add(Pair(index, innerIndex))
                }
            }
        }

        return galaxies.mapIndexed { index, galaxy ->
            var sum = 0L
            for (i in index + 1..<galaxies.size) {
                val partnerGalaxy = galaxies[i]
                val result = (partnerGalaxy.first - galaxy.first + abs(partnerGalaxy.second - galaxy.second))
                val add = emptyRows.count { deadSpace -> deadSpace in galaxy.first..partnerGalaxy.first }
                val add2 = emptyColumns.count { deadSpace ->
                    deadSpace in min(galaxy.second, partnerGalaxy.second)..max(galaxy.second, partnerGalaxy.second)
                }
                sum += result + (expandFactor - 1) * (add + add2)
            }
            sum
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    val part1Result = part1(testInput)
    check(part1Result == 374) { "Part 1 wrong result: $part1Result" }
    val part2Result = part2(testInput, 10)
    check(part2Result == 1030L) { "Part 2 wrong result: $part2Result" }


    val input = readInput("Day11")
    part1(input).println()
    part2(input, 1000000).println()
}
