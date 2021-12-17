import java.io.File

class Resources

fun loadFile(filename: String): File {
    return File("tempFile").apply {
        Resources::class.java.getResourceAsStream(filename).copyTo(this.outputStream())
    }
}

fun <T> Array<Array<T>>.print() {
    val string = map { row -> "\n" + row.map { it.toString() } }.toString()
    println(string)
}

fun <T> List<List<T>>.print() {
    val string = map { row -> "\n" + row.map { it.toString() } }.toString()
    println(string)
}