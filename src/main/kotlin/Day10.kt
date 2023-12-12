fun day10Part1(input: List<String>): Long {
    val graph = input.map { it.toCharArray() }.toTypedArray()
    val start = graph.findStart()
    val queue = ArrayDeque<IntArray>()
    queue.add(start)
    var distance = -1L
    while (queue.isNotEmpty()) {
        repeat(queue.size) {
            val cell = queue.removeFirst()
            val (row, col) = cell
            for (next in cell.unvisitedNeighboursIn(graph)) queue.add(next)
            graph[row][col] = '#'
        }
        distance++
    }

    return distance
}

private fun IntArray.unvisitedNeighboursIn(
    graph: Array<CharArray>
): List<IntArray> {
    val (row, col) = this
    if (graph[row][col] !in symbolDirections) return emptyList()

    val isStart = graph[row][col] == 'S'

    return listOfNotNull(
        intArrayOf(row, col + 1).takeIf {
            it inBoundsOf graph && graph[row][col + 1] in symbolDirections
                    && Direction.WEST in symbolDirections[graph[row][col + 1]]!!
                    && (isStart || Direction.EAST in symbolDirections[graph[row][col]]!!)
        },
        intArrayOf(row, col - 1).takeIf {
            it inBoundsOf graph && graph[row][col - 1] in symbolDirections
                    && Direction.EAST in symbolDirections[graph[row][col - 1]]!!
                    && (isStart || Direction.WEST in symbolDirections[graph[row][col]]!!)
        },
        intArrayOf(row + 1, col).takeIf {
            it inBoundsOf graph && graph[row + 1][col] in symbolDirections
                    && Direction.NORTH in symbolDirections[graph[row + 1][col]]!!
                    && (isStart || Direction.SOUTH in symbolDirections[graph[row][col]]!!)
        },
        intArrayOf(row - 1, col).takeIf {
            it inBoundsOf graph && graph[row - 1][col] in symbolDirections
                    && Direction.SOUTH in symbolDirections[graph[row - 1][col]]!!
                    && (isStart || Direction.NORTH in symbolDirections[graph[row][col]]!!)
        },
    )
}

private infix fun IntArray.inBoundsOf(
    graph: Array<CharArray>
): Boolean = this[0] >= 0 && this[0] <= graph.lastIndex && this[1] >= 0 && this[1] <= graph[0].lastIndex

private enum class Direction { WEST, EAST, NORTH, SOUTH }

private val symbolDirections = mapOf(
    '|' to setOf(Direction.NORTH, Direction.SOUTH),
    '-' to setOf(Direction.EAST, Direction.WEST),
    'L' to setOf(Direction.NORTH, Direction.EAST),
    'J' to setOf(Direction.NORTH, Direction.WEST),
    '7' to setOf(Direction.SOUTH, Direction.WEST),
    'F' to setOf(Direction.SOUTH, Direction.EAST),
    '.' to emptySet(),
    'S' to emptySet(),
)

private fun Array<CharArray>.findStart(): IntArray {
    for (row in indices) {
        for (col in this[row].indices) {
            if (this[row][col] == 'S') return intArrayOf(row, col)
        }
    }

    throw IllegalArgumentException("No start symbol found")
}

fun day10Part2(input: List<String>): Long {
    val graph = input.map { it.toCharArray() }.toTypedArray()
    graph.convertStartPos()
    val expandedGraph = Array(input.size * 3) { CharArray(input[0].length * 3) { '.' } }

    for (row in graph.indices) {
        for (col in graph[row].indices) {
            when (graph[row][col]) {
                '|' -> {
                    expandedGraph[3 * row + 0][3 * col + 1] = '*'
                    expandedGraph[3 * row + 1][3 * col + 1] = '*'
                    expandedGraph[3 * row + 2][3 * col + 1] = '*'
                }
                '-' -> {
                    expandedGraph[3 * row + 1][3 * col + 0] = '*'
                    expandedGraph[3 * row + 1][3 * col + 1] = '*'
                    expandedGraph[3 * row + 1][3 * col + 2] = '*'
                }
                'L' -> {
                    expandedGraph[3 * row + 0][3 * col + 1] = '*'
                    expandedGraph[3 * row + 1][3 * col + 1] = '*'
                    expandedGraph[3 * row + 1][3 * col + 2] = '*'
                }
                'J' -> {
                    expandedGraph[3 * row + 0][3 * col + 1] = '*'
                    expandedGraph[3 * row + 1][3 * col + 1] = '*'
                    expandedGraph[3 * row + 1][3 * col + 0] = '*'
                }
                '7' -> {
                    expandedGraph[3 * row + 1][3 * col + 0] = '*'
                    expandedGraph[3 * row + 1][3 * col + 1] = '*'
                    expandedGraph[3 * row + 2][3 * col + 1] = '*'
                }
                'F' -> {
                    expandedGraph[3 * row + 1][3 * col + 2] = '*'
                    expandedGraph[3 * row + 1][3 * col + 1] = '*'
                    expandedGraph[3 * row + 2][3 * col + 1] = '*'
                }
            }
        }
    }

    val queue = ArrayDeque<IntArray>()

    for (row in expandedGraph.indices) {
        for (col in expandedGraph[row].indices) {
            val isEdge = row == 0 || col == 0 || row == expandedGraph.lastIndex || col == expandedGraph[0].lastIndex
            if (isEdge && expandedGraph[row][col] == '.') {
                queue += intArrayOf(row, col)
                expandedGraph[row][col] = '#'
            }
        }
    }

   while (queue.isNotEmpty()) {
        val (row, col) = queue.removeFirst()
        graph[row / 3][col / 3] = '#'
        for ((dR, dC) in directions) {
            val nextRow = row + dR
            val nextCol = col + dC
            val nextNeighbour = intArrayOf(nextRow, nextCol)
            if (nextNeighbour inBoundsOf expandedGraph
                && expandedGraph[nextRow][nextCol] != '*'
                && expandedGraph[nextRow][nextCol] != '#') {
                expandedGraph[nextRow][nextCol] = '#'
                queue += nextNeighbour
            }
        }
    }

    var ans = 0L

    for (row in graph.indices) for (col in graph[row].indices) if (graph[row][col] != '#') ans++

    return ans
}

private fun Array<CharArray>.convertStartPos() {
    val (row, col) = findStart()

    val directions = setOfNotNull(
        intArrayOf(row, col + 1).takeIf {
            it inBoundsOf this && Direction.WEST in symbolDirections[this[row][col + 1]].orEmpty()
        }?.let { Direction.EAST },
        intArrayOf(row, col - 1).takeIf {
            it inBoundsOf this && Direction.EAST in symbolDirections[this[row][col - 1]].orEmpty()
        }?.let { Direction.WEST },
        intArrayOf(row + 1, col).takeIf {
            it inBoundsOf this && Direction.NORTH in symbolDirections[this[row + 1][col]].orEmpty()
        }?.let { Direction.SOUTH },
        intArrayOf(row - 1, col).takeIf {
            it inBoundsOf this && Direction.SOUTH in symbolDirections[this[row - 1][col]].orEmpty()
        }?.let { Direction.NORTH },
    )

    this[row][col] = symbolDirections.filter { (_, v) -> (v - directions).isEmpty() }.map { (k) -> k }.first()
}

private val directions = arrayOf(intArrayOf(0, 1), intArrayOf(0, -1), intArrayOf(1, 0), intArrayOf(-1, 0))