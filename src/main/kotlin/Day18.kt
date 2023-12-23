fun day18Part1(input: List<String>): Long {
    var row = 0L
    var col = 0L
    var perimeter = 0
    val polygon = mutableListOf<LongArray>()
    for (line in input) {
        val (direction, distance) = line.split(" ")
        when (direction) {
            "L" -> col -= distance.toInt()
            "R" -> col += distance.toInt()
            "D" -> row += distance.toInt()
            "U" -> row -= distance.toInt()
        }

        polygon += longArrayOf(row, col)
        perimeter += distance.toInt()
    }

    return polygon.area() + (perimeter / 2) + 1
}

fun day18Part2(input: List<String>): Long {
    var row = 0L
    var col = 0L
    var perimeter = 0
    val polygon = mutableListOf<LongArray>()
    for (line in input) {
        val (_, _, right) = line.split(" ")
        val hex = right.replace("(", "").replace(")", "")
        val distance = hex.substring(1 until hex.lastIndex).toLong(16)
        when (hex[hex.lastIndex].digitToInt()) {
            0 -> col += distance
            1 -> row += distance
            2 -> col -= distance
            3 -> row -= distance
        }

        polygon += longArrayOf(row, col)
        perimeter += distance.toInt()
    }

    return polygon.area() + (perimeter / 2) + 1
}

private fun List<LongArray>.area(): Long {
    var shoelace = 0L
    for (i in lastIndex downTo 1) {
        val (x0, y0) = this[i]
        val (x1, y1) = this[i - 1]
        shoelace += x0 * y1 - x1 * y0
    }
    return shoelace / 2L
}
