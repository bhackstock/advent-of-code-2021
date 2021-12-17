import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis {

        val caves = getCaves()
        val routes = getRoutes(caves.first { it.name == "start" })
        printRoutes(routes)
    }
    println(time)
}

fun getRoutes(startingCave: Cave): MutableList<Route> {
    return getRoutes(Route(listOf(startingCave)))
}

fun getRoutes(soFar: Route): MutableList<Route> {
    if (soFar.currentCave.name == "end") {
        return mutableListOf(soFar)
    }

    if (!soFar.canContinueRoute()) {
        return mutableListOf()
    }

    return soFar.currentCave.connectedTo
        .filter { soFar.canVisitCave(it) }
        .flatMap { getRoutes(soFar.copyAppend(it)) }
        .toMutableList()
}


fun getCaves(): List<Cave> {
    val adjacentCaves = mutableMapOf<String, MutableList<String>>()
    val file = loadFile("day12.txt")
    file.forEachLine { line ->
        val split = line.trim().split("-")
        val start = split[0]
        val end = split[1]


        if (adjacentCaves[start] == null) {
            adjacentCaves[start] = mutableListOf(end)
        } else {
            adjacentCaves[start]?.add(end)
        }


        if (adjacentCaves[end] == null) {
            adjacentCaves[end] = mutableListOf(start)
        } else {
            adjacentCaves[end]?.add(start)
        }
    }

    val caves: Map<String, Cave> =
        adjacentCaves.keys.distinct().map { Cave(it, mutableListOf()) }.associateBy { it.name }

    return adjacentCaves.keys.map { name ->
        caves[name]!!.also {
            it.connectedTo.addAll(adjacentCaves[name]!!.mapNotNull { caves[it] })
        }
    }
}

class Cave(val name: String, val connectedTo: MutableList<Cave>) {

    val isBig
        get() = name == name.uppercase()

    val isEndOrStart
        get() = name == "end" || name == "start"
}

data class Route(val caves: List<Cave>) {
    fun visitedCave(cave: Cave): Boolean {
        return caves.contains(cave)
    }

    fun visitedNoSmallCaveTwice(): Boolean {
        val smallCaves = caves.filter { !it.isBig }
        return smallCaves.distinct().size == smallCaves.size
    }

    fun canVisitCave(cave: Cave): Boolean {
        return cave.isBig || !visitedCave(cave) || (!cave.isEndOrStart && visitedNoSmallCaveTwice())
    }

    fun canContinueRoute() = caves.last().connectedTo.any { this.canVisitCave(it) }

    fun copyAppend(cave: Cave): Route {
        return copy(caves = caves.toMutableList().apply { add(cave) })
    }

    val currentCave
        get() = caves.last()
}

private fun printRoutes(routes: MutableList<Route>) {
    println("found following routes: ")
    routes.forEach { println(it) }
    println("number of caves: ${routes.size}")
}
