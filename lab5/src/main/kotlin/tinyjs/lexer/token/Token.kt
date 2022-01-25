package tinyjs.lexer.token

data class Token(
    val type: String,
    val startPosition: Int,
    val endPosition: Int
)