/*
    0 -> 6
    1 -> 2
    2 -> 5
    3 -> 5
    4 -> 4
    5 -> 5
    6 -> 6
    7 -> 3
    8 -> 7
    9 -> 6
 */

val map = mapOf(
    setOf(1, 2, 3, 5, 6, 7) to 0,
    setOf(3, 6) to 1,
    setOf(1, 3, 4, 5, 7) to 2,
    setOf(1, 3, 4, 6, 7) to 3,
    setOf(2, 3, 4, 6) to 4,
    setOf(1, 2, 4, 6, 7) to 5,
    setOf(1, 2, 4, 5, 6, 7) to 6,
    setOf(1, 3, 6) to 7,
    setOf(1, 2, 3, 4, 5, 6, 7) to 8,
    setOf(1, 2, 3, 4, 6, 7) to 9,
)


fun main() {
    val file = loadFile("day8.txt")
    val lines = file.readLines().map {
        with(it.split(" | ")) {
            Pair(this[0].trim().split(" "), this[1].trim().split(" "))
        }
    }


    val outputs = lines.map { (input, output) ->
        val segments = getSegments()

        //number one, 2 segments needed
        val segmentsForNumber1 = input.segmentForNumberByNumSegments(2)
        segments(3).possibleNumbers = segmentsForNumber1
        segments(6).possibleNumbers = segmentsForNumber1
        segments.except(3, 6).forEach {
            it.possibleNumbers -= segmentsForNumber1
        }

        //number seven
        val segmentsForNumber7 = input.segmentForNumberByNumSegments(3)
        val segment1 = segmentsForNumber7.minus(segmentsForNumber1).single()
        segments(1).possibleNumbers = setOf(segment1)
        segments.except(1).forEach {
            it.possibleNumbers -= segment1
        }

        //find segment 6 by intersection of numbers 0,6,9 unified with number 1
        val zeroSixNine = input.filter { it.length == 6 }.map { it.split("").toSet().minus("") }
        val union = zeroSixNine[0].intersect(zeroSixNine[1]).intersect(zeroSixNine[2])

        val segment6 = union.intersect(segmentsForNumber1)
        segments(6).possibleNumbers = segment6
        segments(3).possibleNumbers -= segment6

        //number 4
        val segmentsForNumber4 = input.segmentForNumberByNumSegments(4)
        val possibleSegmentsFor2And4 = segmentsForNumber4.minus(segmentsForNumber1)
        segments(2).possibleNumbers = possibleSegmentsFor2And4
        segments(4).possibleNumbers = possibleSegmentsFor2And4
        segments.except(2, 4).forEach { it.possibleNumbers -= possibleSegmentsFor2And4 }

        //find seg 4 by intersection with numbers 0
        val segmentsForNumber0 = zeroSixNine
            .filter { it.intersect(segmentsForNumber1).size == 2 } //get rid of 6
            .first { it.intersect(segmentsForNumber4).size == 3 } //get rid of 9
        val segment4 = input.segmentForNumberByNumSegments(7) - segmentsForNumber0

        segments(4).possibleNumbers = segment4
        segments(2).possibleNumbers -= segment4

        //segment 7 by getting number 9
        val segmentsForNumber9 = zeroSixNine
            .filter { it.intersect(segmentsForNumber1).size == 2 } //get rid of 6
            .first { it.intersect(segmentsForNumber4).size != 3 } //

        val segment7 = segmentsForNumber9.intersect(segments(7).possibleNumbers)
        segments(7).possibleNumbers = segment7
        segments(5).possibleNumbers -= segment7


        val numbers = output.map {
            val segmentNumbers = it.split("").toSet().minus("")
                .map { letter ->
                    val matchingSegment = segments.first { segment -> segment.possibleNumbers.first() == letter }
                    matchingSegment.number
                }.toSet()
            map[segmentNumbers]
        }

        var string = ""
        for (element in numbers) {
            string += element
        }
        val num = string.toInt()
        num
    }

    println(outputs.sum())
}

private fun MutableList<Segment>.except(vararg numbers: Int): List<Segment> {
    return this.filter { segment -> !numbers.any { segment.number == it } }
}

private fun List<String>.segmentForNumberByNumSegments(number: Int): Set<String> {
    return this.first { it.length == number }.split("").toSet().minus("")
}


operator fun MutableList<Segment>.invoke(number: Int): Segment {
    return this.first { it.number == number }
}

fun getSegments(): MutableList<Segment> {
    return mutableListOf(
        Segment(1, setOf("a", "b", "c", "d", "e", "f", "g")),
        Segment(2, setOf("a", "b", "c", "d", "e", "f", "g")),
        Segment(3, setOf("a", "b", "c", "d", "e", "f", "g")),
        Segment(4, setOf("a", "b", "c", "d", "e", "f", "g")),
        Segment(5, setOf("a", "b", "c", "d", "e", "f", "g")),
        Segment(6, setOf("a", "b", "c", "d", "e", "f", "g")),
        Segment(7, setOf("a", "b", "c", "d", "e", "f", "g")),
    )
}

data class Segment(val number: Int, var possibleNumbers: Set<String>)
