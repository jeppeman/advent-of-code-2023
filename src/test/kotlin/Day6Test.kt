import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day6Test : StringSpec({
    val input = resource("Day6.input").readLines()

    "part1" {
        day6Part1(input) shouldBeExactly 74698
    }

    "part2" {
        day6Part2(input) shouldBeExactly 27563421
    }
})
