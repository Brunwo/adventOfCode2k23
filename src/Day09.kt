
    fun part1(input: List<String>): Int {

        val entries = input.map { line -> line.split(' ').map { it.toInt() } }

        return entries.sumOf { line ->
            val seq = generateSequence(line.reversed()) {
                it.zipWithNext().map { (a, b) ->
                        val diff = a - b
                        "$a - $b = $diff".println()
                        diff
                    }
            }.takeWhile { it.any { it != 0 } }
            val newValue = seq.map { it.first() }
            seq.println()
            newValue.println()
            newValue.sum()
        }
    }

    fun part2(input: List<String>): Int {

        val entries = input.map { line ->
            "line $line".println()
            line.split(' ').map { it.toInt() } }
        val placeholders = emptyList<Int>()

        return entries.sumOf { line ->
            val seq = generateSequence(line) {
                it
                    .zipWithNext().map { (a, b) ->
                        val diff = a - b
                        "$a - $b = $diff".println()
                        diff
                    }

            }.takeWhile {
                "evaluating any $it"    .println()
                it.any { it != 0 } } // zip with next, skip unnecessary evaluations ?
            "sequence >".println()
            val newValue = seq.map { it.first().also { it.println() } }
            newValue.sum()
        }
    }

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    part2(readInput("Day09"))
//
    "part1:".println()
    part1(readInput("Day09")).println()
    "part2:".println()
    part2(readInput("Day09")).println()
}