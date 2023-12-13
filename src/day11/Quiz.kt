package day11

import println
import readInput
import readInputFromLauncher
import kotlin.math.abs

data class Image(val galaxies: List<Pair<Long, Long>>, val colsExp: Set<Long>, val rowsExp: Set<Long>)

fun parse(input: List<String>): Image {

    val galaxies = mutableListOf<Pair<Long, Long>>()

    input.forEachIndexed { indexY, line ->
        line.forEachIndexed { indexX, c ->
            if (c == '#')
                galaxies.add(Pair(indexX.toLong(), indexY.toLong()))
        }
    }

    val rowsExp =
        buildSet {
            val allY = galaxies.map { it.second }.toSet()
            for (i in input.indices) {
                if (!allY.contains(i.toLong()))
                    add(i.toLong())
            }
        }

    val colsExp = buildSet {
        val allX = galaxies.map { it.first }.toSet()
        for (i in 0..<input[0].length) {
            if (!allX.contains(i.toLong()))
                add(i.toLong())
        }
    }
    return Image(galaxies, colsExp, rowsExp)
}

fun part1(input: List<String>) :Long {
   return part2(input,2)
}

fun part2(input: List<String>, expFactor: Int = 1_000_000): Long {
    val image = parse(input)
    val galaxiesExpanded = image.galaxies.map { g ->
        g.first + image.colsExp.count { g.first > it } * (expFactor - 1) to g.second + image.rowsExp.count { g.second > it } * (expFactor - 1)
    }

    var totalDistance: Long = 0
    galaxiesExpanded.forEachIndexed { galaxyA, it ->
        galaxiesExpanded.forEachIndexed { galaxyB, pair ->
            if (galaxyA > galaxyB) {
                totalDistance += abs(pair.first - it.first) +  abs(pair.second - it.second)
            }
        }
    }
    return totalDistance
}

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInputFromLauncher("/test")
    val dataInput = readInput("/data")

    check(part1(testInput).let { it.println(); it } == 374L)

    part2(testInput, 10).println() //1030
    part2(testInput, 100).println() //8410

    "part1:".println()
    part1(dataInput).println() //    10077850

    "part2:".println()
    part2(dataInput).println() // 504715068438
}