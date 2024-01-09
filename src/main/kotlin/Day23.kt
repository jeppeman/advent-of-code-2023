import java.util.Stack

fun day23Part1(input: List<String>): Long {
    val graph = input.map { it.toCharArray() }.toTypedArray()
    return graph.dfs(0, 1)
}

fun day23Part2(input: List<String>): Long {
    val graph = input.map { it.toCharArray() }.toTypedArray().findJunctions()
    return graph.dfs(0 to 1, input.lastIndex to input[0].lastIndex - 1)
}

private fun Array<CharArray>.dfs(
    row: Int,
    col: Int,
    recStack: Array<BooleanArray> = Array(size) { BooleanArray(this[0].size) },
): Long {
    if (row < 0 || row > lastIndex || col < 0 || col > this[0].lastIndex) return Long.MIN_VALUE
    if (recStack[row][col]) return Long.MIN_VALUE
    if (this[row][col] == '#') return Long.MIN_VALUE
    if (row == lastIndex && this[row][col] == '.') return 0L

    recStack[row][col] = true

    return 1 + when (this[row][col]) {
        '^' -> dfs(row - 1, col, recStack)
        'v' -> dfs(row + 1, col, recStack)
        '<' -> dfs(row, col - 1, recStack)
        '>' -> dfs(row, col + 1, recStack)
        else -> {
            val up = dfs(row - 1, col, recStack)
            val down = dfs(row + 1, col, recStack)
            val left = dfs(row, col - 1, recStack)
            val right = dfs(row, col + 1, recStack)
            maxOf(up, down, left, right)
        }
    }.apply { recStack[row][col] = false }
}

private fun Map<Pair<Int, Int>, Map<Pair<Int, Int>, Int>>.dfs(
    pos: Pair<Int, Int>,
    endPos: Pair<Int, Int>,
    recStack: HashSet<Pair<Int, Int>> = hashSetOf(),
): Long {
    if (pos == endPos) return 0L

    var ans = Long.MIN_VALUE
    recStack += pos

    for ((nextPos) in this[pos].orEmpty()) {
        if (nextPos in recStack) continue

        ans = maxOf(ans, this[pos].orEmpty()[nextPos]!! + dfs(nextPos, endPos, recStack))
    }

    recStack -= pos

    return ans
}

private fun Array<CharArray>.findJunctions(): Map<Pair<Int, Int>, Map<Pair<Int, Int>, Int>> {
    val junctions = mutableSetOf(0 to 1, lastIndex to this[0].lastIndex - 1)

    for (row in indices) {
        for (col in this[row].indices) {
            if (this[row][col] == '#') continue

            var neighbours = 0
            for ((dR, dC) in directions) {
                val nR = row + dR
                val nC = col + dC
                if (nR < 0 || nR > lastIndex || nC < 0 || nC > this[0].lastIndex) continue
                if (this[nR][nC] == '#') continue

                if (++neighbours >= 3) junctions += row to col
            }
        }
    }

    return populateJunctionDistances(junctions)
}

private fun Array<CharArray>.populateJunctionDistances(
    junctions: Set<Pair<Int, Int>>
): Map<Pair<Int, Int>, Map<Pair<Int, Int>, Int>> {
    val ans = hashMapOf<Pair<Int, Int>, HashMap<Pair<Int, Int>, Int>>()
    for (junction in junctions) {
        val (startRow, startCol) = junction
        val visited = hashSetOf(junction)
        val stack = Stack<IntArray>()
        stack.push(intArrayOf(startRow, startCol, 0))

        while (stack.isNotEmpty()) {
            val (row, col, distance) = stack.pop()

            if (row to col in junctions && distance != 0) {
                ans.computeIfAbsent(startRow to startCol) { hashMapOf() }[row to col] = distance
                continue
            }

            for ((dR, dC) in directions) {
                val nR = row + dR
                val nC = col + dC
                if (nR < 0 || nR > lastIndex || nC < 0 || nC > this[0].lastIndex) continue
                if (nR to nC in visited || this[nR][nC] == '#') continue

                visited += nR to nC
                stack.push(intArrayOf(nR, nC, distance + 1))
            }
        }
    }

    return ans
}

private val directions = arrayOf(intArrayOf(0, 1), intArrayOf(0, -1), intArrayOf(1, 0), intArrayOf(-1, 0))
