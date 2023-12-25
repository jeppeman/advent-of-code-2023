fun day20Part1(input: List<String>): Long {
    val queue = ArrayDeque<Pulse>()
    val modules = input.parse(queue)
    var low = 0L
    var high = 0L
    repeat(1000) {
        queue += Pulse("button", "broadcaster", 0)
        while (queue.isNotEmpty()) {
            val pulse = queue.removeFirst()
            val (_, destination, type) = pulse
            if (type == 0) low++ else high++
            val module = modules[destination] ?: continue
            module.processPulse(pulse)
        }
    }

    return low * high
}

fun day20Part2(input: List<String>): Long {
    val queue = ArrayDeque<Pulse>()
    val modules = input.parse(queue)
    // &dn is the input of rx
    val dn = modules["dn"] ?: throw IllegalStateException("dn module not found")

    val pushesWithHighPulsesForDn = dn.inputs.associateWith { emptyList<Long>() }.toMutableMap()

    var push = 0L
    while (pushesWithHighPulsesForDn.any { (_, pushes) -> pushes.size < 2 }) {
        queue += Pulse("button", "broadcaster", 0)
        while (queue.isNotEmpty()) {
            val pulse = queue.removeFirst()
            val (source, destination, type) = pulse
            val module = modules[destination] ?: continue
            module.processPulse(pulse)
            if (type == 1 && destination == dn.id) {
                pushesWithHighPulsesForDn[source] = pushesWithHighPulsesForDn.getOrDefault(source, emptyList()) + push
            }
        }
        push++
    }

    val dnHighInputCycles = pushesWithHighPulsesForDn.values.map { (low, high) -> high - low }.toLongArray()

    return lcm(*dnHighInputCycles)
}

private fun List<String>.parse(queue: ArrayDeque<Pulse>): Map<String, Module> {
    val moduleInputs = mutableMapOf<String, List<String>>()
    val moduleFactories = mutableMapOf<String, (inputs: List<String>) -> Module>()

    for (line in this) {
        val (moduleString, outputsString) = line.split("->")
        val outputs = outputsString.split(",").filter { it.isNotBlank() }.map { it.trim() }

        val moduleName = when (moduleString[0]) {
            '%', '&' -> moduleString.substring(1).trim()
            else -> moduleString.trim()
        }

        for (output in outputs) {
            moduleInputs[output] = moduleInputs.getOrDefault(output, emptyList()) + moduleName
        }

        moduleFactories[moduleName] = fun(inputs: List<String>) = when (moduleString[0]) {
            '%' -> FlipFlopModule(moduleString.substring(1).trim(), inputs, outputs, queue::add)
            '&' -> ConjunctionModule(moduleString.substring(1).trim(), inputs, outputs, queue::add)
            else -> BroadcastModule(moduleString.trim(), inputs, outputs, queue::add)
        }
    }

    return moduleFactories.mapValues { (moduleName, factory) -> factory(moduleInputs[moduleName] ?: emptyList()) }
}

private data class Pulse(val source: String, val destination: String, val type: Int)

private sealed interface Module {
    val id: String
    val inputs: List<String>
    val outputs: List<String>
    fun processPulse(pulse: Pulse)
}

private class BroadcastModule(
    override val id: String,
    override val inputs: List<String>,
    override val outputs: List<String>,
    val enqueuePulse: (pulse: Pulse) -> Unit,
) : Module {
    override fun processPulse(pulse: Pulse) {
        for (output in outputs) enqueuePulse(Pulse("broadcaster", output, pulse.type))
    }
}

private class FlipFlopModule(
    override val id: String,
    override val inputs: List<String>,
    override val outputs: List<String>,
    val enqueuePulse: (pulse: Pulse) -> Unit,
) : Module {
    private var state = 0

    override fun processPulse(pulse: Pulse) = when (pulse.type) {
        0 -> {
            state = if (state == 1) 0 else 1
            for (output in outputs) enqueuePulse(Pulse(id, output, state))
        }

        1 -> {}
        else -> throw IllegalArgumentException("Unexpected pulse: $pulse")
    }
}

private class ConjunctionModule(
    override val id: String,
    override val inputs: List<String>,
    override val outputs: List<String>,
    val enqueuePulse: (pulse: Pulse) -> Unit,
) : Module {
    private val states = inputs.associateWith { 0 }.toMutableMap()

    override fun processPulse(pulse: Pulse) {
        states[pulse.source] = pulse.type
        if (states.all { (_, state) -> state == 1 }) {
            for (output in outputs) enqueuePulse(Pulse(id, output, 0))
        } else {
            for (output in outputs) enqueuePulse(Pulse(id, output, 1))
        }
    }
}

private fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

private fun lcm(vararg values: Long): Long {
    var ans = 1L
    for (value in values) ans = (value * ans) / gcd(value, ans)
    return ans
}