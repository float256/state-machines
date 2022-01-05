import tinyjs.lexer.Lexer
import tinyjs.lexer.LexerException

fun main() {
    val expression = generateSequence(::readLine).joinToString("\n")
    try {
        val allTokens = Lexer().splitByTokens(expression)
        println(allTokens.joinToString("\n"))
    } catch (e: LexerException) {
        println("Incorrect expression")
    } catch (e: Exception) {
        println("Unexpected exception: $e")
    }
}