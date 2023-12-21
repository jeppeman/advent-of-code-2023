import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day16Test : StringSpec({
    val input = resource("Day16.input").readLines()

    "part1" {
        day16Part1(input) shouldBeExactly 7307
    }

    "part2" {
        day16Part2(input) shouldBeExactly 7635
    }
})
