import java.util.concurrent.atomic.AtomicInteger

fun LongRange.splitInHalf() : Pair<LongRange, LongRange>    {
    return Pair(this.first..this.first + this.count() / 2, this.first + this.count() / 2..this.last)    }

fun main() {

    fun part1(input: List<String>): Int {

        fun parse(s : String) = s.split(':')[1].trim().split(' ').toList().filterNot { it.isEmpty() }.map { it.trim().toInt() }

        val times = parse(input[0])
        val dists = parse(input[1])

        val inputTable = times.zip(dists).toMap()

        return inputTable.map() { (t, d) ->
            var res = 0
            for (press in 1..t - 1) {
                val dx = press * (t - press)
                if (dx > d) {
                    res += 1
                }
            }
            res
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {

        val counts = AtomicInteger(0)

        fun parse(s : String) = s.split(':')[1].trim().replace(" ", "").toLong()

        val t = parse(input[0])
        val d = parse(input[1])

        fun wins (press :Long): Boolean {
            counts.incrementAndGet()
            return press * (t - press) > d
        }

        fun binarySearch(current : LongRange, directionUp :Boolean): Long {

            if (current.count() == 2)
                return if(!directionUp)
                    current.last
                else current.first

            val (first, second ) = current.splitInHalf()

//            check(wins(current.first) ||  wins(current.last)) {"no solution found in range $current directionup $directionUp "}
//            check(!wins(current.first) ||  !wins(current.last)) {"all solution found in range $current directionup $directionUp "}

            return if(wins(first.last))
                if (directionUp) binarySearch(second, directionUp)
                else binarySearch(first, directionUp)
            else
                if(directionUp)
                    binarySearch(first, directionUp)
                else
                    binarySearch(second, directionUp)
    }

        val (firstInitHalf, secondInitHalf) = LongRange(0, t - 1).splitInHalf()

        val sec = binarySearch(secondInitHalf, true).toInt()
        val first = binarySearch(firstInitHalf, false).toInt()

        "races evaluation count :  $counts".println()

    return  sec-first +1
    }

    // test if implementation meets criteria from the description, like:
    //
    val testInput = readInput("Day06_test")
    part1(testInput).println()
    check(part1(testInput) == 288) //(4 * 8 * 9)
    part2(testInput).println()
    check(part2(testInput) == 71503)
//
    "part1:".println()
    part1(readInput("Day06")).println()
    check(part1(readInput("Day06")) == 345015)
//
    "part2:".println()
    part2(readInput("Day06")).println()
    check(part2(readInput("Day06")) == 42588603)
}
