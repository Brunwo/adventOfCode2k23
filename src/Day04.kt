import java.util.LinkedList
import kotlin.math.pow

fun main() {

    fun winCount(line: String): Int {
        val (allWinning, ownWinning) = line.substring(line.indexOf(':') + 1).split('|')
            .map { it.trim().split(' ').filter { it.isNotEmpty() }.map { it.toInt() }.toMutableList() }
        ownWinning.retainAll(allWinning)
        return ownWinning.size
    }

    fun part1(input: List<String>): Int {
        return input.sumOf sum@{
            winCount(it).toLong().let { 2.0.pow(it - 1.0).toInt().let { it.println(); return@sum it } }
        }
    }

    fun part2(input: List<String>): Int {

        val wonCardsStack: LinkedList<Int> = LinkedList()

        return input.sumOf sum@{
            val win = winCount(it)
            var currentCardCount = 1

            if (!wonCardsStack.isEmpty()) {
                currentCardCount = wonCardsStack.remove() + 1
            }

            for (i in 0..<win) {
                if (wonCardsStack.size <= i)
                    wonCardsStack.add(currentCardCount)
                else
                    wonCardsStack[i] = wonCardsStack.getOrElse(i) { currentCardCount } + currentCardCount
            }

            "had $currentCardCount cards of $it, stack is now $wonCardsStack ".println()
            return@sum currentCardCount
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    part1(testInput).println()
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    "part1:".println()
    part1(readInput("Day04")).println()
    check(part1(readInput("Day04")) == 25004)

    "part2:".println()
    part2(readInput("Day04")).println()
    check(part2(readInput("Day04")) == 14427616)
}
