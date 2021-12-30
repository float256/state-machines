package statemachine

data class Transition(
    var stateSymbol: String,
    var outputSymbol: String
) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Transition -> hashCode() == other.hashCode()
            else -> false
        }
    }

    override fun toString(): String {
        return "$stateSymbol|$outputSymbol"
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }
}