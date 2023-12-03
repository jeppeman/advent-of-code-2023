import io.kotest.core.spec.style.StringSpec

class Day2Test : StringSpec({
    val input = resource("Day2.input").readLines()

    "part1" {
        println(day2Part1(input))
    }

    "part2" {
        println(day2Part2(input))
    }
})
