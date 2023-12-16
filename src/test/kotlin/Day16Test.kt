import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day16Test : StringSpec({
    val input = resource("Day16.input").readText()

    "part1" {
        day16Part1(input.split("\n")) shouldBeExactly 7307
    }

    "part2" {
        day16Part2(input.split("\n")) shouldBeExactly 7635
    }
})
