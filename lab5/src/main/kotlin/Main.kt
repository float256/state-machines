import tinyjs.lexer.Lexer
import tinyjs.lexer.LexerException
import tinyjs.lexer.token.recognizer.IdentifierTokenRecognizer
import tinyjs.lexer.token.recognizer.NumberTokenRecognizer
import tinyjs.lexer.token.recognizer.StringTokenRecognizer
import tinyjs.lexer.token.recognizer.keyword.*

fun main() {
    val expression = generateSequence(::readLine).joinToString("\n")
    val allTokenRecognizers = listOf(
        FunctionKeywordRecognizer(),
        IfKeywordRecognizer(),
        ElseKeywordRecognizer(),
        ImportKeywordRecognizer(),
        ExportKeywordRecognizer(),
        FromKeywordRecognizer(),
        OpenCurlyBraceRecognizer(),
        ClosedCurlyBraceRecognizer(),
        OpenParenthesisBraceRecognizer(),
        ClosedParenthesisBraceRecognizer(),
        PlusSighRecognizer(),
        MinusSighRecognizer(),
        AsteriskSighRecognizer(),
        SlashSighRecognizer(),
        EqualsSighRecognizer(),
        SemicolonSighRecognizer(),
        PointSighRecognizer(),
        EOLRecognizer(),
        IdentifierTokenRecognizer(),
        StringTokenRecognizer(),
        NumberTokenRecognizer(),
    )

    try {
        val allTokens = Lexer().splitByTokens(allTokenRecognizers, expression)
        println(allTokens.joinToString("\n"))
    } catch (e: LexerException) {
        println("Incorrect expression")
    } catch (e: Exception) {
        println("Unexpected exception: $e")
    }
}