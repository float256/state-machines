package tinyjs.lexer

data class Token(
    val type: TokenType,
    val startPosition: Int,
    val endPosition: Int
)