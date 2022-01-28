import statemachine.DFAThompsonAlgorithm
import statemachine.StateMachineFactory
import java.util.*

fun main() {
    val factory = StateMachineFactory()
    val algorithm = DFAThompsonAlgorithm()
    val nfaMachine = factory.create(Scanner(System.`in`))
    val dfaMachine = algorithm.determinate(nfaMachine)
    println(dfaMachine)
}
