package tinyjs.lexer

import java.lang.RuntimeException

class LexerException(
    override val message: String
): RuntimeException(message)