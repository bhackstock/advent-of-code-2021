import java.io.File

class Resources

fun loadFile(filename: String): File {
    return File("tempFile").apply {
        Resources::class.java.getResourceAsStream(filename).copyTo(this.outputStream())
    }
}