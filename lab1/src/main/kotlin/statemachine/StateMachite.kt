package statemachine

abstract class StateMachine<StateType> {
    abstract val stateSymbols: Set<String>
    abstract val inputSymbols: Set<String>
    abstract val outputSymbols: Set<String>

    private val allTransitions: MutableMap<StateType, MutableMap<String, Transition>> = mutableMapOf()

    fun get(state: StateType, inputSymbol: String): Transition? {
        return allTransitions[state]?.get(inputSymbol)
    }

    fun set(state: StateType, inputSymbol: String, transition: Transition) {
        validateState(state)
        when (transition.outputSymbol) {
            in inputSymbols -> allTransitions[state]?.set(inputSymbol, transition)
            else -> throw IllegalArgumentException("Incorrect argument")
        }
    }

    abstract fun validateState(state: StateType)

    private fun validateTransition(transition: Transition) {
        if ((transition.stateSymbol !in stateSymbols) && (transition.outputSymbol !in outputSymbols)) {
            throw IllegalArgumentException("Invalid transition")
        }
    }
}