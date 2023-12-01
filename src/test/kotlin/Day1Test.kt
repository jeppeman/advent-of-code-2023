import io.kotest.core.spec.style.StringSpec

class Day1Test : StringSpec({
    val input = resource("Day1.input").readLines()

    "part1" {
        println(day1Part1(input))
    }

    "part2" {
        println(day1Part2(input))
    }
})