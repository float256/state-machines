import statemachine.mealy.MealyStateMachineFabric
import statemachine.moore.MooreStateMachineFabric

fun main() {
    val mooreFabric = MooreStateMachineFabric()
    val mealyFabric = MealyStateMachineFabric()
    when (readLine()?.trim()?.lowercase()) {
        "mealy" -> {
            val mealyStateMachine = mealyFabric.create(System.`in`)
            val mooreStateMachine = mooreFabric.create(mealyStateMachine)
            println(mooreStateMachine.toString())
        }
        "moore" -> {
            val mooreStateMachine = mooreFabric.create(System.`in`)
            val mealyStateMachine = mealyFabric.create(mooreStateMachine)
            println(mealyStateMachine.toString())
        }
        else -> throw IllegalArgumentException("Incorrect state machine type")
    }
}