import kotlin.math.pow

fun main() {
    ex2()
}

private fun ex2() {
    val file = loadFile("day3.txt")
    val oxygen = filterOxygen(0, file.readLines())
    val co2 = filterC02(0, file.readLines())
    println("result: $oxygen")
    println("result: $co2")

    assert(oxygen.size == 1 && co2.size == 1)

    val oxygenInt =
        oxygen[0].toList().map { it.digitToInt() }.reversed()
            .mapIndexed { index, element -> element * (2.toDouble().pow(index)) }.sum().toInt()
    val co2Int =
        co2[0].toList().map { it.digitToInt() }.reversed()
            .mapIndexed { index, element -> element * (2.toDouble().pow(index)) }.sum().toInt()

    println("result: $oxygenInt")
    println("result: $co2Int")

    println("final result ${oxygenInt * co2Int}")
}


fun filterOxygen(level: Int, numbers: List<String>): List<String> {
    print("filter Oxygen: ")
    val numItems = numbers.size
    val num0 = numbers.map { it[level].digitToInt() }.filter { it == 0 }.size
    val num1 = numbers.map { it[level].digitToInt() }.filter { it == 1 }.size
    println("level: $level, current items: $numItems, ($num0, $num1)")

    val filtered = if (num1 > num0) {
        numbers.filter { it[level] == '1' }
    } else if (num0 > num1) {
        numbers.filter { it[level] == '0' }
    } else {
        numbers.filter { it[level] == '1' }
    }

    return if (filtered.size <= 1) {
        filtered
    } else filterOxygen(level + 1, filtered)
}

fun filterC02(level: Int, numbers: List<String>): List<String> {
    val numItems = numbers.size
    val num0 = numbers.map { it[level].digitToInt() }.filter { it == 0 }.size
    val num1 = numbers.map { it[level].digitToInt() }.filter { it == 1 }.size
    println("filter co2\tlevel: $level, current items: $numItems, ($num0, $num1)")

    val filtered = if (num1 < num0) {
        numbers.filter { it[level] == '1' }
    } else if (num0 < num1) {
        numbers.filter { it[level] == '0' }
    } else {
        numbers.filter { it[level] == '0' }
    }

    return if (filtered.size == 1) {
        filtered
    } else filterC02(level + 1, filtered)
}

private fun ex1() {
    val file = loadFile("day4.txt")

    val sums = Array(12) { 0 }
    val numlines = file.readLines().size
    file.forEachLine {
        for (i in 0 until 12) {
            sums[i] += if (it[i] == '1') 1 else 0
        }
    }

    println("entries: $numlines")
    sums.forEach { print("$it,") }
    println()

    val first = sums.map { if (it > (numlines / 2)) 1 else 0 }
    val second = first.map { if (it == 1) 0 else 1 }

    println(first)
    println(second)


    val num1 = first.reversed().mapIndexed { index, element -> element * (2.toDouble().pow(index)) }.sum().toInt()
    val num2 = second.reversed().mapIndexed { index, element -> element * (2.toDouble().pow(index)) }.sum().toInt()

    println(num1)
    println(num2)

    println("result: ${num1 * num2}")
}
