fun main() {
    val lines = loadFile("day9.txt").readLines()

    val rows = lines.size
    val columns = lines[0].length

    val matrix = Array(rows + 2) { Array(columns + 2) { 9 } }

    //populate
    for (i in 0 until rows) {
        for (j in 0 until columns) {
            matrix[i + 1][j + 1] = lines[i][j].digitToInt()
        }
    }

    val basins = mutableListOf<Int>()

    //find low-points
    val lowPoints = mutableListOf<Int>()
    for (i in 1..rows) {
        for (j in 1..columns) {
            val min = minOf(matrix[i + 1][j], matrix[i - 1][j], matrix[i][j + 1], matrix[i][j - 1])
            if (matrix[i][j] < min) {
                //found local minimum
                val visitedLocations = mutableSetOf<Pair<Int, Int>>()
                val size = matrix.countNumInBasin(row = i, column = j, visitedLocations)
                basins.add(size)
                lowPoints.add(matrix[i][j])
                println("basin complete, new one next")
            }
        }
    }

    println("result1 = ${lowPoints.sumOf { it + 1 }}")
    val result2 = basins.asSequence().sortedDescending().take(3).reduce { a, b -> a * b }
    println("result2: $result2")
}

private fun Array<Array<Int>>.countNumInBasin(
    row: Int,
    column: Int,
    visitedLocations: MutableSet<Pair<Int, Int>>
): Int {
    visitedLocations.add(Pair(row, column))
    if (this[row][column] == 9) return 0

    val numAbove = if (visitedLocations.contains(Pair(row - 1, column))) {
        0
    } else {
        visitedLocations.add(Pair(row - 1, column))
        this.countNumInBasin(row - 1, column, visitedLocations)
    }

    val numBelow = if (visitedLocations.contains(Pair(row + 1, column))) {
        0
    } else {
        visitedLocations.add(Pair(row + 1, column))
        this.countNumInBasin(row + 1, column, visitedLocations)
    }

    val numLeft = if (visitedLocations.contains(Pair(row, column - 1))) {
        0
    } else {
        visitedLocations.add(Pair(row, column - 1))
        this.countNumInBasin(row, column - 1, visitedLocations)
    }

    val numRight = if (visitedLocations.contains(Pair(row, column + 1))) {
        0
    } else {
        visitedLocations.add(Pair(row, column + 1))
        this.countNumInBasin(row, column + 1, visitedLocations)
    }

    val result = 1 + numAbove + numBelow + numLeft + numRight
    println("returning from: matrix[$row][$column] = ${this[row][column]} with $result")
    return result
}