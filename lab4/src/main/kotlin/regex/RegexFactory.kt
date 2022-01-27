package regex

class RegexFactory {
    fun create(expression: String): Regex {
        if (validate(expression)) {
            return createRegexHelper(expression)
        } else {
            throw RegexException("Incorrect expression")
        }
    }

    private fun createRegexHelper(expression: String): Regex {
        val allRegexItems = mutableListOf<Pair<Char?, Regex?>>()
        var currPosition = 0
        while (currPosition < expression.length) {
            if (expression[currPosition] == '(') {
                var subExpression = ""
                var nestingLevel = 1

                currPosition++
                while (nestingLevel != 0) {
                    nestingLevel = when (expression[currPosition]) {
                        '(' -> nestingLevel + 1
                        ')' -> nestingLevel - 1
                        else -> nestingLevel
                    }
                    if (nestingLevel != 0) {
                        subExpression += expression[currPosition]
                    }
                    currPosition++
                }
                currPosition--

                allRegexItems.add(Pair(null, createRegexHelper(subExpression)))
            } else {
                if (expression[currPosition] == RegexConstants.IterationOperationSymbol) {
                    val currItem = Pair(null, Regex(listOf(
                        allRegexItems.last(),
                        Pair(RegexConstants.IterationOperationSymbol, null)
                    )))
                    allRegexItems.removeLast()
                    allRegexItems.add(currItem)
                } else {
                    allRegexItems.add(Pair(expression[currPosition], null))
                }
            }
            currPosition++
        }
        return Regex(allRegexItems)
    }

    private fun validate(expression: String): Boolean {
        return areAllBracesCorrect(expression) && areAllOperationSymbolsCorrect(expression)
    }

    private fun areAllBracesCorrect(expression: String): Boolean {
        var numberOfOpenBraces = 0
        var numberOfClosedBraces = 0
        var level = 0
        for (symbol in expression) {
            if (symbol == '(') {
                numberOfOpenBraces++
                level++
            } else if (symbol == ')') {
                numberOfClosedBraces++
                level--
            }

            if (level < 0) {
                return false
            }
        }
        return (numberOfOpenBraces == numberOfClosedBraces) && (level == 0)
    }

    private fun areAllOperationSymbolsCorrect(expression: String): Boolean {
        for ((index, symbol) in expression.withIndex()) {
            if (symbol == RegexConstants.UnionOperationSymbol) {
                if (!isUnionOperationCorrect(expression, index)) {
                    return false
                }
            } else if (symbol == RegexConstants.IterationOperationSymbol) {
                if (!isIterationOperationCorrect(expression, index)) {
                    return false
                }
            }
        }
        return true
    }

    private fun isUnionOperationCorrect(expression: String, position: Int): Boolean {
        return (position + 1 < expression.length) && (position - 1 >= 0)
                && isNotTerminal(expression[position + 1])
                && (expression[position - 1] != RegexConstants.UnionOperationSymbol)
    }

    private fun isIterationOperationCorrect(expression: String, position: Int): Boolean {
        return (position - 1 >= 0) && isNotTerminal(expression[position] - 1)
    }

    private fun isNotTerminal(symbol: Char): Boolean {
        return (symbol != RegexConstants.IterationOperationSymbol) &&
                (symbol != RegexConstants.IterationOperationSymbol)
    }
}