import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day19Test : StringSpec({
    val input = resource("Day19.input").readLines()

    "part1" {
        day19Part1(input) shouldBeExactly 418498
    }

    "part2" {
        day19Part2(input) shouldBeExactly 123331556462603
    }
})
