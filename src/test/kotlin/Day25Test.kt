import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly

class Day25Test : StringSpec({
    val input = resource("Day25.input").readLines()

    "part1" {
        day25Part1(input) shouldBeExactly 525264
    }
})
