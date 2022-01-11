package statemachine

import java.util.*

class DFAAlgorithm {
    fun determinate(initialStateMachine: StateMachine): StateMachine {
        val stateQueue: Queue<Set<String>> = ArrayDeque()
        val groupedStates: MutableSet<Set<String>> = mutableSetOf()
        val transitions: MutableSet<Transition> = mutableSetOf()
        stateQueue.add(setOf(initialStateMachine[initialStateMachine.startSymbol]!!.stateSymbol))

        while (stateQueue.isNotEmpty()) {
            val currStates = stateQueue.poll()
            val statesGroupedByTransitionSymbols = currStates.map { stateSymbol ->
                initialStateMachine[stateSymbol]!!.getTransitions()
            }
                .flatten()
                .map { transition -> Pair(transition.symbol, transition.to) }
                .groupBy(
                    keySelector = { (transitionSymbol, _) -> transitionSymbol },
                    valueTransform = { (_, stateSymbol) -> stateSymbol }
                )
                .mapValues { it.value.toSet() }
            statesGroupedByTransitionSymbols.forEach { (transitionSymbol, newStates) ->
                transitions.add(Transition(generateName(currStates), generateName(newStates), transitionSymbol))
            }
            statesGroupedByTransitionSymbols.filter { (_, states) -> !groupedStates.contains(states) }
                .forEach { (_, newStates) ->
                    groupedStates.add(newStates)
                    stateQueue.add(newStates)
                }
        }

        return buildMachine(groupedStates, initialStateMachine.transitionSymbols,
            initialStateMachine.startSymbol, transitions)
    }

    private fun buildMachine(
        groupedStates: Set<Set<String>>,
        transitionSymbols: Set<String>,
        startSymbol: String,
        transitions: Set<Transition>,
    ): StateMachine {
        val dfaMachine = StateMachine(groupedStates.map { generateName(it) }.toSet(), startSymbol, transitionSymbols)
        transitions.forEach { transition ->
            dfaMachine.setTransition(transition.from,
                transition.to,
                transition.symbol)
        }
        return dfaMachine
    }

    private fun generateName(states: Set<String>): String {
        return when (states.size) {
            1 -> states.first()
            else -> "(${states.toSortedSet().joinToString(", ")})"
        }
    }
}