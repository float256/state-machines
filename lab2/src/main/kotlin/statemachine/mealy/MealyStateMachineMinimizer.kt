package statemachine.mealy

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import statemachine.StateMachineMinimizer
import statemachine.Transition
import java.util.*

private typealias MealyTransitionTable = Table<String, String, Transition>
private typealias MealyMinimizeGroupMap = MutableMap<String, MutableSet<String>>
private typealias MealyGroupTransitionTable = Table<String, String, String>

class MealyStateMachineMinimizer : StateMachineMinimizer<String, MealyStateMachine> {
    override fun minimize(machine: MealyStateMachine): Pair<MealyStateMachine, Set<Set<String>>> {
        val transitions = machine.getAllTransitions()

        var prevGroups: MealyMinimizeGroupMap? = null
        var currGroups: MealyMinimizeGroupMap = splitByGroupFromOutputSymbolsTable(
            transitions.rowMap()
                .map { (state, inputTransitionMap) ->
                    Pair(state!!, inputTransitionMap
                        .map { (_, transition) -> transition.outputSymbol }
                        .toList())
                }.toMap()
        )

        while (prevGroups != currGroups) {
            prevGroups = currGroups
            currGroups = splitByGroup(transitions, prevGroups)
        }

        return Pair(
            generateStateMachine(transitions, machine.outputSymbols, currGroups),
            currGroups.map { (_, allStatesFromGroup) -> allStatesFromGroup.toSet() }.toSet()
        )
    }

    private fun splitByGroup(
        allTransitions: MealyTransitionTable,
        groups: MealyMinimizeGroupMap
    ): MealyMinimizeGroupMap {
        val newAllGroups: MealyMinimizeGroupMap = mutableMapOf()
        val groupTransitions = generateGroupTransitionsTable(allTransitions, groups)

        groups.forEach { (groupName, statesFromGroup) ->
            val splitGroup = splitByGroupFromOutputSymbolsTable(
                groupTransitions.rowMap()
                    .filter { (state, _) -> statesFromGroup.contains(state) }
                    .map { (state, inputTransitionMap) ->
                        Pair(state!!, inputTransitionMap
                            .map { (_, transition) -> transition }
                            .toList())
                    }
                    .toMap()
            )
            if (splitGroup.size == 1) {
                newAllGroups[groupName] = statesFromGroup
            } else {
                splitGroup.forEach { (currSplitGroup, currGroupContent) ->
                    newAllGroups[currSplitGroup] = currGroupContent
                }
            }
        }
        return newAllGroups
    }

    private fun splitByGroupFromOutputSymbolsTable(outputSymbolsTable: Map<String, List<String>>): MealyMinimizeGroupMap {
        return outputSymbolsTable
            .toList()
            .groupBy { (_, groupTransitions) -> groupTransitions }
            .map { (_, groupedStatesWithOutputSymbolsList) ->
                val groupName = UUID.randomUUID().toString()
                val groupedState = groupedStatesWithOutputSymbolsList.map { (state, _) -> state }.toMutableSet()
                Pair(groupName, groupedState)
            }
            .toMap()
            .toMutableMap()
    }

    private fun getGroup(state: String, groups: MealyMinimizeGroupMap): String? {
        return groups
            .filter { (_, statesInGroup) -> statesInGroup.contains(state) }
            .map { (group, _) -> group }
            .firstOrNull()
    }

    private fun generateGroupTransitionsTable(
        transitions: MealyTransitionTable,
        groups: MealyMinimizeGroupMap
    ): MealyGroupTransitionTable {
        val groupTransitionTable: MealyGroupTransitionTable = HashBasedTable.create()
        transitions.rowKeySet().forEach { stateSymbol ->
            transitions.columnKeySet().forEach { inputSymbol ->
                groupTransitionTable.put(stateSymbol, inputSymbol, getGroup(stateSymbol, groups))
            }
        }
        return groupTransitionTable
    }

    private fun generateStateMachine(
        allTransitions: MealyTransitionTable,
        outputSymbols: Set<String>,
        allGroups: MealyMinimizeGroupMap
    ): MealyStateMachine {
        val states = allGroups.map { (_, statesInGroup) -> statesInGroup.first() }.toSet()

        val mealyStateMachine = MealyStateMachine(
            states,
            allTransitions.columnKeySet(),
            outputSymbols
        )

        allTransitions.rowKeySet().filter { states.contains(it) }
            .forEach { stateSymbol ->
                allTransitions.columnKeySet()
                    .forEach { inputSymbol ->
                        mealyStateMachine.set(stateSymbol, inputSymbol, allTransitions[stateSymbol, inputSymbol]!!)
                    }
            }

        return mealyStateMachine
    }
}