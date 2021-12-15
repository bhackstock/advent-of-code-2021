import java.util.*


val symbols = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)

val points = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)
val pointsEx2 = mapOf(
    ')' to 1L,
    ']' to 2L,
    '}' to 3L,
    '>' to 4L
)

fun Char.toClosingChar() = symbols[this]

fun Char.isOpeningChar() = listOf('(', '[', '{', '<').any { it == this }

fun main() {
    day10_ex2()
}

private fun day10_ex1() {
    val lines = loadFile("day10.txt").readLines()

    val result = lines.map { line ->
        val stack = Stack<Char>()

        line.mapNotNull { char ->
            if (char.isOpeningChar()) {
                stack.push(char)
                null
            } else {
                val fromStack = stack.pop()
                if (fromStack.toClosingChar() != char) {
                    //found conflict
                    println("expected: $fromStack, but found $char")
                    points[char]
                } else null //everything ok, moving on
            }

        }
    }.flatten().sum()
    println(result)
}

private fun day10_ex2() {
    val lines = loadFile("day10.txt").readLines()

    val incompleteLines = lines.filter { line ->
        val stack = Stack<Char>()
        line.forEach { char ->
            if (char.isOpeningChar()) {
                stack.push(char)
            } else {
                val fromStack = stack.pop()
                if (fromStack.toClosingChar() != char) {
                    //found conflict
                    return@filter false
                }
            }
        }
        return@filter true
    }


    val completingChars = incompleteLines.map { line ->
        val stack = Stack<Char>()
        line.forEach { char ->
            if (char.isOpeningChar()) {
                stack.push(char)
            } else stack.pop() //we know only incomplete ones left!
        }
        stack.toList().mapToClosingChars()
    }

    val values = completingChars.map { line: List<Char> ->
        var sum = 0L
        line.forEach { char ->
            sum *= 5L
            sum += pointsEx2[char]!!
        }
        sum
    }

    val result = values.sorted()[values.size / 2]


    println(incompleteLines)
    println(completingChars)
    println(values)
    println(result)
}

private fun List<Char>.mapToClosingChars(): List<Char> {
    return this.mapNotNull { symbols[it] }.reversed()
}
