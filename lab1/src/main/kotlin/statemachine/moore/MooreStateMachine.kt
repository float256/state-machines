package statemachine.moore

import statemachine.StateMachine

class MooreStateMachine(
    override val stateSymbols: Set<String>,
    override val inputSymbols: Set<String>,
    override val outputSymbols: Set<String>
) : StateMachine<MooreState>() {
    override fun validateState(state: MooreState) {
        if ((state.stateSymbol !in stateSymbols) || (state.outputSymbol !in outputSymbols)) {
            throw IllegalArgumentException("Incorrect state value")
        }
    }
}