import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day2Test : StringSpec({
    val input = resource("Day2.input").readLines()

    "part1" {
        day2Part1(input) shouldBeExactly 2006
    }

    "part2" {
        day2Part2(input) shouldBeExactly 84911
    }
})
