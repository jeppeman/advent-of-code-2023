fun day3Part1(input: List<String>): Long {
    var sum = 0L

    for (row in input.indices) {
        val line = input[row]
        var col = 0
        while (col < line.length) {
            val char = line[col]
            if (char.isDigit()) {
                val numberBuilder = StringBuilder()
                while (col < line.length && line[col].isDigit()) numberBuilder.append(line[col++])
                val number = numberBuilder.toString()
                if (number.isPartNumber(input, row, col - number.length)) sum += number.toInt()
            } else {
                col++
            }
        }
    }

    return sum
}

private fun String.isPartNumber(
    input: List<String>,
    row: Int,
    col: Int
): Boolean = isTopAdjacentToSymbol(input, row, col)
    || isLeftAdjacentToSymbol(input, row, col)
    || isRightAdjacentToSymbol(input, row, col)
    || isBottomAdjacentToSymbol(input, row, col)

private fun String.isTopAdjacentToSymbol(
    input: List<String>,
    row: Int,
    col: Int
): Boolean {
    if (row == 0) return false

    for (i in maxOf(0, col - 1)..minOf(input[row].lastIndex, col + length)) {
        val char = input[row - 1][i]
        if (char.isPart()) return true
    }

    return false
}

private fun isLeftAdjacentToSymbol(
    input: List<String>,
    row: Int,
    col: Int
): Boolean {
    if (col <= 0) return false
    return input[row][col - 1].isPart()
}

private fun String.isRightAdjacentToSymbol(
    input: List<String>,
    row: Int,
    col: Int
): Boolean {
    if (col + length >= input[row].lastIndex) return false
    return input[row][col + length].isPart()
}

private fun String.isBottomAdjacentToSymbol(
    input: List<String>,
    row: Int,
    col: Int
): Boolean {
    if (row >= input.lastIndex) return false

    for (i in maxOf(0, col - 1)..minOf(input[row].lastIndex, col + length)) {
        val char = input[row + 1][i]
        if (char.isPart()) return true
    }

    return false
}

private fun Char.isPart(): Boolean = !isDigit() && this != '.'

fun day3Part2(input: List<String>): Long {
    var sum = 0L

    for (row in input.indices) {
        val line = input[row]
        for (col in line.indices) sum += gearRatio(input, row, col)
    }

    return sum
}

private fun gearRatio(
    input: List<String>,
    row: Int,
    col: Int
): Long = if (input[row][col] != '*') {
    0L
} else {
    val top = topAdjacentNumbers(input, row, col)
    val left = leftAdjacentNumbers(input, row, col)
    val right = rightAdjacentNumbers(input, row, col)
    val bottom = bottomAdjacentNumbers(input, row, col)
    if (top.size + left.size + right.size + bottom.size == 2) {
        top.product() * left.product() * right.product() * bottom.product()
    } else {
        0L
    }
}

private fun topAdjacentNumbers(
    input: List<String>,
    row: Int,
    col: Int
): List<Long> {
    if (row == 0) return emptyList()

    val adjacent = mutableListOf<Long>()

    var i = maxOf(0, col - 1)
    while (i >= 0 && input[row - 1][i].isDigit()) i--
    i = maxOf(0, i)
    val max = minOf(input[row].lastIndex, col + 1)
    while (i <= max) {
        val char = input[row - 1][i]
        if (char.isDigit()) {
            val numberBuilder = StringBuilder()
            while (i < input[row + 1].length && input[row - 1][i].isDigit()) numberBuilder.append(input[row - 1][i++])
            val number = numberBuilder.toString().toLong()
            adjacent += number
        } else {
            i++
        }
    }

    return adjacent
}

private fun leftAdjacentNumbers(
    input: List<String>,
    row: Int,
    col: Int
): List<Long> {
    if (col <= 0) return emptyList()
    var i = col - 1
    val numberBuilder = StringBuilder()
    while (i >= 0 && input[row][i].isDigit()) numberBuilder.insert(0, input[row][i--])

    return if (numberBuilder.isEmpty()) emptyList() else listOf(numberBuilder.toString().toLong())
}

private fun rightAdjacentNumbers(
    input: List<String>,
    row: Int,
    col: Int
): List<Long> {
    if (col >= input[row].lastIndex) return emptyList()
    var i = col + 1
    val numberBuilder = StringBuilder()
    while (i < input[row].length && input[row][i].isDigit()) numberBuilder.append(input[row][i++])

    return if (numberBuilder.isEmpty()) emptyList() else listOf(numberBuilder.toString().toLong())

}

private fun bottomAdjacentNumbers(
    input: List<String>,
    row: Int,
    col: Int
): List<Long> {
    if (row >= input.lastIndex) return emptyList()

    val adjacent = mutableListOf<Long>()

    var i = maxOf(0, col - 1)
    while (i >= 0 && input[row + 1][i].isDigit()) i--
    i = maxOf(0, i)
    val max = minOf(input[row].lastIndex, col + 1)
    while (i <= max) {
        val char = input[row + 1][i]
        if (char.isDigit()) {
            val numberBuilder = StringBuilder()
            while (i < input[row + 1].length && input[row + 1][i].isDigit()) numberBuilder.append(input[row + 1][i++])
            val number = numberBuilder.toString().toLong()
            adjacent += number
        } else {
            i++
        }
    }

    return adjacent
}

private fun List<Long>.product(): Long = fold(1L) { acc, curr -> acc * curr }