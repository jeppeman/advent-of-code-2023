import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day14Test : StringSpec({
    val input = resource("Day14.input").readLines()

    "part1" {
        day14Part1(input) shouldBeExactly 110677
    }

    "part2" {
        day14Part2(input) shouldBeExactly 90551
    }
})
