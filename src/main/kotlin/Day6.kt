fun day6Part1(input: List<String>): Long {
    val races = input.parseRaces()

    var ans = 1L

    for ((time, distance) in races) {
        var nWaysToBeat = 0L
        for (i in 0 .. time / 2) {
            val holdTime1 = (time / 2) - i
            val distance1 = (time - holdTime1) * holdTime1
            val holdTime2 = (time / 2) + i
            val distance2 = (time - holdTime2) * holdTime2

            if (distance1 > distance) nWaysToBeat++
            if (distance2 > distance && holdTime1 != holdTime2) nWaysToBeat++
            if (distance1 <= distance && distance2 <= distance) break
        }

        ans *= maxOf(nWaysToBeat, 1L)
    }

    return ans
}

private fun List<String>.parseRaces(): List<Race> {
    val times = this[0].split(":")[1]
        .split(" ")
        .filterNot { it.isBlank() }
        .map { it.toLong() }

    val distances = this[1].split(":")[1]
        .split(" ")
        .filterNot { it.isBlank() }
        .map { it.toLong() }

    return times.mapIndexed { index, time -> Race(time, distances[index]) }
}

private data class Race(val time: Long, val distance: Long)

fun day6Part2(input: List<String>): Long {
    return day6Part1(input.map { it.replace(" ", "") })
}
