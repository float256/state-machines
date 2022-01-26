package statemachine

import java.util.*

const val FIND_TRANSITION_REGEX = "(\\w+) -> (\\w+), (\\w+)"

class StateMachineFactory {
    fun create(scanner: Scanner): StateMachine {
        val stateMachine = createEmptyMachine(scanner)
        setTransitions(stateMachine, scanner)
        return stateMachine
    }

    private fun createEmptyMachine(scanner: Scanner): StateMachine {
        val allStateSymbols = scanner.nextLine().split(": ")[1]
            .split(", ").filter { it.isNotBlank() }
        val allTransitionSymbols = scanner.nextLine().split(": ")[1]
            .split(", ").filter { it.isNotBlank() }
        return StateMachine(allStateSymbols.toSet(), allStateSymbols.first(), allTransitionSymbols.toSet())
    }

    private fun setTransitions(stateMachine: StateMachine, scanner: Scanner) {
        while (scanner.hasNext()) {
            processOneLine(stateMachine, scanner.nextLine())
        }
    }

    private fun processOneLine(stateMachine: StateMachine, line: String) {
        val match = Regex(FIND_TRANSITION_REGEX).find(line)!!
        val (startStateSymbol, endStateSymbol, transitionSymbol) = match.destructured
        stateMachine.setTransition(startStateSymbol, endStateSymbol, transitionSymbol)
    }
}