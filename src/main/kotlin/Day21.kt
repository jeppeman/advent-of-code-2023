import kotlin.math.pow

fun day21Part1(input: List<String>): Long {
    val graph = input.map { it.toCharArray() }.toTypedArray()
    val (startRow, startCol) = graph.findStart()
    return graph.solve(startRow, startCol, 64)
}

fun day21Part2(input: List<String>): Long {
    val graph = input.map { it.toCharArray() }.toTypedArray()
    val (startRow, startCol) = graph.findStart()
    val size = graph.size
    val gridWidth = 26501365 / size - 1

    val nOddGrids = (gridWidth / 2 * 2 + 1).toDouble().pow(2).toLong()
    val nEvenGrids = ((gridWidth + 1) / 2 * 2).toDouble().pow(2).toLong()

    val nOddPoints = graph.solve(startRow, startCol, size * 2 + 1)
    val nEvenPoints = graph.solve(startRow, startCol, size * 2)

    val cornerTop = graph.solve(size - 1, startCol, size - 1)
    val cornerRight = graph.solve(startRow, 0, size - 1)
    val cornerBottom = graph.solve(0, startCol, size - 1)
    val cornerLeft = graph.solve(startRow, size - 1, size - 1)

    val smallTopRight = graph.solve(size - 1, 0, size / 2 - 1)
    val smallTopLeft = graph.solve(size - 1, size - 1, size / 2 - 1)
    val smallBottomRight = graph.solve(0, 0, size / 2 - 1)
    val smallBottomLeft = graph.solve(0, size - 1, size / 2 - 1)

    val largeTopRight = graph.solve(size - 1, 0, size * 3 / 2 - 1)
    val largeTopLeft = graph.solve(size - 1, size - 1, size * 3 / 2 - 1)
    val largeBottomRight = graph.solve(0, 0, size * 3 / 2 - 1)
    val largeBottomLeft = graph.solve(0, size - 1, size * 3 / 2 - 1)

    return nOddGrids * nOddPoints +
            nEvenGrids * nEvenPoints +
            cornerTop + cornerRight + cornerBottom + cornerLeft +
            (gridWidth + 1) * (smallTopRight + smallTopLeft + smallBottomRight + smallBottomLeft) +
            gridWidth * (largeTopRight + largeTopLeft + largeBottomRight + largeBottomLeft)
}

private fun Array<CharArray>.solve(startRow: Int, startCol: Int, targetSteps: Int): Long {
    val startPos = startRow to startCol
    val visited = hashSetOf(startPos)
    val queue = ArrayDeque<IntArray>()
    queue += intArrayOf(startRow, startCol, targetSteps)

    var ans = 0L
    while (queue.isNotEmpty()) {
        val (row, col, steps) = queue.removeFirst()
        if (steps % 2 == 0) ans++
        if (steps == 0) continue
        for ((dR, dC) in directions) {
            val newR = row + dR
            val newC = col + dC
            val newPos = newR to newC
            if (newR < 0 || newR > lastIndex || newC < 0 || newC > this[0].lastIndex) continue
            if (newPos in visited || this[newR][newC] == '#') continue
            visited += newPos
            queue += intArrayOf(newR, newC, steps - 1)
        }
    }

    return ans
}

private val directions = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))

private fun Array<CharArray>.findStart(): Pair<Int, Int> {
    for (row in indices) {
        for (col in this[row].indices) {
            if (this[row][col] == 'S') return row to col
        }
    }

    throw IllegalStateException("No start pos found")
}
