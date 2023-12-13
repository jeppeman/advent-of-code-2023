import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day13Test : StringSpec({
    val input = resource("Day13.input").readLines()

    "part1" {
        day13Part1(input) shouldBeExactly 29165
    }

    "part2" {
        day13Part2(input) shouldBeExactly 32192
    }
})
