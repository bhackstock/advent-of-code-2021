import java.lang.Math.abs

fun main() {
    val file = loadFile("day7.txt")
    val crabs = file.readLines()[0].split(",").map { it.toInt() }
    val max = crabs.maxOrNull()!!
    println(max)

    val possiblePositions = Array(max) { 0 }
    possiblePositions.forEachIndexed { position, _ ->
        var sumSteps = 0
        crabs.forEach {
            sumSteps += abs(position - it).getFuel()
        }
        possiblePositions[position] = sumSteps
    }

    val min = possiblePositions.minOrNull()
    println("smallest cost $min")
}


fun Int.getFuel(): Int {
    val int: Int = 0
    var sum = 0
    for (i in this downTo 0) {
        sum += i
    }
    return sum
}