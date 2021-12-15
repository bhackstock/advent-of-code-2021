import java.util.regex.Pattern

fun main() {
    day4ex2()
}

fun day4ex1() {
    val file = loadFile("day4.txt")
    val lines: List<String> = file.readLines()
    val bingoTips = lines[0].split(",").map { it.toInt() }.toMutableList()
    val fields = parseAllFields(lines)

    var bingo = fields.any { it.isBingo() }
    var i = 0
    while (!bingo) {
        fields.forEach { it.markIfPresent(bingoTips[i]) }
        println("called: ${bingoTips[i]}")
        bingo = fields.any { it.isBingo() }
        i++
    }
    println("found a bingo after $i numbers")

    val winner = fields.first { it.isBingo() }
    println(winner)
    val sum = winner.getUnmarkedCells().sumOf { it.number }
    val lastNum = bingoTips[i - 1]
    println("sum $sum, last number $lastNum, total: ${sum * lastNum}")
}

fun day4ex2() {
    val file = loadFile("day4.txt")
    val lines: List<String> = file.readLines()
    val bingoTips = lines[0].split(",").map { it.toInt() }.toMutableList()
    val fields = parseAllFields(lines)

    var numWon = fields.filter { it.isBingo() }.count()
    var i = 0
    while (numWon != fields.size - 1) {
        fields.forEach { it.markIfPresent(bingoTips[i]) }
        println("called: ${bingoTips[i]}")
        numWon = fields.filter { it.isBingo() }.count()
        i++
    }
    println("only one board left")
    val looser = fields.first { !it.isBingo() }
    println(looser)

    while (!looser.isBingo()) {
        looser.markIfPresent(bingoTips[i])
        println("called: ${bingoTips[i]}")
        i++
    }
    val sum = looser.getUnmarkedCells().sumOf { it.number }
    val lastNum = bingoTips[i - 1]
    println("sum $sum, last number $lastNum, total: ${sum * lastNum}")
    println(looser)
}

class BingoCell(val number: Int, var called: Boolean) {
    override fun toString(): String {
        return if (!called) number.toString() else "_"
    }
}

class BingoField(val size: Int = 5) {
    private var field: Array<Array<BingoCell>> = Array(size) {
        Array(size) {
            BingoCell(-1, false)
        }
    }

    constructor(numbers: List<Int>) : this() {
        numbers.forEachIndexed { index, i ->
            field[index / size][index % size] = BingoCell(i, false)
        }
    }

    fun markIfPresent(number: Int) {
        field.forEach { row ->
            row.forEach { cell ->
                if (cell.number == number) {
                    cell.called = true
                }
            }
        }
    }

    fun isBingo(): Boolean {
        val rowIsBingo = field.any { row -> row.all { it.called } }

        var columnIsBingo = false
        for (i in 0 until size) {
            var allCols = true
            for (j in 0 until size) {
                if (!field[j][i].called) {
                    allCols = false
                }
            }
            if (allCols) {
                columnIsBingo = true
            }
        }

        return rowIsBingo || columnIsBingo
    }

    fun getAllCells(): List<BingoCell> {
        return field.flatMap { row -> row.toList() }
    }

    fun getUnmarkedCells(): List<BingoCell> {
        return getAllCells().filter { !it.called }
    }

    override fun toString(): String {
        return field.map { row -> "\n" + row.map { it.toString() } }.toString()
    }
}

fun parseField(numbers: List<String>, startIndex: Int, size: Int = 5): BingoField {
    val content: List<Int> = numbers.subList(startIndex, startIndex + size)
        .joinToString(separator = " ") { " $it " }.trim()
        .split(Pattern.compile("""\s\s*""")).map { it.toInt() }

    return BingoField(content)
}

private fun parseAllFields(lines: List<String>) = lines.mapIndexedNotNull { index: Int, _ ->
    if (isFirstLineOfFieldData(index) && isSpaceInArray(index, lines)) {
        parseField(lines, index)
    } else null
}

private fun isFirstLineOfFieldData(index: Int) = ((index - 2) % 6 == 0)

private fun isSpaceInArray(index: Int, lines: List<String>) =
    ((index + 5) <= lines.size)
