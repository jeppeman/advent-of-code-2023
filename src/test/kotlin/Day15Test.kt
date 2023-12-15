import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day15Test : StringSpec({
    val input = resource("Day15.input").readText()

    "part1" {
        day15Part1(input.split(",")) shouldBeExactly 512283
    }

    "part2" {
        day15Part2(input.split(",")) shouldBeExactly 215827
    }
})
