import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day23Test : StringSpec({
    val input = resource("Day23.input").readLines()

    "part1" {
        day23Part1(input) shouldBeExactly 2182
    }

    "part2" {
        day23Part2(input) shouldBeExactly 6670
    }
})
