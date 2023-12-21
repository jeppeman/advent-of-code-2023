import java.util.*
import kotlin.math.abs

fun day17Part1(
    input: List<String>
): Long = input.solve { _, _, _, streak -> streak <= 3 }

fun day17Part2(
    input: List<String>
): Long = input.solve { prevDirection, newDirection, prevStreak, newStreak ->
    newStreak in 4..10
            || (newStreak <= 10 && (prevDirection == -1 || prevDirection == newDirection))
            || (prevStreak >= 4 && newStreak == 1)
}

private fun List<String>.solve(
    isValid: (prevDirection: Int, newDirection: Int, prevStreak: Int, newStreak: Int) -> Boolean
): Long {
    val graph = map { row -> row.map { it.digitToInt() }.toIntArray() }.toTypedArray()
    val cells = hashMapOf<CellInfo, Long>()
    val queue = TreeSet<CellInfoWithHeatLoss> { (row1, col1, dir1, streak1, hl1), (row2, col2, dir2, streak2, hl2) ->
        hl1.compareTo(hl2).takeIf { it != 0 }
            ?: row1.compareTo(row2).takeIf { it != 0 }
            ?: col1.compareTo(col2).takeIf { it != 0 }
            ?: dir1.compareTo(dir2).takeIf { it != 0 }
            ?: streak1.compareTo(streak2)
    }
    queue += CellInfoWithHeatLoss(0, 0, -1, -1, 0L)
    while (queue.isNotEmpty()) {
        val node = queue.first().apply { queue.remove(this) }
        if (node.withoutHeatLoss() in cells) continue

        cells[node.withoutHeatLoss()] = node.heatLoss
        for ((direction, delta) in directions.withIndex()) {
            val (dR, dC) = delta
            val newR = node.row + dR
            val newC = node.col + dC

            val inBounds = graph.inBounds(newR, newC)
            val isBackwards = node.direction >= 0
                    && (abs(directions[direction].row - directions[node.direction].row) == 2
                    || abs(directions[direction].col - directions[node.direction].col) == 2)
            val newStreak = if (direction == node.direction) node.streak + 1 else 1
            if (inBounds && !isBackwards && isValid(node.direction, direction, node.streak, newStreak)) {
                val newDist = node.heatLoss + graph[newR][newC]
                val newNode = CellInfoWithHeatLoss(newR, newC, direction, newStreak, newDist)
                queue += newNode
            }
        }
    }

    return cells
        .filter { (info) -> info.row == graph.lastIndex && info.col == graph[0].lastIndex }
        .minBy { (_, heatLoss) -> heatLoss }
        .value
}

private data class CellInfo(
    val row: Int,
    val col: Int,
    val direction: Int,
    val streak: Int,
)

private data class CellInfoWithHeatLoss(
    val row: Int,
    val col: Int,
    val direction: Int,
    val streak: Int,
    val heatLoss: Long,
)

private fun CellInfoWithHeatLoss.withoutHeatLoss(): CellInfo = CellInfo(row, col, direction, streak)

private val directions = listOf(listOf(-1, 0), listOf(1, 0), listOf(0, -1), listOf(0, 1))

private val List<Int>.row: Int get() = this[0]
private val List<Int>.col: Int get() = this[1]

private fun Array<IntArray>.inBounds(
    row: Int,
    col: Int
): Boolean = row in 0..lastIndex && col >= 0 && col in 0..this[0].lastIndex