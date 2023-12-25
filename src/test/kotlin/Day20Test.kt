import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day20Test : StringSpec({
    val input = resource("Day20.input").readLines()

    "part1" {
        day20Part1(input) shouldBeExactly 899848294
    }

    "part2" {
        day20Part2(input) shouldBeExactly 247454898168563
    }
})
