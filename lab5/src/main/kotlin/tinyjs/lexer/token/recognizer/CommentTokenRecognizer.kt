package tinyjs.lexer.token.recognizer

import tinyjs.lexer.token.Token

class CommentTokenRecognizer : TokenRecognizer {
    override fun recognize(stringForRecognition: String, startPosition: Int): Token? {
        if (isOneLineCommentStart(stringForRecognition, startPosition)) {
            var endPosition = startPosition + 2
            while (
                (endPosition < stringForRecognition.length) &&
                (stringForRecognition[endPosition] != '\n')
            ) {
                endPosition++
            }
            return Token("ONE_LINE_COMMENT", startPosition, endPosition)
        } else if (isMultipleLineCommentStart(stringForRecognition, startPosition)) {
            var endPosition = startPosition + 2
            while (
                (endPosition < stringForRecognition.length) &&
                !isMultipleLineCommentEnd(stringForRecognition, startPosition)
            ) {
                endPosition++
            }
            return Token("MULTIPLE_LINE_COMMENT_TOKEN", startPosition, endPosition)
        } else {
            return null
        }
    }

    private fun isOneLineCommentStart(stringForRecognition: String, startPosition: Int): Boolean {
        return (startPosition + 1 < stringForRecognition.length) &&
                (stringForRecognition[startPosition] == '/') &&
                (stringForRecognition[startPosition + 1] == '/')
    }

    private fun isMultipleLineCommentStart(stringForRecognition: String, startPosition: Int): Boolean {
        return (startPosition + 1 < stringForRecognition.length) &&
                (stringForRecognition[startPosition] == '/') &&
                (stringForRecognition[startPosition + 1] == '*')
    }

    private fun isMultipleLineCommentEnd(stringForRecognition: String, startPosition: Int): Boolean {
        return (startPosition + 1 < stringForRecognition.length) &&
                (stringForRecognition[startPosition] == '*') &&
                (stringForRecognition[startPosition + 1] == '/')
    }
}