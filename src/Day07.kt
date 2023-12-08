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

data class PokerHand(val cards: List<PokerCard>, val bid: Int, val listOfJokers: Set<PokerCard>) :
    Comparable<PokerHand> {
    private fun getRank(): Int {
        val jokers = cards.filter { it in listOfJokers }
        val remainingCards = cards - jokers.toSet()
        // distinct().size gives us the number of cards that are different to each other
        when (remainingCards.distinct().size) {
            0 -> return 7 // only jokers     -> Five of a kind
            1 -> return 7 // all cards equal -> Five of a kind
            2 -> {
                for (card in cards) {
                    // any card plus jokers is present 4 times -> 4 of a kind
                    if (remainingCards.count { it == card } + jokers.size == 4)
                        return 6
                }
                // only other combination with two distinct cards is a full house
                return 5
            }

            3 -> {
                for (card in cards) {
                    // any card plus jokers is present 3 times -> 3 of a kind
                    if (remainingCards.count { it == card } + jokers.size == 3)
                        return 4
                }
                // if it's not a three of a kind it has to be two pairs
                return 3
            }
            4 -> return 2 // max one joker, so max one pair
            5 -> return 1 // no joker, so single pair
        }
        return -1
    }

    override fun compareTo(other: PokerHand): Int {
        if (getRank() == other.getRank()) {
            for ((index, cardFromFirst) in cards.withIndex()) {
                if (other.cards[index] == cardFromFirst) {
                    continue
                }
                if (other.cards[index] in listOfJokers) {
                    return 1
                }
                if (cardFromFirst in listOfJokers) {
                    return -1
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
            return@map PokerHand(hand, bid, emptySet<PokerCard>())
        }

        val winningsFunction = { rank: Int, sum: Int, hand: PokerHand -> sum + (rank + 1) * hand.bid }
        return hands.sorted().foldIndexed(0, winningsFunction)

    }

    fun part2(input: List<String>): Int {
        val hands = input.map { line ->
            val splitline = line.split(" ")
            val hand = splitline.first.map { hand -> PokerCard.valueOf(hand) }
            val bid = splitline[1].toInt()
            return@map PokerHand(hand, bid, setOf(PokerCard.JACK))
        }

        val winningsFunction = { rank: Int, sum: Int, hand: PokerHand -> sum + (rank + 1) * hand.bid }
        return hands.sorted().foldIndexed(0, winningsFunction)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val part1Result = part1(testInput)
    check(part1Result == 6440) { "Part 1 wrong result: $part1Result" }
    val part2Result = part2(testInput)
    check(part2Result == 5905) { "Part 2 wrong result: $part2Result" }


    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}


