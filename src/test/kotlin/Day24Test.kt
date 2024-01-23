import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day24Test : StringSpec({
    val input = resource("Day24.input").readLines()

    "part1" {
        day24Part1(input, 200000000000000, 400000000000000) shouldBeExactly 16727
    }

    "part2" {
        day24Part2(input) shouldBeExactly 606772018765659
    }
})
