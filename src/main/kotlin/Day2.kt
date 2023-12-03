fun day2Part1(input: List<String>): Long {
    var nPossible = 0L

    outer@ for (game in input) {
        val (idPart, gamePart) = game.split(":")

        val gameId = idPart.extractNum()

        val rounds = gamePart.split(";")

        for (round in rounds) {
            val cubes = round.split(",")
            for (cube in cubes) {
                val num = cube.extractNum()
                if ("red" in cube && num > MAX_RED) continue@outer
                if ("green" in cube && num > MAX_GREEN) continue@outer
                if ("blue" in cube && num > MAX_BLUE) continue@outer
            }
        }

        nPossible += gameId
    }

    return nPossible
}

fun day2Part2(input: List<String>): Long {
     var sum = 0L

    outer@ for (game in input) {
        val (idPart, gamePart) = game.split(":")

        val gameId = idPart.extractNum()

        val rounds = gamePart.split(";")

        var maxRed = 0
        var maxGreen = 0
        var maxBlue = 0

        for (round in rounds) {
            val cubes = round.split(",")
            for (cube in cubes) {
                val num = cube.extractNum()
                if ("red" in cube) maxRed = maxOf(maxRed, num)
                if ("green" in cube) maxGreen = maxOf(maxGreen, num)
                if ("blue" in cube) maxBlue = maxOf(maxBlue, num)
            }
        }

        sum += maxRed * maxGreen * maxBlue
    }

    return sum
}


private val numberRegex = "\\d+".toRegex()
private const val MAX_RED = 12L
private const val MAX_GREEN = 13L
private const val MAX_BLUE = 14L
private fun String.extractNum(): Int = numberRegex.find(this)!!.value.toInt()
