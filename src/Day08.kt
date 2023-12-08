import java.util.function.Predicate

//https://adventofcode.com/2023/day/8

//Math Helpers
fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0.toLong() && lcm % b == 0.toLong()) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}
fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}

data class CommandsAndPath(val command: String, val path: Map<String, Pair<String, String>>) {

    fun traverse(from : String, toCondition : Predicate<String>) : Int {

        var currentNode = from
        var  steps  = 0

        while(true) {
            command.forEach { c ->
                steps++
                currentNode = if (c == 'L')
                    path[currentNode]!!.first
                else
                    path[currentNode]!!.second

                if(toCondition.test(currentNode))
                    return steps
            }
        }
    }
}

fun parse(input: List<String>): CommandsAndPath {

    return CommandsAndPath(
        input[0],
        input.drop(2).associate {
            it.split(" ")[0] to  (it.substring(7, 10) to  it.substring(12, 15))
        }
    )
}

fun main() {

    fun part1(input: List<String>): Int {
        return  parse(input).traverse(from = "AAA", toCondition = { it == "ZZZ" })
    }

    fun part2(input: List<String>): Long {

        val game = parse(input)
        val walks =
            game.path.map { it.key }.filter { it. endsWith('A')}.map { start ->
            game.traverse(from = start, toCondition = {it.endsWith('Z')})
        }

        return findLCMOfListOfNumbers(walks.map { it.toLong() })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 6)

    check(part1(readInput("Day08")) == 16271) { "part 1" }
    check(part2(readInput("Day08")) == 14265111103729)

    "part1:".println()
    part1(readInput("Day08")).println()
    "part2:".println()
    part2(readInput("Day08")).println()
}
