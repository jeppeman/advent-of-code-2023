import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day18Test : StringSpec({
    val input = resource("Day18.input").readLines()

    "part1" {
        day18Part1(input) shouldBeExactly 95356
    }

    "part2" {
        day18Part2(input) shouldBeExactly 92291468914147
    }
})
