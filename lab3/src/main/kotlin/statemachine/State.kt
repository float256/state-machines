package statemachine

data class State(
    val stateSymbol: String,
) {
    private val allTransitions: MutableSet<Transition> = mutableSetOf()

    fun addTransition(endState: State, symbol: String) {
        allTransitions.add(Transition(stateSymbol, endState.stateSymbol, symbol))
    }

    fun getTransitions(): Set<Transition> {
        return allTransitions.toSet()
    }

    override fun toString(): String {
        return allTransitions.joinToString("\n") {
            "${it.from} -> ${it.to}, ${it.symbol}"
        }
    }
}
