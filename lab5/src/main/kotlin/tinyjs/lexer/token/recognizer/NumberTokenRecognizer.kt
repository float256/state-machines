package tinyjs.lexer.token.recognizer

import tinyjs.lexer.token.Token

class NumberTokenRecognizer: TokenRecognizer {
    override fun recognize(stringForRecognition: String, startPosition: Int): Token? {
        return if (stringForRecognition[startPosition].isDigit()) {
            var endPosition = startPosition
            while ((endPosition < stringForRecognition.length) && (stringForRecognition[endPosition].isDigit())) {
                endPosition++
            }
            Token("NUMBER_TOKEN", startPosition, endPosition)
        } else {
            null
        }
    }
}