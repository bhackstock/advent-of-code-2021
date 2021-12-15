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

    //find low-points
    val lowPoints = mutableListOf<Int>()
    for (i in 1..rows) {
        for (j in 1..columns) {
            val min = minOf(matrix[i + 1][j], matrix[i - 1][j], matrix[i][j + 1], matrix[i][j - 1])
            if (matrix[i][j] < min) {
                //found local minima
                lowPoints.add(matrix[i][j])
            }
        }
    }

    println(lowPoints.sumOf { it + 1 })

//    for (i in 0 until rows) {
//        for (j in 0 until columns) {
//            matrix[i][j] = lines[i][j].digitToInt()
//        }
//    }


    println()
}