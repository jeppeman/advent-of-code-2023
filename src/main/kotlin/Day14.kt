fun day14Part1(input: List<String>): Long {
    val platform = input.map { it.toCharArray() }.toTypedArray()

    platform.rollNorth()

    return platform.load
}

fun day14Part2(input: List<String>): Long {
    val platform = input.map { it.toCharArray() }.toTypedArray()

    val cache = hashMapOf<String, Array<Pair<String, Int>?>>()

    var platformState = input.joinToString("")

    val cycleLength: Int
    var iteration = 0
    while (true) {
        val (state, prevIteration) = platform.roll(platformState, iteration, cache)
        platformState = state
        if (prevIteration != iteration) {
            cycleLength = iteration - prevIteration
            break
        }
        iteration++
    }

    var targetIteration = 1_000_000_000 % cycleLength
    while (targetIteration < iteration) targetIteration += cycleLength

    platformState = input.joinToString("")
    for (i in 0 until targetIteration) {
        val (newState) = platform.roll(platformState, i, cache)
        platformState = newState
    }

    for (i in platformState.indices) {
        val row = i / platform[0].size
        val col = i % platform[0].size
        platform[row][col] = platformState[i]
    }

    return platform.load
}

private fun HashMap<String, Array<Pair<String, Int>?>>.query(
    key: String
): Array<Pair<String, Int>?> = computeIfAbsent(key) { Array(4) { null } }

private fun Array<CharArray>.roll(
    state: String,
    iteration: Int,
    cache: HashMap<String, Array<Pair<String, Int>?>>
): Pair<String, Int> {
    val (north) = (cache.query(state)[RollDirection.NORTH.ordinal] ?: rollNorth().run {
        cache.query(state)[RollDirection.NORTH.ordinal] = this to iteration
        this to iteration
    })

    val (west) = cache.query(north)[RollDirection.WEST.ordinal] ?: rollWest().run {
        cache.query(north)[RollDirection.WEST.ordinal] = this to iteration
        this to iteration
    }

    val (south) = cache.query(west)[RollDirection.SOUTH.ordinal] ?: rollSouth().run {
        cache.query(west)[RollDirection.SOUTH.ordinal] = this to iteration
        this to iteration
    }

    val (east, prevEastIteration) = cache.query(south)[RollDirection.EAST.ordinal] ?: rollEast().run {
        cache.query(south)[RollDirection.EAST.ordinal] = this to iteration
        this to iteration
    }
    return east to prevEastIteration
}

private fun Array<CharArray>.rollNorth(): String {
    for (row in 1..lastIndex) {
        for (col in this[row].indices) {
            if (this[row][col] == 'O') {
                var i = row - 1
                while (i >= 0 && this[i][col] == '.') i--
                i++
                if (i < row) {
                    this[i][col] = 'O'
                    this[row][col] = '.'
                }
            }
        }
    }

    return hash
}

private fun Array<CharArray>.rollWest(): String {
    for (row in 0..lastIndex) {
        for (col in 1..this[row].lastIndex) {
            if (this[row][col] == 'O') {
                var i = col - 1
                while (i >= 0 && this[row][i] == '.') i--
                i++
                if (i < col) {
                    this[row][i] = 'O'
                    this[row][col] = '.'
                }
            }
        }
    }
    return hash
}

private fun Array<CharArray>.rollSouth(): String {
    for (row in lastIndex - 1 downTo 0) {
        for (col in this[row].indices) {
            if (this[row][col] == 'O') {
                var i = row + 1
                while (i <= lastIndex && this[i][col] == '.') i++
                i--
                if (i > row) {
                    this[i][col] = 'O'
                    this[row][col] = '.'
                }
            }
        }
    }
    return hash
}

private fun Array<CharArray>.rollEast(): String {
    for (row in indices) {
        for (col in this[row].lastIndex - 1 downTo 0) {
            if (this[row][col] == 'O') {
                var i = col + 1
                while (i <= this[row].lastIndex && this[row][i] == '.') i++
                i--
                if (i > col) {
                    this[row][i] = 'O'
                    this[row][col] = '.'
                }
            }
        }
    }
    return hash
}

private val Array<CharArray>.load: Long
    get() {
        var load = 0L

        for (row in indices) {
            for (col in this[row].indices) {
                if (this[row][col] == 'O') load += size - row
            }
        }

        return load
    }

private val Array<CharArray>.hash: String get() = joinToString("") { it.joinToString("") }

private enum class RollDirection { NORTH, WEST, SOUTH, EAST }
