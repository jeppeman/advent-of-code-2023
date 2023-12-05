import kotlin.math.pow

fun day4Part1(input: List<String>): Long {
    var score = 0L
    for (line in input) {
        val (winningNumbers, myNumbers) = line.parseAsCard()
        var scoreExponent = -1
        for (number in myNumbers) if (number in winningNumbers) scoreExponent++

        score += if (scoreExponent == -1) 0L else 2.0.pow(scoreExponent).toLong()
    }

    return score
}

private data class Card(val winningNumbers: Set<String>, val myNumbers: Set<String>)

private fun String.parseAsCard(): Card {
    val (_, cardPart) = split(":")
    val (winningPart, myPart) = cardPart.split("|")
    val winningNumbers = winningPart.split(" ")
        .filterNot { it.isBlank() }
        .toSet()

    val myNumbers = myPart.split(" ")
        .filterNot { it.isBlank() }
        .toSet()

    return Card(winningNumbers, myNumbers)
}

fun day4Part2(input: List<String>): Long {
    var nCards = 0L
    val cards = input.map { CardWithAmount(1, it.parseAsCard()) }
    for (i in cards.indices) {
        val (amount, card) = cards[i]
        val (winningNumbers, myNumbers) = card
        var nMatches = 0
        for (number in myNumbers) if (number in winningNumbers) cards[i + ++nMatches].amount += amount

        nCards += amount
    }

    return nCards
}

private data class CardWithAmount(var amount: Int, val card: Card)
