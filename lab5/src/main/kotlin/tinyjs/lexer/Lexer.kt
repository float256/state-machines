package tinyjs.lexer

class Lexer {
    fun splitByTokens(expression: String): List<Token> {
        val allTokens = mutableListOf<Token>()
        var currSearchPosition = 0

        while (currSearchPosition < expression.length) {
            var range: IntRange? = null
            TokenType.values().forEach { tokenType ->
                val regexMatch = Regex(tokenType.regex).find(expression, currSearchPosition)
                if (isTokenFound(regexMatch, currSearchPosition)) {
                    range = regexMatch!!.range
                    currSearchPosition = range!!.last + 1
                    allTokens.add(Token(
                        tokenType,
                        range!!.first,
                        range!!.last
                    ))
                }
            }

            if (range == null) {
                println(expression.substring(currSearchPosition))
                throw LexerException("Incorrect expression")
            } else {
                currSearchPosition = skipSpaces(currSearchPosition, expression)
            }
        }
        return allTokens
    }

    private fun isTokenFound(regexMatch: MatchResult?, searchStartPosition: Int): Boolean {
        return (regexMatch != null) && (regexMatch.range.first == searchStartPosition)
    }

    private fun skipSpaces(searchPosition: Int, expression: String): Int {
        val nonSpaceSymbolRegex = "[\\S|\n]".toRegex()
        return nonSpaceSymbolRegex.find(expression, searchPosition)?.range?.first
            ?: searchPosition
    }
}