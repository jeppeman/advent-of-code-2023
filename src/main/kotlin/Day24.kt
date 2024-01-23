import kotlin.math.roundToLong

fun day24Part1(input: List<String>, min: Long, max: Long): Long {
    val hailstones = input.parseHailstones()
    var ans = 0L
    for ((i, hailstone) in hailstones.withIndex()) {
        for (j in i + 1 until hailstones.size) {
            val other = hailstones[j]
            val (crossX, crossY) = hailstone.equation.crossingWith(other.equation * -1f)
            if (crossX >= min && crossX <= max && crossY >= min && crossY <= max
                && hailstone.canGetTo(crossX, crossY) && other.canGetTo(crossX, crossY)
            ) ans++
        }
    }
    return ans
}

fun day24Part2(input: List<String>): Long {
    val hailstones = input.parseHailstones()
    val (rockXPos, rockYPos, rockXSpeed) = hailstones.take(5).windowed(2).map { (h1, h2) ->
        doubleArrayOf(
            h2.y.speed.toDouble() - h1.y.speed,
            h1.x.speed.toDouble() - h2.x.speed,
            h1.y.pos.toDouble() - h2.y.pos,
            h2.x.pos.toDouble() - h1.x.pos,
            ((h1.y.pos * h1.x.speed) + (-h1.x.pos * h1.y.speed) + (-h2.y.pos * h2.x.speed) + (h2.x.pos * h2.y.speed))
                .toDouble()
        )
    }.gaussianElimination().map { it.roundToLong() }

    val (rockZPos) = hailstones.take(3).windowed(2).map { (h1, h2) ->
        doubleArrayOf(
            h1.x.speed.toDouble() - h2.x.speed,
            h2.x.pos.toDouble() - h1.x.pos,

            ((h1.z.pos * h1.x.speed)
                    - (h1.x.pos * h1.z.speed)
                    - (h2.z.pos * h2.x.speed)
                    + (h2.x.pos * h2.z.speed)
                    - ((h2.z.speed - h1.z.speed) * rockXPos.toDouble())
                    - ((h1.z.pos - h2.z.pos) * rockXSpeed))
        )
    }.gaussianElimination().map { it.roundToLong() }

    return rockXPos + rockYPos + rockZPos
}

private data class Hailstone(val x: PosAndSpeed, val y: PosAndSpeed, val z: PosAndSpeed)
private data class PosAndSpeed(val pos: Long, val speed: Long)
private data class Equation(val ax: Float, val by: Float, val c: Float)

private operator fun Equation.times(num: Float): Equation = copy(by = by * num, ax = ax * num, c = c * num)

private val Hailstone.equation: Equation
    get() = Equation(
        ax = y.speed.toFloat() / x.speed.toFloat(),
        by = 1f,
        c = y.pos + (y.speed.toFloat() / x.speed.toFloat()) * -x.pos
    )

private fun Equation.crossingWith(other: Equation): Pair<Float, Float> {
    val x = -(c + other.c) / (ax + other.ax)
    val y = x * this.ax + c
    return x to y
}

private fun Hailstone.canGetTo(x: Float, y: Float): Boolean {
    return (x == this.x.pos.toFloat() && y == this.y.pos.toFloat())
            || (((x > this.x.pos && this.x.speed > 0) || (x < this.x.pos && this.x.speed < 0))
            && (y == this.y.pos.toFloat() || (y > this.y.pos && this.y.speed > 0) || (y < this.y.pos && this.y.speed < 0)))
}

private fun List<String>.parseHailstones(): List<Hailstone> = map { line ->
    val (positions, speeds) = line.split("@").map { part -> part.split(",").map { it.trim().toLong() } }
    val (x, y, z) = positions
    val (vx, vy, vz) = speeds
    Hailstone(x = PosAndSpeed(x, vx), y = PosAndSpeed(y, vy), z = PosAndSpeed(z, vz))
}

// https://en.wikipedia.org/wiki/Gaussian_elimination
private fun List<DoubleArray>.gaussianElimination(): DoubleArray {
    for (row in indices) {
        val pivot = this[row][row]
        for (col in this[row].indices) {
            this[row][col] /= pivot
        }

        for (otherRow in indices) {
            if (row == otherRow) continue

            val factor = this[otherRow][row]
            for (col in this[otherRow].indices) {
                this[otherRow][col] -= factor * this[row][col]
            }
        }
    }

    return DoubleArray(size) { i -> this[i].last() }
}