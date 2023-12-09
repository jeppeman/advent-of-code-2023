fun day9Part1(input: List<String>): Long = input.sumOf { it.nextValue() }

private fun String.extractSequences(): MutableList<MutableList<Long>> {
    val history = split(" ").map { it.toLong() }.toMutableList()
    val sequences = mutableListOf(history)
    var prev = history
    var next = mutableListOf<Long>()
    while (sequences.last().any { it != 0L }) {
        for (i in 1 until prev.size) {
            next += prev[i] - prev[i - 1]
        }
        sequences += next
        prev = next
        next = mutableListOf()
    }

    return sequences
}

private fun String.nextValue(): Long {
    val sequences = extractSequences()
    for (i in sequences.lastIndex downTo 1) {
        sequences[i - 1] += sequences[i - 1][sequences[i - 1].lastIndex] + sequences[i][sequences[i].lastIndex]
    }

    return sequences[0][sequences[0].lastIndex]
}

fun day9Part2(input: List<String>): Long = input.sumOf { it.prevValue() }

private fun String.prevValue(): Long {
    val sequences = extractSequences()
    for (i in sequences.lastIndex downTo 1) {
        sequences[i - 1].add(0, sequences[i - 1][0] - sequences[i][0])
    }

    return sequences[0][0]
}