package tinyjs.lexer

import tinyjs.lexer.token.Token
import tinyjs.lexer.token.recognizer.TokenRecognizer

class Lexer {
    fun splitByTokens(tokenRecognizers: List<TokenRecognizer>, expression: String): List<Token> {
        val allTokens = mutableListOf<Token>()
        var currSearchPosition = skipSpaces(0, expression)

        while (currSearchPosition < expression.length) {
            var currToken: Token? = null
            for (currTokenRecognizer in tokenRecognizers) {
                currToken = currTokenRecognizer.recognize(expression, currSearchPosition)
                if (currToken != null) {
                    allTokens.add(currToken)
                    currSearchPosition = currToken.endPosition
                    break
                }
            }

            if (currToken == null) {
                throw LexerException("Incorrect expression")
            } else {
                currSearchPosition = skipSpaces(currSearchPosition, expression)
            }
        }
        return allTokens
    }

    private fun skipSpaces(searchPosition: Int, expression: String): Int {
        var newSearchPosition = searchPosition
        while (
            (newSearchPosition < expression.length) &&
            expression[newSearchPosition].isWhitespace()
        ) {
            newSearchPosition++
        }
        return newSearchPosition
    }
}