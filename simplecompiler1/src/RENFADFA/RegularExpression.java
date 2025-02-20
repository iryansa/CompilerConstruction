package RENFADFA;
import java.util.regex.Pattern;

public class RegularExpression {
    // Regular expressions for different token types
    private static final String KEYWORDS = "\\b(_int|_float|_string|_bool|_if|_else|_while|_return|_input|_output)\\b";
    private static final String IDENTIFIERS = "\\b[a-z][a-zA-Z0-9_]*\\b";
    private static final String INTEGERS = "\\b\\d+\\b";
    private static final String FLOATS = "\\b\\d+\\.\\d+\\b";
    private static final String STRINGS = "\"[^\"]*\"";
    private static final String BOOLEANS = "\\b(true|false)\\b";
    private static final String OPERATORS = "\\+|-|\\*|/|==|!=|<=|>=|<|>|=|\\(|\\)|\\{|\\}|\\;|,";
    private static final String SINGLE_LINE_COMMENT = "//.*";
    private static final String MULTI_LINE_COMMENT = "/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/";

    // Method to get the pattern for a specific token type
    public static Pattern getPattern(String tokenType) {
        switch (tokenType) {
            case "KEYWORDS":
                return Pattern.compile(KEYWORDS);
            case "IDENTIFIERS":
                return Pattern.compile(IDENTIFIERS);
            case "INTEGERS":
                return Pattern.compile(INTEGERS);
            case "FLOATS":
                return Pattern.compile(FLOATS);
            case "STRINGS":
                return Pattern.compile(STRINGS);
            case "BOOLEANS":
                return Pattern.compile(BOOLEANS);
            case "OPERATORS":
                return Pattern.compile(OPERATORS);
            case "SINGLE_LINE_COMMENT":
                return Pattern.compile(SINGLE_LINE_COMMENT);
            case "MULTI_LINE_COMMENT":
                return Pattern.compile(MULTI_LINE_COMMENT);
            default:
                throw new IllegalArgumentException("Unknown token type: " + tokenType);
        }
    }
}