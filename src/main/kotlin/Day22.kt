import kotlin.math.abs

fun day22Part1(input: List<String>): Long {
    val (bricks, children, parents) = input.parseBricks()

    var ans = 0L
    for (i in bricks.indices) {
        if (children[i].withIndex().all { (j, isChild) -> !isChild || parents[j].count { it } >= 2 }) {
            ans++
        }
    }

    return ans
}

fun day22Part2(input: List<String>): Long {
    val (bricks, children, parents) = input.parseBricks()

    fun falls(i: Int, fallen: BooleanArray = BooleanArray(bricks.size)): Long {
        fallen[i] = true

        val falling = children[i]
            .withIndex()
            .filter { (j, isChild) -> isChild && parents[j].withIndex().count { (k, p) -> !fallen[k] && p } == 0 }
            .map { (idx) -> idx }

        return (falling.size + falling.sumOf { falls(it, fallen) })
    }

    return bricks.withIndex().sumOf { (i) -> falls(i) }
}

private data class Point(val x: Int, val y: Int, val z: Int)

private data class Brick(val name: Char, val end1: Point, val end2: Point)

private infix fun Brick.collidesWith(other: Brick): Boolean {
    val (x1, y1) = end1
    val (x2, y2) = end2
    val (x3, y3) = other.end1
    val (x4, y4) = other.end2

    return maxOf(x1, x3) <= minOf(x2, x4) && maxOf(y1, y3) <= minOf(y2, y4)
}

private fun List<String>.parseBricks(): Triple<List<Brick>, Array<BooleanArray>, Array<BooleanArray>> {
    val bricks = mutableListOf<Brick>()
    for (line in this) {
        val (left, right) = line.split("~")
        val (x1, y1, z1) = left.split(",").map { it.toInt() }
        val (x2, y2, z2) = right.split(",").map { it.toInt() }
        val end1 = Point(x1, y1, z1)
        val end2 = Point(x2, y2, z2)
        val brick = Brick('0', end1, end2)
        bricks += brick
    }

    bricks.sortBy { (_, end1) -> end1.z }

    for ((i, upper) in bricks.withIndex()) {
        var lowerZ = 1
        val zLength = abs(upper.end2.z - upper.end1.z)
        for (j in 0 until i) {
            val lower = bricks[j]
            if (upper collidesWith lower && lower.end2.z + 1 > lowerZ) {
                lowerZ = lower.end2.z + 1
            }
        }
        bricks[i] = upper.copy(
            name = 'A' + i,
            end1 = upper.end1.copy(z = lowerZ),
            end2 = upper.end2.copy(z = lowerZ + zLength),
        )
    }

    bricks.sortBy { (_, end1) -> end1.z }

    val children = Array(bricks.size) { BooleanArray(bricks.size) }
    val parents = Array(bricks.size) { BooleanArray(bricks.size) }

    for ((j, child) in bricks.withIndex()) {
        for (i in 0 until j) {
            val parent = bricks[i]
            if (parent collidesWith child && child.end1.z == parent.end2.z + 1) {
                children[i][j] = true
                parents[j][i] = true
            }
        }
    }

    return Triple(bricks, children, parents)
}