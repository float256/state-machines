import regex.RegexFactory
import regex.RegexToStateMachineConvertor
import statemachine.DFAAlgorithm
import statemachine.StateMachineEpsilonRemover
import statemachine.recreateStateMachineWithDefaultNames

fun main() {
    val regexValidator = RegexFactory()
    val regexConvertor = RegexToStateMachineConvertor()
    val stateMachineEpsilonRemover = StateMachineEpsilonRemover()
    val dfaAlgorithm = DFAAlgorithm()
    while (true) {
        val regexString = readLine()!!

        if (regexString != "") {
            val regex = regexValidator.create(regexString)
            val stateMachineWithEpsilon = regexConvertor.convert(regex)
            val stateMachineWithoutEpsilon = stateMachineEpsilonRemover
                .removeEpsilonTransitions(stateMachineWithEpsilon)
            val determinedStateMachine = dfaAlgorithm.determinate(stateMachineWithoutEpsilon)
            val stateMachineWithDefaultNaming = recreateStateMachineWithDefaultNames(determinedStateMachine)
            println(stateMachineWithDefaultNaming)
        } else {
            println("Empty string")
        }
    }
}
