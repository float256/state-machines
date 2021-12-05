package statemachine.moore

import statemachine.StateMachine
import statemachine.StateMachineFabric
import statemachine.Transition
import statemachine.mealy.MealyStateMachine
import java.io.InputStream
import java.util.*

class MooreStateMachineFabric: StateMachineFabric {
    override fun create(inputStream: InputStream): StateMachine<out Any> {
        val scanner = Scanner(inputStream)
        val allStates = scanner.nextLine().split(":")[1]
            .split(" ").filter{it.isNotBlank()}
        val allInputSymbols = scanner.nextLine().split(":")[1]
            .split(" ").filter{it.isNotBlank()}
        val allOutputSymbols = scanner.nextLine().split(":")[1]
            .split(" ").filter{it.isNotBlank()}
        val machine = MooreStateMachine(allStates.toSet(), allInputSymbols.toSet(), allOutputSymbols.toSet())

        while (scanner.hasNext()) {
            val currLineParts = scanner.nextLine().split(":")
            val splitMooreState = currLineParts[0].trim().split("|")
            val currState = MooreState(splitMooreState[0], splitMooreState[1])
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
            is MealyStateMachine -> fromMealy(stateMachine)
            is MooreStateMachine -> stateMachine
            else -> throw IllegalArgumentException("Unknown state machine type")
        }
    }

    private fun fromMealy(mealyStateMachine: MealyStateMachine): MooreStateMachine {
        val mooreStateMachine = MooreStateMachine(
            mealyStateMachine.stateSymbols,
            mealyStateMachine.inputSymbols,
            mealyStateMachine.outputSymbols
        )
        val mealyMachineTransition = mealyStateMachine.getAllTransitions()

        val mooreMachineStates = mutableSetOf<MooreState>()
        mealyMachineTransition.rowKeySet().forEach { mealyState ->
            mealyMachineTransition.columnKeySet().forEach { mealyInputSymbol ->
                val mealyTransition = mealyMachineTransition.get(mealyState, mealyInputSymbol)!!
                mooreMachineStates.add(MooreState(mealyTransition.stateSymbol, mealyTransition.outputSymbol))
            }
        }

        mooreMachineStates.forEach { mooreState ->
            val transitionsForCurrState = mealyMachineTransition.row(mooreState.stateSymbol)
            transitionsForCurrState.forEach { (inputSymbol, transition) ->
                mooreStateMachine.set(mooreState, inputSymbol, transition)
            }
        }
        return mooreStateMachine
    }
}