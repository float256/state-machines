package statemachine.moore

data class MooreState(
    val stateSymbol: String,
    val outputSymbol: String
) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is MooreState -> hashCode() == other.hashCode()
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