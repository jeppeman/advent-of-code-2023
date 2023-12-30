import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day22Test : StringSpec({
    val input = resource("Day22.input").readLines()

    "part1" {
        day22Part1(input) shouldBeExactly 497
    }

    "part2" {
        day22Part2(input) shouldBeExactly 67468
    }
})
