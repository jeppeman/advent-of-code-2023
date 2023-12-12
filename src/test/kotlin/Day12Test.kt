import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day12Test : StringSpec({
    val input = resource("Day12.input").readLines()

    "part1" {
        day12Part1(input) shouldBeExactly 7307
    }

    "part2" {
        day12Part2(input) shouldBeExactly 3415570893842
    }
})
