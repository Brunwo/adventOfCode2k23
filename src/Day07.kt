fun Map<Char, Int>.reverse() = map { (key, value) -> value to key }.toMap()

interface Rules {
    var cardValues: Map<Char, Int>
}

object Part1 : Rules {
    override var cardValues: Map<Char, Int> = linkedMapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'J' to 11,
        'T' to 10,
        '9' to 9,
        '8' to 8,
        '7' to 7,
        '6' to 6,
        '5' to 5,
        '4' to 4,
        '3' to 3,
        '2' to 2,
    )
}

object Part2 : Rules {
    override var cardValues: Map<Char, Int> = Part1.cardValues.toMutableMap().also { it['J'] = 1 }
}

data class CardGroup(val value: Int, var count: Int) : Comparable<CardGroup> {
    override fun compareTo(other: CardGroup): Int {
        if (count == other.count)
            return value.compareTo(other.value)
        return count.compareTo(other.count)
    }
}

data class Hand(val hand: String, val score: Int, val useJoker: Boolean = false) : Comparable<Hand> {

    private val jokers = hand.count { it == 'J' }
    private val rules = if (!useJoker) Part1 else Part2
    private val sortedHand = if (!useJoker) sortHandPt1() else getSortedHandPart2()

    private fun sortHandPt1() = hand.map { card ->
        CardGroup(rules.cardValues[card]!!, hand.count { it == card })
    }.toSortedSet().reversed()

    private fun getSortedHandPart2(): List<CardGroup> {

        val sortedHand = sortHandPt1().toMutableList()

        if (sortedHand[0].value != rules.cardValues['J'])
            sortedHand[0].count += jokers

        //Js are first, so second entry can be improved
        else if (sortedHand.size > 1) {
            sortedHand[1].count += jokers
            sortedHand[0] = sortedHand[1].also { sortedHand[1] = sortedHand[0] }
        }

        sortedHand.removeIf { sortedHand[0] != it && it.value == rules.cardValues['J'] }
        return sortedHand
    }

    // we square to put high cardinality families on top, ie 4 same cards > 3+2)
    private val familyScore = sortedHand.sumOf { it.count * it.count - 1 }
    private val unsortedHandValues = hand.map { card -> rules.cardValues[card]!! }

    fun printSorted() {
        sortedHand.forEach {
            print(rules.cardValues.reverse()[it.value].toString().repeat(it.count))
        }
        println("")
    }

    override fun compareTo(other: Hand): Int {

        val scoreCompare = familyScore.compareTo(other.familyScore)
        if (scoreCompare != 0)
            return scoreCompare

        unsortedHandValues.forEachIndexed { index, value ->
            val compare = value.compareTo(other.unsortedHandValues[index])
            if (compare != 0)
                return compare
        }
        return 0
    }
    //poker rules, not OK
//        sortedHand.forEachIndexed { index, valueAndCount ->
//            val res = valueAndCount.compareTo(other.sortedHand.elementAt(index))
//                if (res != 0)
//                    return res
//            }
//        return 0
}

fun main() {

    fun parse(input: List<String>): Map<Int, String> {
        return buildMap<Int, String> {
            input.forEach { line ->
                put(line.split(' ')[1].toInt(), line.split(' ')[0])
            }
        }
    }

    fun List<Hand>.getTotalScore(): Long {
        var total: Long = 0
        this.sorted().forEachIndexed { index, hand ->
            val score = hand.score * (index + 1)
            total += score
        }
        return total
    }

    fun part1(input: List<String>): Long {
        return parse(input).map { (score, hand) -> Hand(hand, score) }.getTotalScore()
    }

    fun part2(input: List<String>): Long {
        return parse(input).map { (score, hand) -> Hand(hand, score, useJoker = true) }.getTotalScore()
    }

    // test if implementation meets criteria from the description, like:
    //
    val testInput = readInput("Day07_test")
    check(part1(testInput).toInt() == 6440) // (765 * 1 + 220 * 2 + 28 * 3 + 684 * 4 + 483 * 5)
    check(part2(testInput).toInt() == 5905)


    check(part1(readInput("Day07")).toInt() == 246795406) { "part 1" }
    check(part2(readInput("Day07")).toInt() == 249356515) { "part 2" }

    "part1:".println()
    part1(readInput("Day07")).println()
    "part2:".println()
    part2(readInput("Day07")).println()
}
