var globalFlashCounter = 0L
fun main() {
    day11_ex2()
}

private fun day11_ex2() {
    val octopussies = getMatrix()
    println("before:")
    println(octopussies.getAsString())

    var iteration = 0
    do {
        octopussies.forAll {
            this.flashedThisRound = false
            this += 1
        }
        while (octopussies.hasOctopussesReadyToFlash()) {
            octopussies.flashOneIteration()
        }
        iteration++
    } while (!octopussies.allOctopussesJustFlashed())

    println("after:")
    println(octopussies.getAsString())
    println("all flashed in iteration: $iteration")
}

private fun day11_ex1() {
    val octopussies = getMatrix()
    println("before:")
    println(octopussies.getAsString())

    repeat(100) {
        octopussies.forAll {
            this.flashedThisRound = false
            this += 1
        }
        while (octopussies.hasOctopussesReadyToFlash()) {
            octopussies.flashOneIteration()
        }

    }
    println("after:")
    println(octopussies.getAsString())
    println("flashed $globalFlashCounter times!")
}

fun getMatrix(): Array<Array<Octopus>> {
    val lines = loadFile("day11.txt").readLines()
    val matrix = Array(lines.size + 2) { Array(lines.size + 2) { Octopus(0, false, isOuter = true) } }

    for (i in lines.indices) {
        for (j in lines.indices) {
            matrix[i + 1][j + 1] = Octopus(lines[i][j].digitToInt(), false, isOuter = false)
        }
    }
    return matrix
}

fun Array<Array<Octopus>>.flashOneIteration() {
    this.forAllIndexed { i, j ->
        if (readyToFlash) {
            flash()
            increaseSurroundingOnes(i, j)
        }
    }
}

fun Array<Array<Octopus>>.forAll(dothis: Octopus.() -> Unit) {
    this.forEachIndexed { i, row ->
        row.forEachIndexed { j, octopus ->
            octopus.dothis()
        }
    }
}

fun Array<Array<Octopus>>.forAllIndexed(dothis: Octopus.(i: Int, j: Int) -> Unit) {
    this.forEachIndexed { i, row ->
        row.forEachIndexed { j, octopus ->
            octopus.dothis(i, j)
        }
    }
}

private fun Array<Array<Octopus>>.increaseSurroundingOnes(i: Int, j: Int) {
    this[i - 1][j - 1] += 1
    this[i - 1][j] += 1
    this[i - 1][j + 1] += 1
    this[i][j - 1] += 1
    this[i][j + 1] += 1
    this[i + 1][j - 1] += 1
    this[i + 1][j] += 1
    this[i + 1][j + 1] += 1
}

fun Array<Array<Octopus>>.hasOctopussesReadyToFlash(): Boolean {
    return this.any { row -> row.any { it.readyToFlash } }
}

fun Array<Array<Octopus>>.allOctopussesJustFlashed(): Boolean {
    return this.all { row -> row.all { it.flashedThisRound || it.isOuter } }
}


fun Array<Array<Octopus>>.getAsString(): String {
    return this.mapNotNull { row ->
        if (row.all { it.isOuter }) null else {
            "\n" + row.mapNotNull {
                if (it.isOuter) null
                else it.toString()
            }
        }
    }.toString()
}

data class Octopus(var energy: Int, var flashedThisRound: Boolean, val isOuter: Boolean) {
    override fun toString(): String {
        return energy.toString() + if (flashedThisRound) "+" else "-"
    }

    val readyToFlash get() = !isOuter && energy > 9

    fun flash() {
        if (!isOuter) {
            globalFlashCounter++
            energy = 0
            flashedThisRound = true
        }
    }
}

operator fun Octopus.plusAssign(plus: Int) {
    if (!flashedThisRound && !isOuter) {
        this.energy += plus
    }
}