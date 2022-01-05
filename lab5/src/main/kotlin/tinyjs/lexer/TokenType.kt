package tinyjs.lexer

enum class TokenType(
    val regex: String
) {
    FUNCTION_KEYWORD("function"),
    IF_KEYWORD("if"),
    ELSE_KEYWORD("else"),
    IMPORT_KEYWORD("import"),
    EXPORT_KEYWORD("export"),
    FROM_KEYWORD("from"),
    IDENTIFIER("([a-zA-Z_\$][a-zA-Z_\$\\d]*)+"),
    STRING("\"[^\"^\n]*\""),
    OPEN_CURLY_BRACE("\\{"),
    CLOSED_CURLY_BRACE("\\}"),
    OPEN_PARENTHESIS("\\("),
    CLOSED_PARENTHESIS("\\)"),
    PLUS_SIGH("\\+"),
    MINUS_SIGH("\\-"),
    ASTERISK_SIGH("\\*"),
    SLASH_SIGH("\\/"),
    EQUALS_SIGH("\\="),
    SEMICOLON("\\;"),
    POINT("\\."),
    EOL("\n")
}