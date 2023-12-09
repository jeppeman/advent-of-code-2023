import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day9Test : StringSpec({
    val input = resource("Day9.input").readLines()

    "part1" {
        day9Part1(input) shouldBeExactly 1884768153
    }

    "part2" {
        day9Part2(input) shouldBeExactly 1031
    }
})
