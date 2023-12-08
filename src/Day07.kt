enum class PokerCard(val shortName: Char) {
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    companion object {
        fun valueOf(char: Char): PokerCard {
            return entries.first { it.shortName == char }
        }
    }
}

data class PokerHand(val cards: List<PokerCard>, val bid: Int) : Comparable<PokerHand> {
    private fun getRank(): Int {
        when (cards.distinct().size) {
            1 -> return 7
            2 -> return if (cards.count { cards.first == it } in 2..3) 5 else 6
            3 -> {
                for (card in cards) {
                    if (cards.count { it == card } == 3)
                        return 4
                }
                return 3
            }

            4 -> return 2
            5 -> return 1
        }
        return -1
    }

    override fun compareTo(other: PokerHand): Int {
        if (getRank() == other.getRank()) {
            for ((index, cardFromFirst) in cards.withIndex()) {
                if (other.cards[index] == cardFromFirst) {
                    continue
                }
                return cardFromFirst.ordinal - other.cards[index].ordinal
            }
        }
        return getRank() - other.getRank()
    }
}


fun main() {

    fun part1(input: List<String>): Int {
        val hands = input.map { line ->
            val splitline = line.split(" ")
            val hand = splitline.first.map { hand -> PokerCard.valueOf(hand) }
            val bid = splitline[1].toInt()
            return@map PokerHand(hand, bid)
        }

        val winningsFunction = { rank: Int, sum: Int, hand: PokerHand -> sum + (rank + 1) * hand.bid }
        return hands.sorted().foldIndexed(0, winningsFunction)

    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val part1Result = part1(testInput)
    check(part1Result == 6440) { "Part 1 wrong result: $part1Result" }
//    val part2Result = part2(testInput)
//    check(part2Result == 71503) { "Part 2 wrong result: $part2Result" }


    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}


