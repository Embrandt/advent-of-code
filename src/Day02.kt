import Color.*

data class Draw(val drawnCubes : Map<Color, Int>)
data class Game(val id: Int, val draws: List<Draw>)

enum class Color {BLUE,RED,GREEN}

fun main() {
    fun String.toColorMap(): Draw {
        return Draw(split(",").map {
            cubes ->
            Color.valueOf(cubes.trim().substringAfter(" ").uppercase()) to cubes.trim().substringBefore(" ").toInt()
        }.toMap())
    }

    fun String.toGame() : Game = Game (
            substringBefore(":").filter { it.isDigit() }.toInt(),
            substringAfter(":").split(";").map(String::toColorMap)
    )

    fun isDrawPossible(draw : Draw, bagContent : Map<Color, Int>) : Boolean {
        for ((color, number) in draw.drawnCubes) {
            if (bagContent[color]!! < number) {
                return false
            }
        }
        return true
    }

    fun isGamePossible(game : Game, bagContent : Map<Color, Int>) : Boolean {
        for (draw in game.draws) {
            if (!isDrawPossible(draw, bagContent)) {
                return false
            }
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val bagContent = mapOf(RED to 12, BLUE to 14, GREEN to 13)
        return input.map(String::toGame)
                .filter { game -> isGamePossible(game, bagContent) }
                .map(Game::id)
                .sum()
    }

    fun minBagContent(game : Game) : Map<Color, Int> {
        val minCubes : MutableMap<Color, Int> = entries.associateWith {  0 }.withDefault { 0 }.toMutableMap()
        for (draw in game.draws) {
            val cubes = draw.drawnCubes
            for (key in cubes.keys) {
                val minValue = cubes.getOrDefault(key, 0)
                if (minValue > minCubes.getValue(key)) {
                    minCubes[key] = minValue
                }
            }
        }
        return minCubes
    }
    fun part2(input: List<String>): Int {
        return input
                .map(String::toGame)
                .map { game -> minBagContent(game) }
                .map { content -> content[RED]!! *content[BLUE]!!*content[GREEN]!! }
                .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
