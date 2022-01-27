package regex

import statemachine.StateMachine
import statemachine.Transition
import java.util.*

private fun <T> Iterable<T>.replace(predicate: (T) -> T): List<T> = map { predicate(it) }

class RegexToStateMachineConvertor {
    fun convert(regex: Regex): StateMachine {
        val (allStates, allTransitions) = createStatesAndTransitions(regex)
        val (renamedStates, renamedTransitions) = createReadableNamesForStates(allStates, allTransitions)
        return createStateMachine(renamedStates, renamedTransitions)
    }

    private fun createStatesAndTransitions(regex: Regex): Pair<List<String>, Set<Transition>> {
        val allStates = mutableListOf<String>()
        val allTransitions = mutableSetOf<Transition>()

        var currPosition = 0
        while (currPosition < regex.items.size) {
            if (isIterationOperation(regex, currPosition)) {
                val operand = regex.items[currPosition]
                processIteration(regex, currPosition, allStates, allTransitions)
            } else if (isUnionOperation(regex, currPosition)) {
                val operands = getUnionOperands(regex, currPosition)
                processUnion(regex, currPosition, allStates, allTransitions)
            } else {
                processConcatenation(regex, currPosition, allStates, allTransitions)
            }

            currPosition++
        }
        return Pair(allStates, allTransitions)
    }

    private fun processIteration(
        regex: Regex,
        position: Int,
        allStates: MutableList<String>,
        allTransitions: MutableSet<Transition>,
    ) {
        val currItem = regex.items[position]
        val currState = allStates[position]
        if (currItem.first != null) {
            allTransitions.add(Transition(currState, currState, currItem.first!!.toString()))
        } else if (currItem.second != null) {
            val (allStatesFromSubRegex, allTransitionsFromSubRegex) = createStatesAndTransitions(regex)

        }
    }

    private fun processUnion(
        regex: Regex,
        position: Int,
        allStates: MutableList<String>,
        allTransitions: MutableSet<Transition>,
    ) {

    }

    private fun processConcatenation(
        regex: Regex,
        position: Int,
        allStates: MutableList<String>,
        allTransitions: MutableSet<Transition>,
    ) {
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
        }
    }

    private fun replaceState(
        state: String,
        newState: String,
        allStates: MutableList<String>,
        allTransitions: MutableSet<Transition>,
    ) {
        allStates[allStates.indexOf(state)] = newState
        allTransitions.replace { previousTransition ->
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

            Transition(from, to, previousTransition.symbol)
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

    private fun createReadableNamesForStates(
        allStates: List<String>,
        allTransitions: Set<Transition>,
    ): Pair<List<String>, Set<Transition>> {
        val renamedStateForInitialStateMap = allStates.mapIndexed { index, initial -> Pair("q$index", initial) }.toMap()
        val renamedStateSet = renamedStateForInitialStateMap.values.toList()
        val renamedTransitionSet = allTransitions.map { initialTransition ->
            Transition(
                renamedStateForInitialStateMap[initialTransition.from]!!,
                renamedStateForInitialStateMap[initialTransition.to]!!,
                initialTransition.symbol
            )
        }.toSet()
        return Pair(renamedStateSet, renamedTransitionSet)
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
