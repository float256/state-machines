package tinyjs.lexer.token.recognizer

import tinyjs.lexer.token.Token

class StringTokenRecognizer: TokenRecognizer {
    override fun recognize(stringForRecognition: String, startPosition: Int): Token? {
        return if (stringForRecognition[startPosition] == '\"') {
            var endPosition = startPosition + 1
            var isStringTokenEndReached = false

            while (
                (endPosition < stringForRecognition.length) &&
                !isStringTokenEndReached &&
                isCorrectSymbol(stringForRecognition[endPosition])
            ) {
                endPosition++
                isStringTokenEndReached = stringForRecognition[endPosition] == '\"'
                if (stringForRecognition[endPosition] == '\\') {
                    endPosition++
                }
            }

            if (!isStringTokenEndReached && (stringForRecognition[endPosition] == '\"')) {
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