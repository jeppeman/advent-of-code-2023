fun day12Part1(input: List<String>): Long {
    var ans = 0L

    for (line in input) {
        val (row, groupsString) = line.split(" ")
        val groups = groupsString.split(",").map { it.toInt() }.toIntArray()
        ans += row.solve(groups)
    }

    return ans
}

fun day12Part2(input: List<String>): Long {
    var ans = 0L

    for (line in input) {
        val (row, groupsString) = line.split(" ")
        val unfolded = (0 until 5).joinToString("?") { row }
        val unfoldedGroupsString = (0 until 5).joinToString(",") { groupsString }
        val groups = unfoldedGroupsString.split(",").map { it.toInt() }.toIntArray()
        ans += unfolded.solve(groups)
    }

    return ans
}

private fun String.solve(
    groups: IntArray,
    spring: Int = 0,
    group: Int = 0,
    groupSize: Int = 0,
    memo: HashMap<String, Long> = hashMapOf(),
    key: String = "$spring $group $groupSize",
): Long = when {
    spring == length -> if (
        (group == groups.size && groupSize == 0) || group == groups.lastIndex && groups[group] == groupSize
    ) 1L else 0L

    key in memo -> memo[key]!!

    group < groups.size && groupSize == groups[group] -> if (this[spring] == '#') {
        0L
    } else solve(groups, spring + 1, group + 1, 0, memo)

    this[spring] == '#' -> solve(groups, spring + 1, group, groupSize + 1, memo)

    this[spring] == '.' -> if (groupSize == 0) solve(groups, spring + 1, group, 0, memo) else 0L

    else -> {
        val operational = if (groupSize == 0) solve(groups, spring + 1, group, 0, memo) else 0L
        val damaged = solve(groups, spring + 1, group, groupSize + 1, memo)

        (operational + damaged)
    }
}.apply { memo[key] = this }