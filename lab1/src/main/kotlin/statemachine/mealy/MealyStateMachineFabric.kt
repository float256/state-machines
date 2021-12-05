package statemachine.mealy

import statemachine.StateMachine
import statemachine.StateMachineFabric
import statemachine.Transition
import statemachine.moore.MooreStateMachine
import java.io.InputStream
import java.util.*

class MealyStateMachineFabric: StateMachineFabric {
    override fun parse(inputStream: InputStream): StateMachine<out Any> {
        val scanner = Scanner(inputStream)
        return initMachine(scanner)
    }

    override fun parse(stateMachine: StateMachine<out Any>): StateMachine<out Any> {
        return when(stateMachine) {
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

        return mealyStateMachine
    }

    private fun initMachine(scanner: Scanner): MealyStateMachine {
        val allStates = scanner.nextLine().split(":")[1]
            .split(" ").filter(Objects::nonNull)
        val allInputSymbols = scanner.nextLine().split(":")[1]
            .split(" ").filter(Objects::nonNull)
        val allOutputSymbols = scanner.nextLine().split(":")[1]
            .split(" ").filter(Objects::nonNull)
        val machine = MealyStateMachine(allStates.toSet(), allInputSymbols.toSet(), allOutputSymbols.toSet())

        while (scanner.hasNext()) {
            val currLineParts = scanner.nextLine().split(":")
            val currState = currLineParts[0].trim()
            val allTransitions = currLineParts[1].split(" ")
                .filter(Objects::nonNull)
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
}