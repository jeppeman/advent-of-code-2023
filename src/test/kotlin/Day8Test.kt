import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day8Test : StringSpec({
    val input = resource("Day8.input").readLines()

    "part1" {
        day8Part1(input) shouldBeExactly 17263
    }

    "part2" {
        day8Part2(input) shouldBeExactly 14631604759649
    }
})
