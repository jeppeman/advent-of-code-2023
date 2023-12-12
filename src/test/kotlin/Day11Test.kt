import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day11Test : StringSpec({
    val input = resource("Day11.input").readLines()

    "part1" {
        day11Part1(input) shouldBeExactly 9418609
    }

    "part2" {
        day11Part2(input) shouldBeExactly 593821230983
    }
})
