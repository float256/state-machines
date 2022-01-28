package statemachine

import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table

abstract class StateMachine<StateType : Any> {
    abstract val stateSymbols: Set<String>
    abstract val inputSymbols: Set<String>
    abstract val outputSymbols: Set<String>

    private val allTransitions: Table<StateType, String, Transition> = HashBasedTable.create()

    fun getAllTransitions(): ImmutableTable<StateType, String, Transition> = ImmutableTable.copyOf(allTransitions)

    fun set(state: StateType, inputSymbol: String, transition: Transition) {
        validateState(state)
        validateTransition(transition)
        allTransitions.put(state, inputSymbol, transition)
    }

    abstract fun validateState(state: StateType)

    private fun validateTransition(transition: Transition) {
        if ((transition.stateSymbol !in stateSymbols) && (transition.outputSymbol !in outputSymbols)) {
            throw IllegalArgumentException("Invalid transition")
        }
    }

    override fun toString(): String {
        return "${getMachineSymbolsDescription()}\n${getTransitionsDescription()}".trimIndent()
    }

    private fun getMachineSymbolsDescription(): String = """
        Q: ${allTransitions.rowKeySet().joinToString(" ")}
        X: ${allTransitions.columnKeySet().joinToString(" ")}
        Y: ${outputSymbols.joinToString(" ")}
        """.trimIndent()

    private fun getTransitionsDescription(): String = allTransitions.rowKeySet()
        .joinToString("\n") { currState ->
            "$currState: ${allTransitions.row(currState).values.joinToString(" ")}"
        }
}