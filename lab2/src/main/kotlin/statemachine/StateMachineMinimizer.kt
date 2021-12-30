package statemachine

interface StateMachineMinimizer<StateSymbolType: Any, StateMachineType: StateMachine<StateSymbolType>> {
    fun minimize(machine: StateMachineType): Pair<StateMachineType, Set<Set<StateSymbolType>>>
}