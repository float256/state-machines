import statemachine.mealy.MealyStateMachineFabric
import statemachine.mealy.MealyStateMachineMinimizer
import statemachine.moore.MooreStateMachineFabric

fun main() {
    val mooreFabric = MooreStateMachineFabric()
    val mealyFabric = MealyStateMachineFabric()
    val minimizer = MealyStateMachineMinimizer()

    val (minimizedMachine, groups) = when (readLine()?.trim()?.lowercase()) {
        "mealy" -> {
            val mealyStateMachine = mealyFabric.create(System.`in`)
            minimizer.minimize(mealyStateMachine)
        }
        "moore" -> {
            val mooreStateMachine = mooreFabric.create(System.`in`)
            val mealyStateMachine = mealyFabric.create(mooreStateMachine)
            minimizer.minimize(mealyStateMachine)
        }
        else -> throw IllegalArgumentException("Incorrect state machine type")
    }

    println(
        """
        |Minimized state machine: 
        |$minimizedMachine
        |
        |Groups:
        |$groups
        """.trimMargin("|")
    )
}