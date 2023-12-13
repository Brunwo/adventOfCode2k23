package day10

import println
import readInput
import kotlin.math.abs
import kotlin.system.measureTimeMillis

const val red = "\u001b[31m"
const val blue = "\u001b[34m"
const val reset = "\u001b[0m"

//returns 1 for left turn, 0 for strait, -1 for right turn
fun Pair<Int, Int>.rotationLeftAmount(v2: Pair<Int, Int>): Int {
    return (second * v2.first - first * v2.second)
}

operator fun Pair<Int, Int>.plus(increment: Pair<Int, Int>): Pair<Int, Int> {
    return first + increment.first to second + increment.second
}

data class Tile(
    val pos: Pair<Int, Int>,
    val char: Char,
    var color: String = reset,
    var pathDirection: Pair<Int, Int>? = null
) {

    fun connects(other: Tile): Boolean {
        return connectsTo().contains(other.pos)
                && other.connectsTo().contains(this.pos)
    }

    fun print() {
        print(color + betterCharDisplay())
    }

    override operator fun equals(other: Any?): Boolean = pos == (other as Tile).pos
    override fun hashCode(): Int {
        return pos.hashCode()
    }

    fun isStartingPoint(): Boolean = char == 'S'

    private fun getConnects(): List<Direction> {
        return when (char) {
            'F' -> listOf(Direction.SOUTH, Direction.EAST)
            '7' -> listOf(Direction.SOUTH, Direction.WEST)
            'L' -> listOf(Direction.NORTH, Direction.EAST)
            'J' -> listOf(Direction.NORTH, Direction.WEST)
            '|' -> listOf(Direction.NORTH, Direction.SOUTH)
            '-' -> listOf(Direction.EAST, Direction.WEST)
            'S' -> Direction.entries
            else -> emptyList()
        }
    }

    private fun connectsTo(): List<Pair<Int, Int>> {
        return getConnects().map { this.pos + it.vector }
    }

    enum class Direction(val vector: Pair<Int, Int>) {
        NORTH(0 to -1),
        SOUTH(0 to 1),
        EAST(1 to 0),
        WEST(-1 to 0);
    }

    private fun betterCharDisplay(): Char {
        return when (char) {
            'F' -> '╔'
            '7' -> '╗'
            'L' -> '╚'
            'J' -> '╝'
            '|' -> '║'
            '-' -> '═'
            'S' -> '⬤'
            else -> char
        }
    }
}

fun List<Tile>.getSurroundingTiles(tile: Tile): List<Tile> {
    return this.filter {
        abs(it.pos.first - tile.pos.first) + abs(it.pos.second - tile.pos.second) == 1
    }
}

fun List<Tile>.printMaze(topLayer: Collection<Tile>, color: String) {

    topLayer.forEach { it.color = color }

    val printMaze = this.toMutableList()
    printMaze.removeAll(topLayer)
    printMaze.addAll(topLayer)

    printMaze.sortedBy { it.pos.first + it.pos.second * 200 }.zipWithNext().forEach { (a, b) ->
        if (a.pos.second != b.pos.second) {
            println("")
        }
        b.print()
    }
    reset.println()
}

fun parse(input: List<String>): List<Tile> {
    val tiles = mutableListOf<Tile>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            tiles.add(Tile(Pair(x, y), char))
        }
    }
    return tiles
}

fun part1(input: List<String>): Int {
    val tiles = parse(input)
    val (walked, _) = path(tiles)
    return walked.size / 2
}


fun part2(input: List<String>): Int {
    val tiles = parse(input)
    val (walked, leftTurns) = path(tiles)
    return tiles.getNests(leftTurns, walked).size
}

private fun path(tiles: List<Tile>): Pair<MutableList<Tile>, Int> {
    val start = tiles.first { it.isStartingPoint() }
    var currentTile = start
    val walked = mutableListOf<Tile>()

    var leftRightTurnsDiff = 0
    while (currentTile != start || walked.size == 0) {
        walked.add(currentTile)
        currentTile = tiles.getSurroundingTiles(currentTile).first {
            it.connects(currentTile)
                    && it != walked.getOrElse(walked.size - 2) { start }
        }

        val direction = Pair(
            currentTile.pos.first - walked.last().pos.first, currentTile.pos.second - walked.last().pos.second
        )

        walked.last().pathDirection?.let { leftRightTurnsDiff += it.rotationLeftAmount(direction) }
        currentTile.pathDirection = direction
    }
    tiles.printMaze(walked, red)
    walked.first().pathDirection = null
    return walked to leftRightTurnsDiff
}

fun List<Tile>.getNests(checkLeft: Int, path: List<Tile>): Collection<Tile> {

    // we check tiles on the correct "inside" side (left or right) regarding both the incoming & next direction
    val gridDirections = Tile.Direction.entries.map { it.vector }.toMutableList()
    val nests = mutableSetOf<Tile>()
    path.zipWithNext { tile, nextTile ->
        val offsets = gridDirections.filter {
            if (tile.pathDirection == null)
                nextTile.pathDirection!!.rotationLeftAmount(it) * checkLeft > 0
            else
                tile.pathDirection!!.rotationLeftAmount(it) * checkLeft > 0
                        || nextTile.pathDirection!!.rotationLeftAmount(it) * checkLeft > 0
        }

        val absolutePosCandidates = offsets.map { tile.pos + it }
        val adjacentNests = absolutePosCandidates.filter {
            !path.map { it.pos }.contains(it)
                    && this.map { it.pos }.contains(it)
        }

        nests.addAll(adjacentNests.map { Tile(it, '0', blue) })
    }

    measureTimeMillis {
        // extend nests until no more tiles can be found
        do {
            var active = false
            val extensionCandidates = this.toMutableList()
            extensionCandidates.removeAll(nests)
            extensionCandidates.removeAll(path)

            nests.forEach {
                Tile.Direction.entries.map { it.vector }.map { vector -> it.pos + vector }
                    .filter { !nests.map { it.pos }.contains(it) && !path.map { it.pos }.contains(it) } // 20% faster
                    .forEach { testPos ->
                        extensionCandidates.filter { it.pos == testPos }
                            .forEach {
                                it.color = blue
                                active = true
                            }
                    }
            }
            nests.addAll(extensionCandidates.filter { it.color == blue })
        } while (active)
    }.println()

    printMaze(nests, blue)
    return nests
}

fun main() {

    val packageName = System.getProperties().filter { it.key.toString().contains("command") }
        .map { it.value.toString().split('.')[0] }.first()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("$packageName/test")
    val test = part1(testInput).also { it.println() }
    val test2 = part2(readInput("$packageName/test2")).also { it.println() }

    check(test == 4)
    check(test2 == 8)

    "part1:".println()
    part1(readInput("$packageName/data")).println()
    "part2:".println()
    part2(readInput("$packageName/data")).println()
//    355
}