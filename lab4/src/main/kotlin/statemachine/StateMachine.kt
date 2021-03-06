package statemachine

class StateMachine(
    stateSymbols: Set<String>,
    startSymbol: String,
    transitionSymbols: Set<String>,
) {
    var stateSymbols = stateSymbols
        private set
    var startSymbol = startSymbol
        private set
    var transitionSymbols = transitionSymbols
        private set

    init {
        if (startSymbol !in stateSymbols) {
            throw IllegalArgumentException("Incorrect start symbol")
        }
    }

    private val allStates: MutableMap<String, State> = stateSymbols.associateWith { State(it) }.toMutableMap()

    fun setTransition(startStateSymbol: String, endStateSymbol: String, transitionSymbol: String) {
        if ((startStateSymbol in stateSymbols) &&
            (endStateSymbol in stateSymbols) &&
            (transitionSymbol in transitionSymbols)
        ) {
            allStates[startStateSymbol]!!.addTransition(allStates[endStateSymbol]!!, transitionSymbol)
        } else {
            throw IllegalArgumentException("Incorrect symbols")
        }
    }

    operator fun get(stateName: String): State? {
        return allStates[stateName]
    }

    override fun toString(): String {
        return "Q: ${stateSymbols.joinToString(", ")}\n" +
                "T: ${transitionSymbols.joinToString(", ")}\n" +
                allStates.map { (_, state) -> state.toString() }.joinToString("\n")
    }
}

fun recreateStateMachineWithDefaultNames(stateMachine: StateMachine): StateMachine {
    val allTransitions = stateMachine.stateSymbols.map {
        stateMachine[it]!!.getTransitions()
    }.flatten().toSet()

    val renamedStateForInitialStateMap = stateMachine.stateSymbols
        .mapIndexed { index, initial -> Pair(initial, "q$index") }.toMap()
    val renamedStateMachine = StateMachine(
        renamedStateForInitialStateMap.values.toSet(),
        stateMachine.startSymbol,
        stateMachine.transitionSymbols
    )

    allTransitions.forEach { initialTransition ->
        renamedStateMachine.setTransition(
            renamedStateForInitialStateMap[initialTransition.from]!!,
            renamedStateForInitialStateMap[initialTransition.to]!!,
            initialTransition.symbol
        )
    }
    return renamedStateMachine
}