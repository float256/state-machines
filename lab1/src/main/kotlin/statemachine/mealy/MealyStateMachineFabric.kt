package statemachine.mealy

import statemachine.StateMachine
import statemachine.StateMachineFabric
import statemachine.Transition
import statemachine.moore.MooreStateMachine
import java.io.InputStream
import java.util.*

class MealyStateMachineFabric : StateMachineFabric { // Factory
    override fun create(inputStream: InputStream): StateMachine<out Any> {
        val scanner = Scanner(inputStream)
        val allStates = scanner.nextLine().split(":")[1]
            .split(" ").filter{it.isNotBlank()}
        val allInputSymbols = scanner.nextLine().split(":")[1]
            .split(" ").filter{it.isNotBlank()}
        val allOutputSymbols = scanner.nextLine().split(":")[1]
            .split(" ").filter{it.isNotBlank()}
        val machine = MealyStateMachine(allStates.toSet(), allInputSymbols.toSet(), allOutputSymbols.toSet())

        while (scanner.hasNext()) {
            val currLineParts = scanner.nextLine().split(":")
            val currState = currLineParts[0].trim()
            val allTransitions = currLineParts[1].split(" ")
                .filter{it.isNotBlank()}
                .map {
                    val splitState = it.split("|")
                    Transition(splitState[0].trim(), splitState[1].trim())
                }

            allInputSymbols.zip(allTransitions).forEach {
                val (currInputSymbol, currTransition) = it
                machine.set(currState, currInputSymbol, currTransition)
            }
        }
        return machine
    }

    override fun create(stateMachine: StateMachine<out Any>): StateMachine<out Any> {
        return when (stateMachine) {
            is MealyStateMachine -> stateMachine
            is MooreStateMachine -> fromMoore(stateMachine)
            else -> throw IllegalArgumentException("Unknown state machine type")
        }
    }

    private fun fromMoore(mooreStateMachine: MooreStateMachine): MealyStateMachine {
        val mealyStateMachine = MealyStateMachine(
            mooreStateMachine.stateSymbols,
            mooreStateMachine.inputSymbols,
            mooreStateMachine.outputSymbols
        )

        val mooreMachineTransitions = mooreStateMachine.getAllTransitions()

        mooreMachineTransitions.rowKeySet().forEach { mooreState ->
            mooreMachineTransitions.columnKeySet().forEach { mooreInputSymbol ->
                if (mooreMachineTransitions.contains(mooreState.stateSymbol, mooreInputSymbol)) {
                    throw IllegalArgumentException("The resulting Mealy state machine is not deterministic")
                }
                val mooreTransition = mooreMachineTransitions.get(mooreState, mooreInputSymbol)!!
                mealyStateMachine.set(mooreState.stateSymbol, mooreInputSymbol, mooreTransition)
            }
        }

        return mealyStateMachine
    }
}