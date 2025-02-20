package RENFADFA;
import java.util.*;
import java.util.regex.*;
import java.io.*;


public class LexicalAnalyzer {
    private static final String KEYWORDS = "\\b(_int|_float|_string|_bool|_if|_else|_while|_return|_input|_output)\\b";
    private static final String IDENTIFIERS = "\\b[a-z][a-zA-Z0-9_]*\\b";
    private static final String INTEGERS = "\\b\\d+\\b";
    private static final String FLOATS = "\\b\\d+\\.\\d+\\b";
    private static final String STRINGS = "\"[^\"]*\"";
    private static final String BOOLEANS = "\\b(true|false)\\b";
    private static final String OPERATORS = "\\+|-|\\*|/|==|!=|<=|>=|<|>|=|\\(|\\)|\\{|\\}|\\;|,|\\.";
    private static final String SINGLE_LINE_COMMENT = "//.*";
    private static final String MULTI_LINE_COMMENT = "/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/";
    private static final String WHITESPACE = "\\s+";
    private static final String UNKNOWN = "."; // Matches any single character

    private static final List<Pattern> tokenPatterns = Arrays.asList(
        Pattern.compile(SINGLE_LINE_COMMENT),
        Pattern.compile(MULTI_LINE_COMMENT),
        Pattern.compile(KEYWORDS),
        Pattern.compile(IDENTIFIERS),
        Pattern.compile(INTEGERS),
        Pattern.compile(FLOATS),
        Pattern.compile(BOOLEANS),
        Pattern.compile(STRINGS),
        Pattern.compile(OPERATORS),
        Pattern.compile(WHITESPACE),
        Pattern.compile(UNKNOWN) // Last resort: match any unknown character
    );

    private static final List<String> tokenNames = Arrays.asList(
        "SINGLE_LINE_COMMENT",
        "MULTI_LINE_COMMENT",
        "KEYWORD",
        "IDENTIFIER",
        "INTEGER",
        "FLOAT",
        "BOOLEAN",
        "STRING",
        "OPERATOR",
        "WHITESPACE",
        "UNKNOWN"
    );

    private ErrorHandler errorHandler;

    public LexicalAnalyzer(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public List<Token> tokenize(String sourceCode) {
        List<Token> tokens = new ArrayList<>();
        int position = 0;

        while (position < sourceCode.length()) {
            boolean matched = false;

            for (int i = 0; i < tokenPatterns.size(); i++) {
                Matcher matcher = tokenPatterns.get(i).matcher(sourceCode.substring(position));
                if (matcher.lookingAt()) {
                    String tokenValue = matcher.group();
                    String tokenName = tokenNames.get(i);

                    if (!tokenName.equals("WHITESPACE") && !tokenName.equals("SINGLE_LINE_COMMENT") && !tokenName.equals("MULTI_LINE_COMMENT")) {
                        if (tokenName.equals("UNKNOWN")) {
                            errorHandler.reportError("Unknown token: " + tokenValue, 1); // Simplified line number handling
                        } else {
                            tokens.add(new Token(tokenName, tokenValue));
                        }
                    }

                    position += tokenValue.length();
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                errorHandler.reportError("Unexpected character at position " + position, 1); // Simplified line number handling
                position++; // Skip the unknown character to continue processing
            }
        }

        return tokens;
    }

    public static class Token {
        private final String type;
        private final String value;

        public Token(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}