import kotlin.math.abs

fun day11Part1(input: List<String>): Long {
    val expanded = input.expand(2L)
    return expanded.shortestPaths()
}

fun day11Part2(input: List<String>): Long {
    val expanded = input.expand(1000000)
    return expanded.shortestPaths()
}

private fun List<String>.expand(factor: Long = 1L): List<MutableList<Triple<Long, Long, Char>>> {
    val ans = mutableListOf<MutableList<Triple<Long, Long, Char>>>()
    var offset = 0L
    for (row in indices) {
        ans += this[row].mapIndexed { idx, c -> Triple(offset + row.toLong(), idx.toLong(), c) }.toMutableList()
        if (this[row].all { it == '.' }) offset += factor - 1
    }

    offset = 0L
    for (col in indices) {
        for (row in ans.indices) ans[row][col] = ans[row][col].copy(second = offset + col)
        if ((0..lastIndex).all { this[it][col] == '.' }) offset += factor - 1
    }

    return ans
}

private fun List<List<Triple<Long, Long, Char>>>.shortestPath(srcRow: Long, srcCol: Long): Long {
    var ans = 0L
    for (cols in this) {
        for ((row, col, char) in cols) {
            if (char == '#'){
                val dist = abs(srcRow - row) + abs(srcCol - col)
                ans += dist
            }
        }
    }

    return ans
}

private fun List<MutableList<Triple<Long, Long, Char>>>.shortestPaths(): Long {
    var ans = 0L
    for (i in indices) {
        for (j in this[i].indices) {
            val (row, col, char) = this[i][j]
            if (char != '.') {
                ans += shortestPath(row, col)
                this[i][j] = this[i][j].copy(third = '.')
            }
        }
    }

    return ans
}
