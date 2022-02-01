package statemachine

import java.util.*

class StateMachineEpsilonRemover {
    fun removeEpsilonTransitions(stateMachine: StateMachine): StateMachine {
        val epsilonClosureForEachState = stateMachine.stateSymbols.map { stateSymbol ->
            Pair(stateSymbol, getEpsilonClosure(stateMachine, stateSymbol))
        }.toMap()
        val allTransitions = stateMachine.stateSymbols.map { stateSymbol ->
            stateMachine.transitionSymbols.map { transitionSymbol ->
                getTransitions(stateSymbol, transitionSymbol, epsilonClosureForEachState[stateSymbol]!!)
            }.flatten()
        }.flatten().toSet()
        val allStates = allTransitions.map { listOf(it.from, it.to) }.flatten().toSet()

        val stateMachineWithoutEpsilonTransitions = StateMachine(
            allStates, stateMachine.startSymbol, stateMachine.transitionSymbols
        )
        allTransitions.forEach { transition ->
            stateMachineWithoutEpsilonTransitions.setTransition(transition.from, transition.to, transition.symbol)
        }
        return stateMachineWithoutEpsilonTransitions
    }

    private fun getEpsilonClosure(stateMachine: StateMachine, stateSymbol: String): Set<State> {
        val result = mutableSetOf<State>()

        val statesStack = ArrayDeque(mutableListOf(stateMachine[stateSymbol]!!))
        while (statesStack.isNotEmpty()) {
            val currState = statesStack.pop()
            result.add(currState)

            currState.getTransitions().forEach { transition ->
                if (transition.symbol == StateMachineConstants.EmptyTransitionSymbol) {
                    statesStack.add(stateMachine[transition.to]!!)
                }
            }
        }
        return result
    }

    private fun getTransitions(
        stateSymbol: String,
        transitionSymbol: String,
        epsilonClosureForState: Set<State>
    ): Set<Transition> {
        return epsilonClosureForState.map { state ->
            state.getTransitions().filter { it.symbol == transitionSymbol }
                .map { transitionForStateFromEpsilonClosure ->
                    Transition(stateSymbol, transitionForStateFromEpsilonClosure.to, transitionSymbol)
                }
        }.flatten().toSet()
    }
}