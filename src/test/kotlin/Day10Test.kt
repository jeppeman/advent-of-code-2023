import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day10Test : StringSpec({
    val input = resource("Day10.input").readLines()

    "part1" {
        day10Part1(input) shouldBeExactly 6823
    }

    "part2" {
        day10Part2(input) shouldBeExactly 415
    }
})
