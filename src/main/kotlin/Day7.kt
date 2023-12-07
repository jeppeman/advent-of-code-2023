fun day7Part1(input: List<String>): Long {
    return input.map { it.parseHand(withJoker = false) }
        .sorted()
        .foldIndexed(0L) { index, acc, (_, _, bet) -> acc + bet * (index + 1) }
}

private enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND,
}

private val HandType.next: HandType
    get() = when (this) {
        HandType.HIGH_CARD -> HandType.ONE_PAIR
        HandType.ONE_PAIR -> HandType.THREE_OF_A_KIND
        HandType.TWO_PAIR -> HandType.FULL_HOUSE
        HandType.THREE_OF_A_KIND -> HandType.FOUR_OF_A_KIND
        HandType.FULL_HOUSE -> HandType.FOUR_OF_A_KIND
        HandType.FOUR_OF_A_KIND -> HandType.FIVE_OF_A_KIND
        HandType.FIVE_OF_A_KIND -> HandType.FIVE_OF_A_KIND
    }

private data class Hand(
    val cards: String,
    val type: HandType,
    val bet: Long,
    val withJoker: Boolean,
) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        val typeComparison = type.compareTo(other.type)
        if (typeComparison != 0) return typeComparison

        val firstCardComparison = cards[0].cardValue.compareTo(other.cards[0].cardValue)
        if (firstCardComparison != 0) return firstCardComparison

        val secondCardComparison = cards[1].cardValue.compareTo(other.cards[1].cardValue)
        if (secondCardComparison != 0) return secondCardComparison

        val thirdCardComparison = cards[2].cardValue.compareTo(other.cards[2].cardValue)
        if (thirdCardComparison != 0) return thirdCardComparison

        val fourthCardComparison = cards[3].cardValue.compareTo(other.cards[3].cardValue)
        if (fourthCardComparison != 0) return fourthCardComparison

        return cards[4].cardValue.compareTo(other.cards[4].cardValue)
    }

    private val Char.cardValue: Int
        get() = when (this) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> if (withJoker) 0 else 11
            'T' -> 10
            else -> this - '0'
        }
}


private fun String.parseHand(withJoker: Boolean): Hand {
    val (handString, betString) = split(" ")
    val cards = hashMapOf<Char, Int>()
    var jokers = 0L

    for (card in handString) {
        if (withJoker && card == 'J') {
            jokers++
        } else {
            cards[card] = cards.getOrDefault(card, 0) + 1
        }
    }

    var type = when {
        cards.any { (_, n) -> n == 5 } -> HandType.FIVE_OF_A_KIND
        cards.any { (_, n) -> n == 4 } -> HandType.FOUR_OF_A_KIND
        cards.any { (_, n) -> n == 3 } && cards.any { (_, n) -> n == 2 } -> HandType.FULL_HOUSE
        cards.any { (_, n) -> n == 3 } -> HandType.THREE_OF_A_KIND
        cards.count { (_, n) -> n == 2 } == 2 -> HandType.TWO_PAIR
        cards.count { (_, n) -> n == 2 } == 1 -> HandType.ONE_PAIR
        else -> HandType.HIGH_CARD
    }

    while (jokers-- > 0) type = type.next

    return Hand(this, type, betString.toLong(), withJoker)
}

fun day7Part2(input: List<String>): Long {
    return input.map { it.parseHand(withJoker = true) }
        .sorted()
        .foldIndexed(0L) { index, acc, (_, _, bet) -> acc + bet * (index + 1) }
}
