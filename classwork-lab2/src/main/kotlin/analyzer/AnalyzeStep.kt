package analyzer

data class AnalyzeStep(
    val id: Int,
    val symbol: String,
    val directSymbols: Set<String>,
    val isShift: Boolean,
    val pointerToNextStep: Int?,
    val isThrowError: Boolean,
    val isAddToStack: Boolean,
    val isFinish: Boolean
)