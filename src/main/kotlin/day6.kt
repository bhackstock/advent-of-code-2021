fun main() {

    val input = loadFile("day6.txt")

    val fish = input.readLines()[0].split(",").map { it.toInt() }
    val numFishForDays = LongArray(9) { 0 }

    fish.forEach { numFishForDays[it]++ }

    val queue = ArrayDeque(numFishForDays.asList())
    println(queue)
    repeat(256) {
        queue.iterate()
    }

    println("total fish ${queue.sum()}")
}

fun ArrayDeque<Long>.iterate() {
    val parents = removeFirst()
    this[6] += parents
    addLast(parents)

    println(this)
}
