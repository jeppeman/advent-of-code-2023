fun day16Part1(input: List<String>): Long {
    val graph = input.map { it.toCharArray() }.toTypedArray()
    return graph.dfs(0, -1, BeamDirection.RIGHT)
}

fun day16Part2(input: List<String>): Long {
    val graph = input.map { it.toCharArray() }.toTypedArray()
    var ans = 0L
    for (row in graph.indices) {
        ans = maxOf(ans, graph.dfs(row, -1, BeamDirection.RIGHT))
        ans = maxOf(ans, graph.dfs(row, graph.size, BeamDirection.LEFT))
    }
    for (col in graph[0].indices) {
        ans = maxOf(ans, graph.dfs(-1, col, BeamDirection.DOWN))
        ans = maxOf(ans, graph.dfs(graph[0].size, col, BeamDirection.UP))
    }

    return ans
}

private fun Array<CharArray>.dfs(
    row: Int,
    col: Int,
    direction: BeamDirection,
    visited: Array<Array<BooleanArray>> = Array(size) { Array(this[0].size) { BooleanArray(4) } },
): Long {
    if (inBounds(row, col) && visited[row][col][direction.ordinal]) return 0L

    var ans = if (inBounds(row, col) && visited[row][col].none { it }) 1L else 0L

    if (inBounds(row, col)) visited[row][col][direction.ordinal] = true

    for ((neighbourRow, neighbourCol, neighbourDir) in getNeighboursFor(row, col, direction)) {
        ans += dfs(neighbourRow, neighbourCol, neighbourDir, visited)
    }

    return ans
}

private enum class BeamDirection { LEFT, RIGHT, UP, DOWN }

private fun Array<CharArray>.getNeighboursFor(
    row: Int,
    col: Int,
    direction: BeamDirection,
): List<Beam> = when (direction) {
    BeamDirection.LEFT -> if (inBounds(row, col - 1)) when (this[row][col - 1]) {
        '|' -> listOf(BeamDirection.UP, BeamDirection.DOWN).map { Beam(row, col - 1, it) }
        '/' -> listOf(Beam(row, col - 1, BeamDirection.DOWN))
        '\\' -> listOf(Beam(row, col - 1, BeamDirection.UP))
        else -> listOf(Beam(row, col - 1, direction))
    } else emptyList()

    BeamDirection.RIGHT -> if (inBounds(row, col + 1)) when (this[row][col + 1]) {
        '|' -> listOf(BeamDirection.UP, BeamDirection.DOWN).map { Beam(row, col + 1, it) }
        '/' -> listOf(Beam(row, col + 1, BeamDirection.UP))
        '\\' -> listOf(Beam(row, col + 1, BeamDirection.DOWN))
        else -> listOf(Beam(row, col + 1, direction))
    } else emptyList()

    BeamDirection.UP -> if (inBounds(row - 1, col)) when (this[row - 1][col]) {
        '-' -> listOf(BeamDirection.LEFT, BeamDirection.RIGHT).map { Beam(row - 1, col, it) }
        '/' -> listOf(Beam(row - 1, col, BeamDirection.RIGHT))
        '\\' -> listOf(Beam(row - 1, col, BeamDirection.LEFT))
        else -> listOf(Beam(row - 1, col, direction))
    } else emptyList()

    BeamDirection.DOWN -> if (inBounds(row + 1, col)) when (this[row + 1][col]) {
        '-' -> listOf(BeamDirection.LEFT, BeamDirection.RIGHT).map { Beam(row + 1, col, it) }
        '/' -> listOf(Beam(row + 1, col, BeamDirection.LEFT))
        '\\' -> listOf(Beam(row + 1, col, BeamDirection.RIGHT))
        else -> listOf(Beam(row + 1, col, direction))
    } else emptyList()
}

private fun Array<CharArray>.inBounds(
    row: Int,
    col: Int,
): Boolean = row in 0..lastIndex && col in 0..this[0].lastIndex

private data class Beam(val row: Int, val col: Int, val direction: BeamDirection)
