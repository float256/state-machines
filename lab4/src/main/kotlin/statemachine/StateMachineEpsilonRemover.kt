package statemachine

import java.util.*

class StateMachineEpsilonRemover {
    fun removeEpsilonTransitions(stateMachine: StateMachine): StateMachine {
        val epsilonClosureForEachState = stateMachine.stateSymbols.map { stateSymbol ->
            Pair(stateSymbol, getEpsilonClosure(stateMachine, stateSymbol))
        }.toMap()
        TODO("Not implemented")
    }

    private fun getEpsilonClosure(stateMachine: StateMachine, stateSymbol: String): Set<State> {
        val result = mutableSetOf<State>()

        val statesStack = ArrayDeque(mutableListOf(stateMachine[stateSymbol]!!))
        while (statesStack.isNotEmpty()) {
            val currState = statesStack.pop()
            result.add(currState)

            currState.getTransitions().forEach{ transition ->
                if (transition.symbol == StateMachineConstants.EmptyTransitionSymbol) {
                    statesStack.add(stateMachine[transition.to]!!)
                }
            }
        }
        return result
    }

    private fun getTransitions(stateMachine: StateMachine,
                               stateSymbol: String,
                               transitionSymbol: String,
                               epsilonClosure: Set<State>): Set<Transition> {
        TODO("Not implemented")
    }
}