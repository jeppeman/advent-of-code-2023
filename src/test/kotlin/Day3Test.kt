import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day3Test : StringSpec({
    val input = resource("Day3.input").readLines()

    "part1" {
        day3Part1(input) shouldBeExactly 540131
    }

    "part2" {
        day3Part2(input) shouldBeExactly 86879020
    }
})
