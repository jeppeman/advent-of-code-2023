import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day1Test : StringSpec({
    val input = resource("Day1.input").readLines()

    "part1" {
        day1Part1(input) shouldBeExactly 54304
    }

    "part2" {
        day1Part2(input) shouldBeExactly 54418
    }
})