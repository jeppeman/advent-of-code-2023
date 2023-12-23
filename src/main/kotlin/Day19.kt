fun day19Part1(input: List<String>): Long {
    val (workflows, parts) = input.parse()
    return parts.filter(workflows::accepts).fold(0L) { acc, curr -> acc + curr.rating }
}

fun day19Part2(input: List<String>): Long {
    val (workflows) = input.parse()
    val queue = ArrayDeque<State>()
    queue += State("in", 1..4000, 1..4000, 1..4000, 1..4000)
    var ans = 0L

    while (queue.isNotEmpty()) {
        var state = queue.removeFirst()
        val (result, x, m, a, s) = state
        if (result == "A") {
            ans += x.size * m.size * a.size * s.size
            continue
        }
        if (result == "R") continue
        val workflow = workflows[result] ?: continue
        for (rule in workflow) {
            queue += when (":" in rule.raw) {
                false -> state.copy(result = rule.raw)
                else -> {
                    val (condition, nextResult) = rule.raw.split(":")
                    val variable = condition[0]
                    val op = condition[1].toString()
                    val limit = condition.split(op)[1].toInt()
                    state.adjustRange(variable, op, limit).copy(result = nextResult).apply {
                        state = state.adjustRange(variable, if (op == "<") ">=" else "<=", limit)
                    }
                }
            }
        }
    }

    return ans
}

private fun State.adjustRange(variable: Char, op: String, limit: Int): State {
    fun IntRange.adjust(): IntRange = when (op) {
        ">" -> maxOf(limit + 1, first)..last
        ">=" -> maxOf(limit, first)..last
        "<" -> first until minOf(limit, last)
        "<=" -> first..minOf(limit, last)
        else -> throw IllegalArgumentException()
    }

    return when (variable) {
        'x' -> copy(x = x.adjust())
        'm' -> copy(m = m.adjust())
        'a' -> copy(a = a.adjust())
        's' -> copy(s = s.adjust())
        else -> throw IllegalArgumentException()
    }
}

private fun List<String>.parse(): Pair<Map<String, List<Rule>>, List<Part>> {
    val workflows = hashMapOf<String, List<Rule>>()
    val parts = mutableListOf<Part>()
    var inParts = false
    for (line in this) {
        if (line.isBlank()) {
            inParts = true
            continue
        }
        if (inParts) {
            parts += line.parsePart()
        } else {
            val rules = mutableListOf<Rule>()
            val (name, right) = line.split("{")
            val ruleStrings = right.replace("}", "").split(",")
            for (ruleString in ruleStrings) rules += ruleString.parseRule()
            workflows[name] = rules
        }
    }

    return workflows to parts
}

private fun Map<String, List<Rule>>.accepts(part: Part): Boolean {
    var workflowRules = this["in"]
    workflow@ while (workflowRules != null) {
        val iterator = workflowRules.iterator()
        while (iterator.hasNext()) {
            return when (val result = iterator.next()(part)) {
                Result.Accepted -> true
                Result.Rejected -> false
                Result.Next -> continue
                is Result.Reroute -> {
                    workflowRules = this[result.workflow]
                    continue@workflow
                }
            }
        }
    }

    return false
}

private fun String.parseRule(): Rule = when {
    ":" in this -> parseComparison()
    else -> rule(this) { parseResult() }
}

private fun String.parseComparison(): Rule {
    val (condition, result) = split(":")
    val op = condition[1]
    val (leftOp, rightOp) = condition.split(op)
    return rule(this) { part ->
        val leftOpInt = when (leftOp) {
            "x" -> part.x
            "m" -> part.m
            "a" -> part.a
            "s" -> part.s
            else -> throw IllegalArgumentException()
        }
        val opResult = when (op) {
            '<' -> leftOpInt < rightOp.toInt()
            '>' -> leftOpInt > rightOp.toInt()
            else -> throw IllegalArgumentException()
        }
        when (opResult) {
            false -> Result.Next
            true -> result.parseResult()
        }
    }
}

private fun String.parseResult(): Result = when (this) {
    "A" -> Result.Accepted
    "R" -> Result.Rejected
    else -> Result.Reroute(this)
}

private fun String.parsePart(): Part {
    val propertyStrings = replace("{", "").replace("}", "").split(",")
    var part = Part(0, 0, 0, 0)
    for (propertyString in propertyStrings) {
        val (property, valueString) = propertyString.split("=")
        part = when (property) {
            "x" -> part.copy(x = valueString.toInt())
            "m" -> part.copy(m = valueString.toInt())
            "a" -> part.copy(a = valueString.toInt())
            "s" -> part.copy(s = valueString.toInt())
            else -> throw IllegalArgumentException(property)
        }
    }
    return part
}

private data class Part(val x: Int, val m: Int, val a: Int, val s: Int)

private val Part.rating: Int get() = x + m + a + s

private interface Rule {
    val raw: String
    operator fun invoke(part: Part): Result
}

private fun rule(raw: String, block: (part: Part) -> Result) = object : Rule {
    override val raw: String = raw

    override fun invoke(part: Part): Result = block(part)
}

private sealed interface Result {
    object Accepted : Result
    object Rejected : Result
    object Next : Result
    data class Reroute(val workflow: String) : Result
}

private data class State(val result: String, val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange)

private val IntRange.size: Long get() = (last - first + 1).toLong()