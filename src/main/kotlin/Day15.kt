import kotlin.math.pow

fun day15Part1(input: List<String>): Long = input.sumOf { it.hash }.toLong()

fun day15Part2(input: List<String>): Long {
    val boxes = Array(256) { mutableMapOf<String, Int>() }
    var ans = 0L
    for (step in input) {
        val labelBuilder = StringBuilder()
        var i = 0
        while (step[i] != '-' && step[i] != '=') labelBuilder.append(step[i++])
        val label = labelBuilder.toString()
        when (step[i++]) {
            '-' -> boxes[label.hash] -= label
            '=' -> {
                var focalLength = 0
                while (i < step.length) {
                    focalLength += step[i].digitToInt() * 10.0.pow(step.length - i - 1).toInt()
                    i++
                }
                boxes[label.hash][label] = focalLength
            }
        }
    }

    for ((boxIndex, box) in boxes.withIndex()) {
        for ((slotIndex, focalLength) in box.values.withIndex()) {
            ans += (boxIndex + 1) * (slotIndex + 1) * focalLength
        }
    }

    return ans
}

private val String.hash: Int
    get() {
        var hash = 0
        for (char in this) {
            hash += char.code
            hash *= 17
            hash %= 256
        }
        return hash
    }
