package regex

import statemachine.StateMachine
import statemachine.StateMachineConstants
import statemachine.Transition
import java.util.*

class RegexToStateMachineConvertor {
    fun convert(regex: Regex): StateMachine {
        val (allStates, allTransitions) = createStatesAndTransitions(regex)
        return createStateMachine(allStates, allTransitions)
    }

    private fun createStatesAndTransitions(regex: Regex): Pair<List<String>, Set<Transition>> {
        val allStates = mutableListOf(UUID.randomUUID().toString())
        val allTransitions = mutableSetOf<Transition>()

        var currPosition = 0
        while (currPosition < regex.items.size) {
            currPosition = if (isIterationOperation(regex, currPosition)) {
                processIteration(regex, currPosition, allStates, allTransitions)
            } else if (isUnionOperation(regex, currPosition)) {
                processUnion(regex, currPosition, allStates, allTransitions)
            } else {
                processConcatenation(regex, currPosition, allStates, allTransitions)
            }
        }
        return Pair(allStates, allTransitions)
    }

    private fun processIteration(
        regex: Regex,
        position: Int,
        allStates: MutableList<String>,
        allTransitions: MutableSet<Transition>,
    ): Int {
        val currItem = regex.items[position]
        if (currItem.first != null) {
            val firstState = UUID.randomUUID().toString()
            val secondState = UUID.randomUUID().toString()
            val newFinalState = UUID.randomUUID().toString()
            val prevFinalState = allStates.last()

            allStates.add(firstState)
            allStates.add(secondState)
            allStates.add(newFinalState)

            allTransitions.add(Transition(firstState, secondState, currItem.first!!.toString()))
            allTransitions.add(Transition(secondState, firstState, StateMachineConstants.EmptyTransitionSymbol))
            allTransitions.add(Transition(prevFinalState, firstState, StateMachineConstants.EmptyTransitionSymbol))
            allTransitions.add(Transition(secondState, newFinalState, StateMachineConstants.EmptyTransitionSymbol))
            allTransitions.add(Transition(prevFinalState, newFinalState, StateMachineConstants.EmptyTransitionSymbol))
        } else if (currItem.second != null) {
            val (allStatesFromSubRegex, allTransitionsFromSubRegex) = createStatesAndTransitions(currItem.second!!)
            val newFinalState = UUID.randomUUID().toString()
            val prevFinalState = allStates.last()
            val subRegexStartState = allStatesFromSubRegex.first()
            val subRegexEndState = allStatesFromSubRegex.last()

            allStates.addAll(allStatesFromSubRegex)
            allStates.add(newFinalState)

            allTransitions.addAll(allTransitionsFromSubRegex)
            allTransitions.add(Transition(prevFinalState, newFinalState, StateMachineConstants.EmptyTransitionSymbol))
            allTransitions.add(Transition(prevFinalState, subRegexStartState, StateMachineConstants.EmptyTransitionSymbol))
            allTransitions.add(Transition(subRegexEndState, subRegexStartState, StateMachineConstants.EmptyTransitionSymbol))
            allTransitions.add(Transition(subRegexEndState, newFinalState, StateMachineConstants.EmptyTransitionSymbol))
        }
        return position + 2
    }

    private fun processUnion(
        regex: Regex,
        position: Int,
        allStates: MutableList<String>,
        allTransitions: MutableSet<Transition>,
    ): Int {
        val operands = getUnionOperands(regex, position)
        val newFinalState = UUID.randomUUID().toString()
        val prevFinalState = allStates.last()

        operands.forEach { (symbol, subRegex) ->
            if (symbol != null) {
                val firstState = UUID.randomUUID().toString()
                val secondState = UUID.randomUUID().toString()

                allStates.add(firstState)
                allStates.add(secondState)

                allTransitions.add(Transition(firstState, secondState, symbol.toString()))
                allTransitions.add(Transition(prevFinalState, firstState, StateMachineConstants.EmptyTransitionSymbol))
                allTransitions.add(Transition(secondState, newFinalState, StateMachineConstants.EmptyTransitionSymbol))
            } else if (subRegex != null) {
                val (allStatesFromSubRegex, allTransitionsFromSubRegex) = createStatesAndTransitions(subRegex)
                val subRegexStartState = allStatesFromSubRegex.first()
                val subRegexFinalState = allStatesFromSubRegex.last()

                allStates.addAll(allStatesFromSubRegex)
                allTransitions.addAll(allTransitionsFromSubRegex)

                allTransitions.add(Transition(prevFinalState, subRegexStartState, StateMachineConstants.EmptyTransitionSymbol))
                allTransitions.add(Transition(subRegexFinalState, newFinalState, StateMachineConstants.EmptyTransitionSymbol))
            }
        }
        allStates.add(newFinalState)

        return position + operands.size * 2 - 1
    }

