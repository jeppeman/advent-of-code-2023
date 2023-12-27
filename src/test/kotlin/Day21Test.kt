import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day21Test : StringSpec({
    val input = resource("Day21.input").readLines()

    "part1" {
        day21Part1(input) shouldBeExactly 3764
    }

    "part2" {
        day21Part2(input) shouldBeExactly 622926941971282
    }
})
