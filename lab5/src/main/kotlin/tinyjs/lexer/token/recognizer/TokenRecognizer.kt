package tinyjs.lexer.token.recognizer

import tinyjs.lexer.token.Token

interface TokenRecognizer {
    fun recognize(stringForRecognition: String, startPosition: Int): Token?
}