import java.lang.Integer.max
import java.lang.Math.min

fun main() {
    val file = loadFile("day5.txt")

    val lines: List<Line> = file.readLines().map {
        with(it.split(" -> ")) {
            Line(this[0].toPair(), this[1].toPair())
        }
    }

    val valid = lines.filter { it.isStraight || it.is45DegreeDiagonal }
    println("filtered, before ${lines.size}, after ${valid.size}")


    val field = VentureField(1000)
    valid.forEach { field.markVent(it) }
//    println(field)w
    val hotspots = field.getCellsOver(1).size
    println("num of hotspots: $hotspots")

//    println(field)
}

fun String.toPair(): Point {
    return with(this.split(",")) {
        Point(this[0].toInt(), this[1].toInt())
    }
}

data class Point(val x: Int, val y: Int)

data class VentureField(val size: Int = 1000) {
    //field[y][x]
    val field = Array(size) { Array(size) { 0 } }


    fun markVent(line: Line) {
        if (line.start.x == line.end.x) {
            //is a column
            val min = min(line.start.y, line.end.y)
            val max = max(line.start.y, line.end.y)
            for (i in min..max) {
                field[i][line.end.x]++
            }
        } else if (line.start.y == line.end.y) {
            //is a row
            val min = min(line.start.x, line.end.x)
            val max = max(line.start.x, line.end.x)
            for (i in min..max) {
                field[line.start.y][i]++
            }
        } else {
            //is diagonal

            //upward or downward line
            val minX = min(line.start.x, line.end.x)
            val maxX = max(line.start.x, line.end.x)
            val minY = min(line.start.y, line.end.y)
            val maxY = max(line.start.y, line.end.y)
            val diff = maxX - minX

            if (minY == line.start.y && minX == line.start.x) {
                //downwards right
                for (i in 0..diff) {
                    field[line.start.y + i][line.start.x + i]++
                }
            } else if (minY == line.start.y && minX == line.end.x) {
                //down left
                for (i in 0..diff) {
                    field[line.start.y + i][line.start.x - i]++
                }
            } else if (minY == line.end.y && minX == line.start.x) {
                //up right
                for (i in 0..diff) {
                    field[line.start.y - i][line.start.x + i]++
                }
            } else if (minY == line.end.y && minX == line.end.x) {
                //up left
                for (i in 0..diff) {
                    field[line.start.y - i][line.start.x - i]++
                }
            }
        }
    }

    fun getAllCells(): List<Int> {
        return field.flatMap { row -> row.toList() }
    }

    fun getCellsOver(moreThan: Int): List<Int> {
        return getAllCells().filter { it > moreThan }
    }

    override fun toString(): String {
        return field.map { row -> "\n" + row.map { it.toString() } }.toString()
    }
}

data class Line(val start: Point, val end: Point) {
    val isStraight
        get() = start.x == end.x || start.y == end.y

    val is45DegreeDiagonal: Boolean
        get() {
            val diffX = max(start.x, end.x) - min(start.x, end.x)
            val diffY = max(start.y, end.y) - min(start.y, end.y)
            return diffX == diffY
        }
}

