
fun main() {

    val groundTruth = Color(12,13,14)
//    val maxRed = 12
//    val maxGreen = 13
//    val maxBlue = 14

    fun part1(games: List<String>): Int {

        // sample game line to parse :
        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green

        return games.sumOf  game@{   gameLine ->
            "game iteration".println()
        val (game, rounds) = gameLine.split(":")
            rounds.split(";").forEach { round ->
                val color = Color()
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
                   if(color.anyHigher(groundTruth)) {
                       return@game 0
                   }
            }
            val (_, gameNumber ) = game.split(" ")
            return@game gameNumber.toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    part1(testInput).println()
    check(part1(testInput) == 8)

//    val test2 = readInput("Day02_test2")
//    check(part2(test2) == 0)

    "part1:".println()
    part1(readInput("Day02")).println()

    "part2:".println()
    part2(readInput("Day02")).println()
    check(part2(readInput("Day02")) == 0)

}

data class Color(var red  : Int = 0, var green: Int = 0, var blue: Int = 0) {

    fun anyHigher  (other: Color) : Boolean {
        return red > other.red || green > other.green || blue > other.blue
    }
}
