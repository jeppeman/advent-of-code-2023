fun day13Part1(input: List<String>): Long {
    val patterns = input.parsePatterns()

    var ans = 0L

    for (pattern in patterns) {
        val (verticalSymmetry, verticalSymmetryDegree) = pattern.columns.findMaxSymmetry()
        val (horizontalSymmetry, horizontalSymmetryDegree) = pattern.findMaxSymmetry()

        if (verticalSymmetryDegree > horizontalSymmetryDegree) {
            ans += verticalSymmetry
        } else if (horizontalSymmetry != -1) {
            ans += horizontalSymmetry * 100
        }
    }

    return ans
}

fun day13Part2(input: List<String>): Long {
    val patterns = input.parsePatterns()

    var ans = 0L

    for (pattern in patterns) {
        val (verticalSymmetry, verticalSymmetryDegree) = pattern.columns.findMaxSymmetry(1)
        val (horizontalSymmetry, horizontalSymmetryDegree) = pattern.findMaxSymmetry(1)

        if (verticalSymmetryDegree > horizontalSymmetryDegree) {
            ans += verticalSymmetry
        } else if (horizontalSymmetry != -1) {
            ans += horizontalSymmetry * 100
        }
    }

    return ans
}

private fun List<String>.parsePatterns(): List<List<String>> {
    val patterns = mutableListOf<MutableList<String>>()

    var currPattern = mutableListOf<String>()
    patterns += currPattern
    var i = 0
    while (i < size) {
        val line = this[i]
        if (line.isBlank()) {
            currPattern = mutableListOf()
            patterns += currPattern
        } else {
            currPattern += line
        }
        i++
    }

    return patterns
}

private fun List<String>.findMaxSymmetry(smudgeAmount: Int = 0): IntArray {
    val symmetryCandidates = mutableListOf<Int>()

    var prev: String? = null
    for (i in indices) {
        if (prev == this[i] || prev diff this[i] == smudgeAmount) symmetryCandidates += i - 1
        prev = this[i]
    }
    if (symmetryCandidates.isEmpty()) return intArrayOf(-1, -1)

    var maxSymmetryIndex = -1
    var maxSymmetry = -1
    outer@ for (candidate in symmetryCandidates) {
        var i = candidate
        var j = candidate + 1
        var diff = 0
        while (i >= 0 && j < size) {
            diff += this[i] diff this[j]
            if (diff > 1) continue@outer
            i--
            j++
        }

        if (diff == smudgeAmount && j - i + 1 > maxSymmetry) {
            maxSymmetry = j - i + 1
            maxSymmetryIndex = i + (j - i) / 2 + 1
        }
    }

    return intArrayOf(maxSymmetryIndex, maxSymmetry)
}

private infix fun String?.diff(other: String): Int {
    if (this == null) return other.length
    var diff = 0
    for (i in indices) if (this[i] != other[i]) diff++
    return diff
}

private val List<String>.columns: List<String>
    get() {
        val m = size
        val n = this[0].length
        val columns = MutableList(n) { "" }
        for (col in 0 until n) {
            val line = CharArray(m)
            for (row in 0 until m) line[row] = this[row][col]
            columns[col] = String(line)
        }
        return columns
    }
