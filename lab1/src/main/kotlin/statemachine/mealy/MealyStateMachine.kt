package statemachine.mealy

import statemachine.StateMachine

class MealyStateMachine(
    override val stateSymbols: Set<String>,
    override val inputSymbols: Set<String>,
    override val outputSymbols: Set<String>
) : StateMachine<String>() {
    override fun validateState(state: String) {
        if (state !in stateSymbols) {
            throw IllegalArgumentException("Incorrect state value")
        }
    }
}