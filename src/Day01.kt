fun main() {

    fun part1(input: List<String>): Int {

      return input.sumOf {
          val first = it.first { it.isDigit() }
          val last = it.last { it.isDigit() }
          "$first$last".toInt()
      }
    }

    fun addSpelledOutNumbersAsDigits(line:String ) : String {

        val firstIndexWord = line.indexOfAny(NumberWord.words)
        val firstWord: NumberWord? = firstIndexWord
            .takeIf { it != -1 }
            ?.let { NumberWord.match3firstLetters(line.substring(it, it + 3)) }

        val firstIndexDigit = line.indexOfAny(NumberWord.digits)


       val lastIndexWord = line.lastIndexOfAny(NumberWord.words)
        val lastWord =lastIndexWord
            .takeIf { it != -1 }
            ?.let { NumberWord.match3firstLetters(line.substring(it, it + 3)) }
       val lastIndexDigit= line.lastIndexOfAny(NumberWord.digits)


        var transformedLine = line

        if((firstIndexWord < firstIndexDigit || firstIndexDigit == -1 ) && firstWord != null) {
            transformedLine =  "${firstWord.value}$transformedLine"
        }

        if((lastIndexWord > lastIndexDigit || lastIndexDigit == -1 ) && lastWord != null) {
            transformedLine += lastWord.value
        }
        return transformedLine
    }

    fun part2(input: List<String>): Int {
        return part1(input.map {  addSpelledOutNumbersAsDigits(it) })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val test2 = readInput("Day01_test2")
    check(part2(test2) == 281)

    "part1:".println()
    part1(readInput("Day01")).println()

    "part2:".println()
    part2(readInput("Day01")).println()
    check(part2(readInput("Day01")) == 54094)

}

enum class NumberWord(val word: String, val value: Int) {
    ONE("one", 1),
    TWO("two", 2),
    THREE("three", 3),
    FOUR("four", 4),
    FIVE("five", 5),
    SIX("six", 6),
    SEVEN("seven", 7),
    EIGHT("eight", 8),
    NINE("nine", 9);

    companion object {

        val digits = NumberWord.entries.map { it.value.toString() }
        val words = NumberWord.entries.map { it.word }
        private fun threeFirstLetters(word: String): String {
            return word.substring(0, 3)
        }

        fun match3firstLetters(word: String): NumberWord? {
            return entries.firstOrNull { threeFirstLetters(it.word) == threeFirstLetters(word) }
        }
    }
}