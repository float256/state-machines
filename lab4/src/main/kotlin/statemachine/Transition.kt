package statemachine

data class Transition(
    val from: String,
    val to: String,
    val symbol: String
)