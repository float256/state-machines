package tinyjs.lexer.token.recognizer.keyword

import tinyjs.lexer.token.Token
import tinyjs.lexer.token.recognizer.TokenRecognizer

abstract class KeywordRecognizer : TokenRecognizer {
    abstract val keyword: String
    abstract val tokenName: String

    override fun recognize(stringForRecognition: String, startPosition: Int): Token? {
        val endPosition = startPosition + keyword.length
        return if ((endPosition <= stringForRecognition.length) &&
            (stringForRecognition.substring(startPosition, endPosition) == keyword)
        ) {
            Token(tokenName, startPosition, endPosition)
        } else {
            null
        }
    }
}