enum class PipeType(val representation: Char) {
    NORTH_SOUTH('|'),
    EAST_WEST('-'),
    NORTH_EAST('L'),
    NORTH_WEST('J'),
    SOUTH_WEST('7'),
    SOUTH_EAST('F'),
    START('S'),
    NONE('.');

    fun getExit(goingTo: CompassDirection): CompassDirection {
        val entrance = when (goingTo) {
            CompassDirection.NORTH -> CompassDirection.SOUTH
            CompassDirection.SOUTH -> CompassDirection.NORTH
            CompassDirection.EAST -> CompassDirection.WEST
            CompassDirection.WEST -> CompassDirection.EAST
            CompassDirection.BLOCKED -> CompassDirection.BLOCKED
        }
        val possibilities = directionMap.getValue(this)
        if (possibilities.first == entrance) {
            return possibilities.second
        }
        if (possibilities.second == entrance) {
            return possibilities.first
        }
        return CompassDirection.BLOCKED
    }

    companion object {
        val directionMap = entries.associate { type ->
            val splitName = type.name.split("_")
            if (type == START) {
                return@associate type to Pair(CompassDirection.SOUTH, CompassDirection.EAST)
            }
            if (splitName.size != 2) {
                return@associate type to Pair(CompassDirection.BLOCKED, CompassDirection.BLOCKED)
            }

            val from = CompassDirection.valueOf(splitName.first)
            val to = CompassDirection.valueOf(splitName.last)
            Pair(type, Pair(from, to))
        }

        fun valueOf(char: Char): PipeType {
            return entries.first { it.representation == char }
        }
    }
}

enum class CompassDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    BLOCKED
}

data class Position(val x: Int, val y: Int)
data class Pipe(val position: Position, val type: PipeType)
typealias PipeSystem = List<List<PipeType>>

fun getNextPipe(currentPosition: Position, goingTo: CompassDirection, pipeSystem: PipeSystem): Pipe {
    val newPosition: Position = when (goingTo) {
        CompassDirection.NORTH -> Position(currentPosition.x, currentPosition.y - 1)
        CompassDirection.SOUTH -> Position(currentPosition.x, currentPosition.y + 1)
        CompassDirection.EAST -> Position(currentPosition.x + 1, currentPosition.y)
        CompassDirection.WEST -> Position(currentPosition.x - 1, currentPosition.y)
        CompassDirection.BLOCKED -> Position(-1, -1)
    }
    if (newPosition.x !in 0..pipeSystem.first.size || newPosition.y !in 0..pipeSystem.size) {
        return Pipe(newPosition, PipeType.NONE)
    }
    return Pipe(newPosition, pipeSystem[newPosition.y][newPosition.x])
}

fun getStartDirection(position: Position, pipeSystem: PipeSystem): CompassDirection {
    for (direction in CompassDirection.entries) {
        val nextPipe = getNextPipe(position, direction, pipeSystem)
        if (nextPipe.type != PipeType.NONE && nextPipe.type.getExit(direction) != CompassDirection.BLOCKED) {
            return direction
        }
    }
    return CompassDirection.BLOCKED
}

fun main() {


    fun part1(input: List<String>): Int {
        val pipeSystem = input.map { line -> line.map { PipeType.valueOf(it) } }
        val y = pipeSystem.indexOfFirst { list -> list.contains(PipeType.START) }
        val x =
            pipeSystem.first { list -> list.contains(PipeType.START) }.indexOfFirst { type -> type == PipeType.START }
        val startPosition = Position(x, y)

        var loopLength = 1
        var oldDirection = getStartDirection(startPosition, pipeSystem)
        var currentPipe = getNextPipe(startPosition, oldDirection, pipeSystem)
        do {
            val newDirection = currentPipe.type.getExit(oldDirection)
            currentPipe = getNextPipe(currentPipe.position, newDirection, pipeSystem)
            oldDirection = newDirection
            loopLength += 1
        } while (currentPipe.type != PipeType.START)
        return loopLength / 2
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day10_part1_test")
    val part1Result = part1(testInput1)
    check(part1Result == 4) { "Part 1 wrong result: $part1Result" }
//    val testInput2 = readInput("Day10_part2_test")
//    val part2Result = part2(testInput2)
//    check(part2Result == 71503) { "Part 2 wrong result: $part2Result" }


    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
