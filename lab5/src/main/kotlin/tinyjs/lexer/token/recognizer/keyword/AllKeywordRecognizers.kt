package tinyjs.lexer.token.recognizer.keyword

class FunctionKeywordRecognizer: KeywordRecognizer() {
    override val keyword: String = "function"
    override val tokenName: String = "FUNCTION_KEYWORD"
}

class IfKeywordRecognizer: KeywordRecognizer() {
    override val keyword: String = "if"
    override val tokenName: String = "IF_KEYWORD"
}

class ElseKeywordRecognizer: KeywordRecognizer() {
    override val keyword: String = "else"
    override val tokenName: String = "ELSE_KEYWORD"
}

class ImportKeywordRecognizer: KeywordRecognizer() {
    override val keyword: String = "import"
    override val tokenName: String = "IMPORT_KEYWORD"
}

class ExportKeywordRecognizer: KeywordRecognizer() {
    override val keyword: String = "export"
    override val tokenName: String = "EXPORT_KEYWORD"
}

class FromKeywordRecognizer: KeywordRecognizer() {
    override val keyword: String = "from"
    override val tokenName: String = "FROM_KEYWORD"
}

class OpenCurlyBraceRecognizer: KeywordRecognizer() {
    override val keyword: String = "{"
    override val tokenName: String = "OPEN_CURLY_BRACE"
}

class ClosedCurlyBraceRecognizer: KeywordRecognizer() {
    override val keyword: String = "}"
    override val tokenName: String = "CLOSED_CURLY_BRACE"
}

class OpenParenthesisBraceRecognizer: KeywordRecognizer() {
    override val keyword: String = "("
    override val tokenName: String = "OPEN_PARENTHESIS_BRACE"
}

class ClosedParenthesisBraceRecognizer: KeywordRecognizer() {
    override val keyword: String = ")"
    override val tokenName: String = "CLOSED_PARENTHESIS_BRACE"
}

class PlusSighRecognizer: KeywordRecognizer() {
    override val keyword: String = "+"
    override val tokenName: String = "PLUS_SIGH"
}

class MinusSighRecognizer: KeywordRecognizer() {
    override val keyword: String = "-"
    override val tokenName: String = "MINUS_SIGH"
}

class AsteriskSighRecognizer: KeywordRecognizer() {
    override val keyword: String = "*"
    override val tokenName: String = "ASTERISK_SIGH"
}

class SlashSighRecognizer: KeywordRecognizer() {
    override val keyword: String = "="
    override val tokenName: String = "SLASH_SIGH"
}

class EqualsSighRecognizer: KeywordRecognizer() {
    override val keyword: String = "/"
    override val tokenName: String = "EQUALS_SIGH"
}

class SemicolonSighRecognizer: KeywordRecognizer() {
    override val keyword: String = ";"
    override val tokenName: String = "SEMICOLON_SIGH"
}

class PointSighRecognizer: KeywordRecognizer() {
    override val keyword: String = "."
    override val tokenName: String = "POINT"
}

class EOLRecognizer: KeywordRecognizer() {
    override val keyword: String = "\n"
    override val tokenName: String = "EOL"
}
