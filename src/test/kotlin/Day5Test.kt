import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day5Test : StringSpec({
    val input = resource("Day5.input").readLines()

    "part1" {
        day5Part1(input) shouldBeExactly 174137457
    }

    "part2" {
        day5Part2(input) shouldBeExactly 1493866
    }
})
