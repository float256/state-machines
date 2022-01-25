package tinyjs.lexer.token.recognizer

import tinyjs.lexer.token.Token

class StringTokenRecognizer: TokenRecognizer {
    override fun recognize(stringForRecognition: String, startPosition: Int): Token? {
        return if (stringForRecognition[startPosition] == '\"') {
            var endPosition = startPosition + 1
            var isEnd = (endPosition >= stringForRecognition.length)

            while (!isEnd && isCorrectSymbol(stringForRecognition[endPosition])) {
                endPosition++
                isEnd = (endPosition >= stringForRecognition.length)
            }

            if (!isEnd && (stringForRecognition[endPosition] == '\"')) {
                Token("STRING", startPosition, endPosition + 1)
            } else {
                null
            }
        } else {
            null
        }
    }

    private fun isCorrectSymbol(symbol: Char): Boolean {
        return (symbol != '\"') && (symbol != '\n')
    }
}