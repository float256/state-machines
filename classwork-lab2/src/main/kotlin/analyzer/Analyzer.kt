package analyzer

import java.util.*

class Analyzer {
    fun isValid(sentence: String, analyzeSteps: Map<Int, AnalyzeStep>) = try {
        val symbols: Queue<String> = LinkedList(
            sentence.split("").filter(String::isNotEmpty)
        )
        throwIsInvalid(symbols, analyzeSteps)
        true
    } catch (e: Exception) {
        false
    }

    private fun throwIsInvalid(symbols: Queue<String>, analyzeSteps: Map<Int, AnalyzeStep>) {
        val stack: Stack<Int> = Stack()

        var isFinish = false
        var currStepId = 1
        while (!isFinish) {
            var currSymbol = ""
            if (symbols.isNotEmpty()) {
                currSymbol = symbols.peek()
            }
            val currStep = analyzeSteps[currStepId]!!

            if (currStep.directSymbols.isEmpty() || currStep.directSymbols.contains(currSymbol)) {
                if (currStep.isAddToStack) {
                    stack.add(currStepId + 1)
                }
                if (currStep.isShift) {
                    symbols.poll()
                }

                isFinish = currStep.isFinish
                currStepId = currStep.pointerToNextStep ?: stack.pop()
            } else if (currStep.isThrowError) {
                throw IllegalArgumentException()
            } else {
                currStepId++
            }
        }

        if (stack.isNotEmpty() || symbols.isNotEmpty()) {
            throw IllegalArgumentException()
        }
    }
}
