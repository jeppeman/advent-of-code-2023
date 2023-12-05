import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day4Test : StringSpec({
    val input = resource("Day4.input").readLines()

    "part1" {
        day4Part1(input) shouldBeExactly 25010
    }

    "part2" {
        day4Part2(input) shouldBeExactly 9924412
    }
})
