fun main() {

    fun extrapolate(valueHistory: List<Long>): Long {
        var lastCalculation = valueHistory
        val pyramid = mutableListOf(lastCalculation)
        while (lastCalculation.size != 1) {
            val reducedList = lastCalculation.zipWithNext { a, b -> b - a }
            if (reducedList.isNotEmpty()) pyramid.add(reducedList)
            lastCalculation = reducedList
        }
        return pyramid.fold(0) { prediction: Long, list -> prediction + list.last }
    }

    fun extrapolateBackWards(valueHistory: List<Long>): Long {
        var lastCalculation = valueHistory
        val pyramid = mutableListOf(lastCalculation)
        while (lastCalculation.size != 1) {
            val reducedList = lastCalculation.zipWithNext { a, b -> b - a }
            if (reducedList.isNotEmpty()) pyramid.add(reducedList)
            lastCalculation = reducedList
        }
        return pyramid.reversed().fold(0) { prediction: Long, list -> list.first - prediction }
    }

    fun part1(input: List<String>): Int {
        val result = input.map { it.toNumberList() }
        return result.sumOf { extrapolate(it) }.toInt()
    }

    fun part2(input: List<String>): Int {
        val result = input.map { it.toNumberList() }
        return result.sumOf { extrapolateBackWards(it) }.toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    val part1Result = part1(testInput)
    check(part1Result == 114) { "Part 1 wrong result: $part1Result" }
    val part2Result = part2(testInput)
    check(part2Result == 2) { "Part 2 wrong result: $part2Result" }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
