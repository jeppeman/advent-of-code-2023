import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day17Test : StringSpec({
    val input = resource("Day17.input").readLines()

    "part1" {
        day17Part1(input) shouldBeExactly 742
    }

    "part2" {
        day17Part2(input) shouldBeExactly 918
    }
})
