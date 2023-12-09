fun day8Part1(input: List<String>): Long {
    val (instructions, startNode) = input.parseNodesAndInstructions()
    var nSteps = 0L
    var node = startNode
    while (true) {
        for (instruction in instructions) {
            when (instruction) {
                'L' -> node = node.left ?: throw IllegalArgumentException()
                'R' -> node = node.right ?: throw IllegalArgumentException()
            }
            nSteps++
            if (node.name == "ZZZ") return nSteps
        }
    }
}

private data class Node(val name: String, var left: Node? = null, var right: Node? = null) {
    override fun toString(): String = "Node(name=$name, left=${left?.name}, right=${right?.name})"
}

private fun List<String>.parseNodesAndInstructions(): Pair<String, Node> {
    val instructions = this[0]
    val lines = hashMapOf<String, String>()
    val nodes = hashMapOf<String, Node>()
    for (i in 2..lastIndex) {
        val line = this[i]
        val (nodePart) = line.split("=")
        lines[nodePart.trim()] = line
    }

    return instructions to lines.parseNode("AAA", nodes)
}

private fun Map<String, String>.parseNode(name: String, nodes: HashMap<String, Node>): Node {
    if (nodes[name] != null) return nodes[name]!!

    val (_, neighboursPart) = this[name]!!.split("=")
    val (leftNeighbour, rightNeighbour) = neighboursPart.replace("(", "").replace(")", "").split(",")
    val leftNeighbourTrimmed = leftNeighbour.trim()
    val rightNeighbourTrimmed = rightNeighbour.trim()
    val node = Node(name)
    nodes[name] = node
    node.left = parseNode(leftNeighbourTrimmed, nodes)
    node.right = parseNode(rightNeighbourTrimmed, nodes)
    return node
}

fun day8Part2(input: List<String>): Long {
    val (instructions, startNodes) = input.parseNodesAndInstructions2()

    val distancesToZ = LongArray(startNodes.size)
    for (i in startNodes.indices) {
        var distanceToZ = 0L
        var currNode = startNodes[i]
        while (distanceToZ == 0L || currNode.name[2] != 'Z') {
            currNode = when (instructions[distanceToZ.toInt() % instructions.length]) {
                'L' -> currNode.left
                'R' -> currNode.right
                else -> throw IllegalArgumentException()
            }!!
            distanceToZ++
        }
        distancesToZ[i] = distanceToZ
    }

    return lcm(*distancesToZ)
}

private fun List<String>.parseNodesAndInstructions2(): Pair<String, List<Node>> {
    val instructions = this[0]
    val lines = hashMapOf<String, String>()
    val nodes = hashMapOf<String, Node>()
    for (i in 2..lastIndex) {
        val line = this[i]
        val (nodePart) = line.split("=")
        lines[nodePart.trim()] = line
    }

    return instructions to lines
        .filter { (key) -> key[2] == 'A' }
        .map { (key) -> lines.parseNode(key, nodes) }
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