package tinyjs.lexer.token.recognizer

import tinyjs.lexer.token.Token

class IdentifierTokenRecognizer : TokenRecognizer {
    override fun recognize(stringForRecognition: String, startPosition: Int): Token? {
        return if (isFirstSymbolCorrect(stringForRecognition[startPosition])) {
            var endPosition = startPosition + 1
            while ((endPosition < stringForRecognition.length) &&
                isNonFirstSymbolCorrect(stringForRecognition[endPosition])
            ) {
                endPosition++
            }
            Token("IDENTIFIER_TOKEN", startPosition, endPosition)
        } else {
            null
        }
    }


    private fun isFirstSymbolCorrect(symbol: Char): Boolean {
        return symbol.isLetter() || (symbol == '$') || (symbol == '_')
    }

    private fun isNonFirstSymbolCorrect(symbol: Char): Boolean {
        return isFirstSymbolCorrect(symbol) || symbol.isDigit()
    }
}