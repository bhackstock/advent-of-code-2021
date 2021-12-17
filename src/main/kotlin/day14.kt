fun main() {
    val polymer = getPolymer().toMutableList()
    val map: Map<String, Char> = getMap()

    repeat(10) {
        polymerIteration(polymer, map)
    }

    println(polymer.joinToString(""))
    val quantities = polymer.sorted().groupBy { it.toInt() }.values.map { it.size }
    val result = quantities.maxOrNull()!! - quantities.minOrNull()!!
    println(result)
}

private fun polymerIteration(
    polymer: MutableList<Char>,
    map: Map<String, Char>
) {
    var i = 0
    while (i < polymer.size - 1) {
        val key: String = polymer.subList(i, i + 2).joinToString("")
        val value = map[key]
        value?.let {
            polymer.add(i + 1, it)
            i++
        }
        i++
    }
}

fun getPolymer(): String = loadFile("day14.txt").readLines()[0]

fun getMap(): Map<String, Char> {
    return loadFile("day14.txt").readLines()
        .filter { it.contains("->") }
        .map { Pair(it.split("->")[0].trim(), it.split("->")[1][1]) }
        .associate { it }
}