    private fun processConcatenation(
        regex: Regex,
        position: Int,
        allStates: MutableList<String>,
        allTransitions: MutableSet<Transition>,
    ): Int {
        if (regex.items[position].first != null) {
            val transitionSymbol = regex.items[position].first!!.toString()
            val newState = UUID.randomUUID().toString()
            val stateBeforeNewState = allStates.last()
            allStates.add(newState)
            allTransitions.add(Transition(stateBeforeNewState, newState, transitionSymbol))
        } else if (regex.items[position].second != null) {
            val subRegex = regex.items[position].second!!
            val statesFromSubRegex: MutableList<String> = mutableListOf()
            val transitionsFromSubRegex: MutableSet<Transition> = mutableSetOf()
            val statesAndTransitionsFromSubRegex = createStatesAndTransitions(subRegex)
            statesFromSubRegex.addAll(statesAndTransitionsFromSubRegex.first)
            transitionsFromSubRegex.addAll(statesAndTransitionsFromSubRegex.second)
            replaceState(statesFromSubRegex.first(), allStates.last(), statesFromSubRegex, transitionsFromSubRegex)
            statesFromSubRegex.removeAt(0)

            allStates.addAll(statesFromSubRegex)
            allTransitions.addAll(transitionsFromSubRegex)
        }
        return position + 1
    }

    private fun replaceState(
        state: String,
        newState: String,
        allStates: MutableList<String>,
        allTransitions: MutableSet<Transition>,
    ) {
        allStates[allStates.indexOf(state)] = newState
        allTransitions.toList().forEach { previousTransition ->
            val from = if (previousTransition.from == state) {
                newState
            } else {
                previousTransition.from
            }
            val to = if (previousTransition.to == state) {
                newState
            } else {
                previousTransition.to
            }

            allTransitions.remove(previousTransition)
            allTransitions.add(Transition(from, to, previousTransition.symbol))
        }
    }

    private fun isIterationOperation(regex: Regex, position: Int): Boolean {
        return (position + 1 < regex.items.size) &&
                (regex.items[position + 1].first == RegexConstants.IterationOperationSymbol)
    }

    private fun isUnionOperation(regex: Regex, position: Int): Boolean {
        return (position + 1 < regex.items.size) &&
                (regex.items[position + 1].first == RegexConstants.UnionOperationSymbol)
    }

    private fun getUnionOperands(regex: Regex, startPosition: Int): List<Pair<Char?, Regex?>> {
        val operands = mutableListOf<Pair<Char?, Regex?>>()
        var currPosition = startPosition

        while (currPosition < regex.items.size) {
            operands.add(regex.items[currPosition])
            currPosition++
            if ((currPosition >= regex.items.size) ||
                (regex.items[currPosition].first != RegexConstants.UnionOperationSymbol)
            ) {
                break
            }
            currPosition++
        }
        return operands
    }

    private fun createStateMachine(
        allStates: List<String>,
        allTransitions: Set<Transition>,
    ): StateMachine {
        val allTransitionSymbols = allTransitions
            .map { it.symbol }
            .distinct()
            .toSet()
        val stateMachine = StateMachine(allStates.toSet(), allStates.first(), allTransitionSymbols)
        allTransitions.map { stateMachine.setTransition(it.from, it.to, it.symbol) }
        return stateMachine
    }
}
