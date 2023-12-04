fun main() {
        val map = (1..9).associateBy { it.toString() } + mapOf(
                "one" to 1,
                "two" to 2,
                "three" to 3,
                "four" to 4,
                "five" to 5,
                "six" to 6,
                "seven" to 7,
                "eight" to 8,
                "nine" to 9,
        )

    fun part1(input: List<String>): Int {
        var sum = 0
                for (line in input) {
                    val result =
                            line.filter { it.isDigit() }
                    val firstDigit = result.first()
                    val lastDigit = result.last()
                    val combined = StringBuilder().append(firstDigit).append(lastDigit)
                    sum += combined.toString().toInt()
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { row ->
            val firstDigit = map
                    .map { (key, value) -> row.indexOf(key) to value }
                    .filter { (key) -> key != -1 }
                    .minByOrNull { (key) -> key }
                    ?.second
            val lastDigit = map
                    .map { (key, value) -> row.lastIndexOf(key) to value }
                    .filter { (key) -> key != -1 }
                    .maxBy { (key) -> key }
                    .second
            "$firstDigit$lastDigit".toInt()
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 22)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
