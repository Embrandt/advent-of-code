data class CustomMap(val mappers : MutableList<RangeMapper>) {
    fun convert(entry : Long) : Long{
        for (mapper in mappers) {
            if (mapper.isInRange(entry)) {
                return mapper.convert(entry)
            }
        }
        return entry
    }
}
data class RangeMapper(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {
    fun isInRange(entry: Long): Boolean {
        return entry in sourceRangeStart..sourceRangeStart + rangeLength
    }
    fun convert(entry : Long) : Long {
        return entry-sourceRangeStart+destinationRangeStart
    }
}
fun main() {
    fun part1(input: List<String>): Long {
        val listOfMaps = mutableListOf<CustomMap>()
        var currentMap : CustomMap
        val seedNumbers = input.first.removePrefix("seeds: ").split(" ").map(String::toLong)
        input.forEach { line ->
            if (line.contains("map")) {
                currentMap = CustomMap(mutableListOf())
                listOfMaps.add(currentMap)
            } else if (!line.startsWith("seed") && line.isNotBlank()) {
                val splitLine = line.split(" ").map(String::toLong)
                listOfMaps.last.mappers.add(RangeMapper(splitLine[0],splitLine[1],splitLine[2]))
            }
        }
        var transformed = seedNumbers
        for (map in listOfMaps) {
            transformed = transformed.map { map.convert(it) }
        }
        return transformed.min()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val part1Result = part1(testInput)
    check(part1Result == 35L) { "Part 1 wrong result: $part1Result" }
//    val part2Result = part2(testInput)
//    check(part2Result == -1) { "Part 2 wrong result: $part2Result" }


    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
