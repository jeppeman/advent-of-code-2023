import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day7Test : StringSpec({
    val input = resource("Day7.input").readLines()

    "part1" {
        day7Part1(input) shouldBeExactly 252052080
    }

    "part2" {
        day7Part2(input) shouldBeExactly 252898370
    }
})
