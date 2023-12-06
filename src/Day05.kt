data class CustomMap(val mappers: MutableList<RangeMapper>) {
    fun convert(entry: Long): Long {
        for (mapper in mappers) {
            if (mapper.isInRange(entry)) {
                return mapper.convert(entry)
            }
        }
        return entry
    }

    fun reverse(entry: Long): Long {
        for (mapper in mappers.asReversed()) {
            if (mapper.isInDestinationRange(entry)) {
                return mapper.reverse(entry)
            }
        }
        return entry
    }

    fun getMax(): Long {
        return mappers.maxOf { rangeMapper -> rangeMapper.destinationRangeStart + rangeMapper.rangeLength }
    }
}

data class RangeMapper(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {
    fun isInRange(entry: Long): Boolean {
        return entry in sourceRangeStart..<sourceRangeStart + rangeLength
    }

    fun convert(entry: Long): Long {
        return entry - sourceRangeStart + destinationRangeStart
    }

    fun isInDestinationRange(entry: Long): Boolean {
        return entry in destinationRangeStart..<destinationRangeStart + rangeLength
    }

    fun reverse(entry: Long): Long {
        return entry - destinationRangeStart + sourceRangeStart
    }
}

data class SeedRange(val start: Long, val length: Long) {
    fun isInRange(entry: Long): Boolean {
        return entry in start..start + length
    }
}

fun main() {
    fun extractMaps(input: List<String>): MutableList<CustomMap> {
        val listOfMaps = mutableListOf<CustomMap>()
        var currentMap: CustomMap

        input.forEach { line ->
            if (line.contains("map")) {
                currentMap = CustomMap(mutableListOf())
                listOfMaps.add(currentMap)
            } else if (!line.startsWith("seed") && line.isNotBlank()) {
                val splitLine = line.split(" ").map(String::toLong)
                listOfMaps.last.mappers.add(RangeMapper(splitLine[0], splitLine[1], splitLine[2]))
            }
        }
        return listOfMaps
    }

    fun getMinLocationNumber(listOfMaps: List<CustomMap>, seedNumbers: Collection<Long>): Long {
        var transformed = seedNumbers
        for (map in listOfMaps) {
            transformed = transformed.map { map.convert(it) }
        }
        return transformed.min()
    }

    fun part1(input: List<String>): Long {
        val seedNumbers = input.first.removePrefix("seeds: ").split(" ").map(String::toLong)
        val listOfMaps = extractMaps(input)
        return getMinLocationNumber(listOfMaps, seedNumbers)
    }

    fun reverseFind(seedRanges: List<SeedRange>, listOfMaps: List<CustomMap>): Long {
        val max = listOfMaps.last.getMax()
        for (i in 0..max) {
            var next = i
            for (map in listOfMaps.asReversed()) {
                next = map.reverse(next)
            }
            for (range in seedRanges) {
                if (range.isInRange(next)) {
                    return i
                }
            }
        }
        return -1
    }

    fun part2(input: List<String>): Long {
        val listOfMaps = extractMaps(input)
        val seedRanges = input.first
            .removePrefix("seeds: ")
            .split(" ")
            .map(String::toLong)
            .windowed(2, 2)
            .map { SeedRange(it.first, it.last) }

        return reverseFind(seedRanges, listOfMaps)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val part1Result = part1(testInput)
    check(part1Result == 35L) { "Part 1 wrong result: $part1Result" }
    val part2Result = part2(testInput)
    check(part2Result == 46L) { "Part 2 wrong result: $part2Result" }


    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
