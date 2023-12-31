import java.util.*

// solution moves pointers on part/gears/labels from left of the screen to the right, using a 3 lines window
// checking middle line for scoring and surrounding lines to analyze the context
fun main() {
    data class Part(val startIndex:Int , val number : Int, val endIndex : Int)

    data class Line(val data :String) {

        val labels =
             Regex("[^0-9.]")
                .findAll(data)
                .map { it.range.first }.toSortedSet()

        val gears = Regex("[*]")
            .findAll(data)
            .map { it.range.first }.toSortedSet()

        val parts =
            Regex("[0-9]+")
                .findAll(data)
                .map {Part(it.range.first,it.value.toInt(), it.range.last) }
    }

    class WorkWindow {

        val newLine = Line("")

        val buffer = arrayOf(newLine, newLine, newLine)
        var circularIndex = 0

        fun lineAbove() = buffer[(circularIndex + 1) % 3]
        fun middleLine() = buffer[(circularIndex + 2) % 3]
        fun lineUnder() = buffer[circularIndex]

        fun addLine(l: Line) {
            circularIndex = ++circularIndex % 3
            buffer[circularIndex] = l
        }

        fun scanPreviousLineToSum(): Int {

            if (!middleLine().parts.any())
                return 0

            var surroundingLabels = Collections.emptySortedSet<Int>().toMutableSet()
            if (!lineAbove().labels.isEmpty())
                surroundingLabels.addAll(lineAbove().labels.asIterable())

            if (!middleLine().labels.isEmpty())
                surroundingLabels.addAll(middleLine().labels.asIterable())

            if (!lineUnder().labels.isEmpty())
                surroundingLabels.addAll(lineUnder().labels.asIterable())

            surroundingLabels = surroundingLabels.toSortedSet()

            val labelsI = surroundingLabels.iterator()
            var watermarkIndex = labelsI.next()
            var sum = 0

            middleLine().parts.forEach { p ->
                val i = p.startIndex
                while (watermarkIndex < i - 1 && labelsI.hasNext()) {
                    watermarkIndex = labelsI.next()
                }
                if (watermarkIndex >= i - 1 &&
                    watermarkIndex <= p.endIndex + 1
                ) {
                    sum += p.number
                }
            }
            return sum
        }

        fun scanMiddleLineGearRatioPart2(): Int {

            if (!middleLine().gears.any())
                return 0

            val surroundingParts  = Collections.emptySortedSet<Part>().toMutableSet()
            if (lineAbove().parts.any())
                surroundingParts.addAll(lineAbove().parts)

            if (middleLine().parts.any())
                surroundingParts.addAll(middleLine().parts)

            if (lineUnder().parts.any())
                surroundingParts.addAll(lineUnder().parts)

            val allParts = surroundingParts.toMutableList().sortedBy { it.startIndex }

            val partsI = allParts.iterator()
            var currentPartEvaluation = partsI.next()

            return middleLine().gears.sumOf sumLine@{

                var firstPartFound: Part? = null

                while (it >= currentPartEvaluation.startIndex - 1) {

                    if (it <= currentPartEvaluation.endIndex + 1
                    ) {
                        if (firstPartFound != null && currentPartEvaluation != firstPartFound) {
                            return@sumLine firstPartFound.number * currentPartEvaluation.number
                        } else {
                            firstPartFound = currentPartEvaluation
                        }
                    }

                    if (partsI.hasNext()) {
                        currentPartEvaluation = partsI.next()
                    }else {
                        return@sumLine 0
                    }
                }

"""
    lines : containing  ${middleLine().gears.size} gears no sum found for gear at pos ${it}
    ${"".padEnd(200).replaceRange(it,it+1,"!")}
    ${lineAbove().data}
    ${middleLine().data.replaceRange(it,it+1,"⊚")}
    ${lineUnder().data}
    
""".println()
                return@sumLine 0
            }
        }
    }

    val workWindowAccumulator = WorkWindow()

    fun part1(input: List<String>): Int {
        return input.union(listOf("")).sumOf {
            workWindowAccumulator.addLine(Line(it))
            workWindowAccumulator.scanPreviousLineToSum()
        }
    }

    fun part2(input: List<String>): Int {
         return input.union(listOf("")).sumOf {
            workWindowAccumulator.addLine(Line(it))
            workWindowAccumulator.scanMiddleLineGearRatioPart2()
        }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    "part1:".println()
    part1(readInput("Day03")).println()

    "part2:".println()
    part2(readInput("Day03")).println()
    check(part2(readInput("Day03")) == 83279367)
    }


