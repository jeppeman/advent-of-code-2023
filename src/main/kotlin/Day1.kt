fun day1Part1(strings: List<String>): Long {
    var sum = 0L
    for (str in strings) {
        var firstDigit = 0
        for (char in str) {
            if (char.isDigit()) {
                firstDigit = char - '0'
                break
            }
        }
        var secondDigit = 0
        for (i in str.lastIndex downTo 0) {
            if (str[i].isDigit()) {
                secondDigit = str[i] - '0'
                break
            }
        }
        sum += "$firstDigit$secondDigit".toInt()
    }

    return sum
}

fun day1Part2(strings: List<String>): Long {
    var sum = 0L
    for (str in strings) {
        var firstDigitIndex = 0
        var minDigitWordIndex = Int.MAX_VALUE
        var minDigitWord = ""
        var maxDigitWordIndex = Int.MIN_VALUE
        var maxDigitWord = ""
        for (digitWord in digitWords) {
            val minIdx = str.indexOf(digitWord).takeIf { it != -1 }
            val maxIdx = str.lastIndexOf(digitWord).takeIf { it != -1 }
            if (minIdx != null && minIdx < minDigitWordIndex) {
                minDigitWordIndex = minIdx
                minDigitWord = digitWord
            }
            if (maxIdx != null && maxIdx > maxDigitWordIndex) {
                maxDigitWordIndex = maxIdx
                maxDigitWord = digitWord
            }
        }
        for (i in str.indices) {
            if (str[i].isDigit()) {
                firstDigitIndex = i
                break
            }
        }
        var secondDigitIndex = 0
        for (i in str.lastIndex downTo 0) {
            if (str[i].isDigit()) {
                secondDigitIndex = i
                break
            }
        }

        val firstDigit = if (firstDigitIndex < minDigitWordIndex) {
            str[firstDigitIndex] - '0'
        } else {
            digitWords.indexOf(minDigitWord) + 1
        }

        val secondDigit = if (secondDigitIndex > maxDigitWordIndex) {
            str[secondDigitIndex] - '0'
        } else {
            digitWords.indexOf(maxDigitWord) + 1
        }

        sum += "$firstDigit$secondDigit".toInt()
    }

    return sum
}

private val digitWords = arrayOf(
    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
)