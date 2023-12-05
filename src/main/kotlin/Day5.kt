fun day5Part1(input: List<String>): Long {
    val seeds = input.parseSeeds()
    val (
        seedToSoil,
        soilToFertilizer,
        fertilizerToWater,
        waterToLight,
        lightToTemperature,
        temperatureToHumidity,
        humidityToLocation,
    ) = input.parseMaps()

    var minLocation = Long.MAX_VALUE

    for (seed in seeds) {
        val soil = seedToSoil[seed]
        val fertilizer = soilToFertilizer[soil]
        val water = fertilizerToWater[fertilizer]
        val light = waterToLight[water]
        val temperature = lightToTemperature[light]
        val humidity = temperatureToHumidity[temperature]
        val location = humidityToLocation[humidity]

        minLocation = minOf(minLocation, location)
    }

    return minLocation
}


private data class SourceMaps(
    val seedToSoil: SourceMap = SourceMap(),
    val soilToFertilizer: SourceMap = SourceMap(),
    val fertilizerToWater: SourceMap = SourceMap(),
    val waterToLight: SourceMap = SourceMap(),
    val lightToTemperature: SourceMap = SourceMap(),
    val temperatureToHumidity: SourceMap = SourceMap(),
    val humidityToLocation: SourceMap = SourceMap(),
)

private val DATA_LINE_REGEX = "\\d+ \\d+ \\d+".toRegex()

private data class SourceRange(val srcStart: Long, val dstStart: Long, val length: Long) {
    val srcRange get() = srcStart until srcStart + length
}

private class SourceMap {
    private val ranges = mutableListOf<SourceRange>()

    operator fun plusAssign(range: SourceRange) {
        ranges += range
    }

    operator fun get(src: Long): Long {
        for (range in ranges) {
            if (src in range.srcRange) {
                return range.dstStart + (src - range.srcRange.first)
            }
        }

        return src
    }

    operator fun get(srcRange: LongRange): List<LongRange> {
        val ans = mutableListOf<LongRange>()
        for (range in ranges) {
            if (srcRange.first in range.srcRange
                || srcRange.last in range.srcRange
                || range.srcRange.first in srcRange
            ) {
                val startOffset = maxOf(0, srcRange.first - range.srcRange.first)
                ans += range.dstStart + startOffset until minOf(
                    range.dstStart + startOffset + (srcRange.last - srcRange.first),
                    range.dstStart + range.length
                )
            }
        }
        return ans.takeIf { it.isNotEmpty() } ?: listOf(srcRange)
    }
}

private fun List<String>.parseSeeds(): List<Long> = this[0].split(":")[1]
    .split(" ")
    .filter { it.isNotBlank() }
    .map { it.toLong() }

private fun List<String>.parseMaps(): SourceMaps {
    lateinit var currMap: SourceMap
    var sourceMaps = SourceMaps()
    for (line in this) when {
        "seed-to-soil" in line -> {
            sourceMaps = sourceMaps.copy(seedToSoil = SourceMap().apply { currMap = this })
        }

        "soil-to-fertilizer" in line -> {
            sourceMaps = sourceMaps.copy(soilToFertilizer = SourceMap().apply { currMap = this })
        }

        "fertilizer-to-water" in line -> {
            sourceMaps = sourceMaps.copy(fertilizerToWater = SourceMap().apply { currMap = this })
        }

        "water-to-light" in line -> {
            sourceMaps = sourceMaps.copy(waterToLight = SourceMap().apply { currMap = this })
        }

        "light-to-temperature" in line -> {
            sourceMaps = sourceMaps.copy(lightToTemperature = SourceMap().apply { currMap = this })
        }

        "temperature-to-humidity" in line -> {
            sourceMaps = sourceMaps.copy(temperatureToHumidity = SourceMap().apply { currMap = this })
        }

        "humidity-to-location" in line -> {
            sourceMaps = sourceMaps.copy(humidityToLocation = SourceMap().apply { currMap = this })
        }

        DATA_LINE_REGEX.matches(line) -> {
            val (destStr, sourceStr, lengthStr) = line.split(" ")
            currMap += SourceRange(sourceStr.toLong(), destStr.toLong(), lengthStr.toLong())
        }
    }

    return sourceMaps
}

fun day5Part2(input: List<String>): Long {
    val seedRanges = input.parseSeedRanges()
    val (
        seedToSoil,
        soilToFertilizer,
        fertilizerToWater,
        waterToLight,
        lightToTemperature,
        temperatureToHumidity,
        humidityToLocation,
    ) = input.parseMaps()


    var minLocation = Long.MAX_VALUE

    for (seedRange in seedRanges) {
        val soilRanges = seedToSoil[seedRange]
        for (soilRange in soilRanges) {
            val fertilizerRanges = soilToFertilizer[soilRange]
            for (fertilizerRange in fertilizerRanges) {
                val waterRanges = fertilizerToWater[fertilizerRange]
                for (waterRange in waterRanges) {
                    val lightRanges = waterToLight[waterRange]
                    for (lightRange in lightRanges) {
                        val temperatureRanges = lightToTemperature[lightRange]
                        for (temperatureRange in temperatureRanges) {
                            val humidityRanges = temperatureToHumidity[temperatureRange]
                            for (humidityRange in humidityRanges) {
                                val locationRanges = humidityToLocation[humidityRange]
                                for (locationRange in locationRanges) {
                                    minLocation = minOf(minLocation, locationRange.first)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    return minLocation
}

private fun List<String>.parseSeedRanges(): List<LongRange> = this[0].split(":")[1]
    .split(" ")
    .filter { it.isNotBlank() }
    .map { it.toLong() }
    .run {
        val ranges = mutableListOf<LongRange>()
        for (i in indices step 2) {
            ranges += this[i] until this[i] + this[i + 1]
        }
        return@run ranges
    }

