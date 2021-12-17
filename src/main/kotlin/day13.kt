fun main() {
    var paper = getPaper()
    val folds = getFolds()

    paper.print()
    folds.forEach {
        paper = paper.foldPaper(it)
    }

    paper.print()
    val dots = paper.flatMap { it.filter { it == '#' } }.size
    println(dots)
}

private fun List<List<Char>>.foldPaper(instruction: Pair<String, Int>): List<List<Char>> {
    println("fold along ${instruction.first}, at ${instruction.second}")

    return if (instruction.first == "y") {
        //fold up
        val firstHalf = subList(0, instruction.second)
        val secondHalf = subList(instruction.second + 1, this.size)

        secondHalf.reversed().zip(firstHalf) { a, b ->
            a.zip(b) { char1, char2 -> if (char1 == '#') char1 else char2 }
        }
    } else {
        //fold right
        val firstHalf = map { it.subList(0, instruction.second) }
        val secondHalf = map { it.subList(instruction.second + 1, it.size) }

        secondHalf.map { it.reversed() }.zip(firstHalf) { a, b ->
            a.zip(b) { char1, char2 -> if (char1 == '#') char1 else char2 }
        }
    }
}

fun getPaper(): List<List<Char>> {
    val coordinates =
        loadFile("day13.txt").readLines()
            .filter { it.contains(",") }
            .map {
                val split = it.split(",")
                Pair(split[1].toInt(), split[0].toInt())
            }

    val numRows = coordinates.maxByOrNull { it.first }!!.first
    val numCols = coordinates.maxByOrNull { it.second }!!.second

    val matrix: MutableList<MutableList<Char>> =
        Array(numRows + 1) { Array(numCols + 1) { '_' }.toMutableList() }.toMutableList()

    coordinates.forEach {
        matrix[it.first][it.second] = '#'
    }
    return matrix
}

fun getFolds(): List<Pair<String, Int>> {
    return loadFile("day13.txt").readLines()
        .filter { it.contains("=") }
        .map {
            val split = it.removePrefix("fold along ").split("=")
            Pair(split[0], split[1].toInt())
        }
}