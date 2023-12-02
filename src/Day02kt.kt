import kotlin.math.max

fun main() {

    fun part1(games: List<String>): Int {

        val maxPick = CubesPick(12, 13, 14)

        return games.sumOf game@{ gameLine ->
            val (game, rounds) = gameLine.split(":")
            rounds.split(";").forEach { round ->
                val picked = CubesPick()
                    .apply {
                        round.trim().split(",").forEach { c ->
                            val (value, name) = c.trim().split(" ")
                            when (name) {
                                "red" -> red = value.toInt()
                                "green" -> green = value.toInt()
                                "blue" -> blue = value.toInt()
                            }
                        }
                    }
                if (picked.anyHigher(maxPick)) {
                    return@game 0
                }
            }
            val (_, gameNumber) = game.split(" ")
            return@game gameNumber.toInt()
        }
    }

    fun part2(games: List<String>): Int {

        return games.sumOf game@{ gameLine ->

            var atLeastRed = 1
            var atLeastGreen = 1
            var atLeastBlue = 1

            val (_, rounds) = gameLine.split(":")
            rounds.split(";").map { round ->
                round.trim().split(",").forEach { c ->
                    val (value, name) = c.trim().split(" ")
                    when (name) {
                        "red" -> atLeastRed = max(atLeastRed, value.toInt())
                        "green" -> atLeastGreen = max(atLeastGreen, value.toInt())
                        "blue" -> atLeastBlue = max(atLeastBlue, value.toInt())
                    }
                }
            }
            atLeastRed * atLeastGreen * atLeastBlue
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    part1(testInput).println()
    check(part1(testInput) == 8)

    val test2 = readInput("Day02_test")
    check(part2(test2) == 2286)

    "part1:".println()
    part1(readInput("Day02")).println()

    "part2:".println()
    part2(readInput("Day02")).println()

}

data class CubesPick(var red: Int = 0, var green: Int = 0, var blue: Int = 0) {

    fun anyHigher(other: CubesPick): Boolean {
        return red > other.red || green > other.green || blue > other.blue
    }

}
