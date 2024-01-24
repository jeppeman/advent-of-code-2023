import java.util.Objects

fun day25Part1(input: List<String>): Long {
    val graph = input.parse()

    val node = graph.keys.first()
    val start = graph.furthestFrom(node)
    val finish = graph.furthestFrom(start)

    val path1 = graph.pathBetween(start, finish)
    val path2 = graph.pathBetween(start, finish, path1)
    val path3 = graph.pathBetween(start, finish, path1 + path2)

    val excluded = path1 + path2 + path3

    val group1Size = graph.nReachableNodesFrom(start, excluded)
    val group2Size = graph.nReachableNodesFrom(finish, excluded)

    return group1Size * group2Size
}

private typealias Graph = Map<String, Set<String>>

private class Edge(val from: String, val to: String) {
    override fun equals(other: Any?): Boolean = other is Edge
            && listOf(from, to).sorted() == listOf(other.from, other.to).sorted()

    override fun hashCode(): Int = Objects.hash(listOf(from, to).sorted())

    override fun toString(): String = "Edge($from, $to)"
}

private fun List<String>.parse(): Graph {
    val graph = hashMapOf<String, HashSet<String>>()
    for (line in this) {
        val (node, neighbours) = line.split(":")
            .let { (n, adj) -> n to adj.split(" ").filter { it.isNotBlank() }.toSet() }
        graph.computeIfAbsent(node) { hashSetOf() } += neighbours
        for (neighbour in neighbours) {
            graph.computeIfAbsent(neighbour) { hashSetOf() } += node
        }
    }

    return graph
}

private fun Graph.furthestFrom(node: String): String {
    val queue = ArrayDeque<String>().apply { this += node }
    val visited = hashSetOf(node)
    var curr = node

    while (queue.isNotEmpty()) {
        curr = queue.removeFirst()

        for (neighbour in this[curr].orEmpty()) {
            if (neighbour in visited) continue

            visited += neighbour
            queue += neighbour
        }
    }

    return curr
}

private fun Graph.pathBetween(
    start: String,
    finish: String,
    exclude: Set<Edge> = hashSetOf()
): Set<Edge> {
    val queue = ArrayDeque<Pair<String, Set<Edge>>>().apply { this += start to emptySet() }
    val visited = hashSetOf(start)

    while (queue.isNotEmpty()) {
        val (curr, path) = queue.removeFirst()
        if (curr == finish) return path

        for (neighbour in this[curr].orEmpty()) {
            if (neighbour in visited) continue

            val edge = Edge(curr, neighbour)
            if (edge in exclude) continue

            visited += neighbour
            queue += neighbour to path + edge
        }
    }

    return emptySet()
}

private fun Graph.nReachableNodesFrom(node: String, exclude: Set<Edge>): Long {
    val queue = ArrayDeque<String>().apply { this += node }
    val visited = hashSetOf(node)

    while (queue.isNotEmpty()) {
        val curr = queue.removeFirst()

        for (neighbour in this[curr].orEmpty()) {
            if (neighbour in visited) continue

            val edge = Edge(curr, neighbour)
            if (edge in exclude) continue

            visited += neighbour
            queue += neighbour
        }
    }

    return visited.size.toLong()
}