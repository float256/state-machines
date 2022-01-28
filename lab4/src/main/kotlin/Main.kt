import regex.RegexFactory
import regex.RegexToStateMachineConvertor

fun main() {
    val regexValidator = RegexFactory()
    val regexConvertor = RegexToStateMachineConvertor()
    while (true) {
        val regexString = readLine()!!

        if (regexString != "") {
            val regex = regexValidator.create(regexString)
            val stateMachineWithEpsilon = regexConvertor.convert(regex)
            println(stateMachineWithEpsilon)
        } else {
            println("Empty string")
        }
    }
}
