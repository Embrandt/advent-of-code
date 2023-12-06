import kotlin.math.pow

data class Card(val cardNumber: Int, val winningNumbers: Set<Int>, val ownNumbers: Set<Int>)

fun main() {
    fun String.toCard(): Card {
        val splitString = this.split(":")
        val numbers = splitString[1].split("|")
        val winningNumbers = numbers[0].removeSurrounding(" ").windowed(2, 3).map { string -> string.trim().toInt() }
        val ownNumbers = numbers[1].removePrefix(" ").windowed(2, 3).map { string -> string.trim().toInt() }
        return Card(splitString[0].removePrefix("Card ").trim().toInt(), winningNumbers.toSet(), ownNumbers.toSet())
    }

    fun Card.countWinningNumbers(): Int {
        val winNumbers = this.ownNumbers.toMutableSet()
        winNumbers.retainAll(this.winningNumbers.toSet())
        return winNumbers.size
    }

    fun part1(input: List<String>): Int {
        return input.map(String::toCard)
            .map { card ->
                val winNumbers = card.countWinningNumbers()
                if (winNumbers == 0) {
                    return@map 0
                }
                val points: Int = 2.toFloat().pow(winNumbers - 1).toInt()
                points
            }.sum()

    }

    fun Card.getWhichTickets(cardMap: Map<Int, Card>): List<Card> {
        val cardList = mutableListOf<Card>()
        for (i in cardNumber + 1..cardNumber + countWinningNumbers()) {
            cardMap[i]?.let { cardList.add(it) }
        }
        return cardList
    }

    tailrec fun createTickets(sum: Int, existingCards: MutableList<Card>, cardMap: Map<Int, Card>): Int {
        if (existingCards.isEmpty()) {
            return sum
        }
        val currentNumber = existingCards.count { card -> card == existingCards.first }
        val currentCard = existingCards.first
        existingCards.removeAll { card -> card == currentCard }
        for (i in 1..currentNumber) {
            existingCards.addAll(currentCard.getWhichTickets(cardMap))
        }
        return createTickets(sum + currentNumber, existingCards, cardMap)
    }

    fun part2(input: List<String>): Int {
        val cardMap = input.map(String::toCard)
            .associate { card -> Pair(card.cardNumber, card) }
        return createTickets(0, cardMap.values.toMutableList(), cardMap)

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13) { "Part 1 wrong result: ${part1(testInput)}" }
    check(part2(testInput) == 30) { "Part 2 wrong result: ${part2(testInput)}" }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
