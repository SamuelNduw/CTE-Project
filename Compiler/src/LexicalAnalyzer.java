import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LexicalAnalyzer {

    // === Token types ===
    public enum TokenType {
        KEYWORD,
        IDENTIFIER,
        OPERATOR,
        SYMBOL,
        LEXICAL_ERROR
    }

    public static class Token {
        public final String lexeme;
        public final TokenType type;

        public Token(String lexeme, TokenType type) {
            this.lexeme = lexeme;
            this.type   = type;
        }

        @Override
        public String toString() {
            return String.format("[%s: '%s']", type, lexeme);
        }
    }

    // === Language definition ===
    private static final Set<String> KEYWORDS = Set.of(
            "BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END"
    );

    private static final Set<Character> OPERATORS = Set.of(
            '+', '-', '*', '/'
    );

    private static final Set<Character> SYMBOLS = Set.of(
            '=', ',', ';', '%', '$', '&', '<', '>'
    );

    // Characters that are outright illegal in this stage
    private static final Set<Character> ILLEGAL_CHARS = Set.of(
//            '%', '$', '&', '<', '>', // per spec
            // digits are also disallowed here
            '0','1','2','3','4','5','6','7','8','9'
    );

    private boolean isKeywordTypo(String word) {
        for (String kw : KEYWORDS) {
            if (levenshteinDistance(word, kw) == 1) return true;
        }
        return false;
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1],
                            Math.min(dp[i - 1][j], dp[i][j - 1])
                    );
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }


    /**
     * Perform lexical analysis on one line of source code.
     * @param line one source‐code line
     * @return list of tokens (in order). Any unrecognized or illegal lexeme
     *         becomes a TokenType.LEXICAL_ERROR token.
     */
    public List<Token> analyze(String line) {
        List<Token> tokens = new ArrayList<>();
        int i = 0, n = line.length();

        while (i < n) {
            char c = line.charAt(i);

            // 1) skip whitespace
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // 2) letter → KEYWORD or IDENTIFIER
            if (Character.isLetter(c)) {
                int start = i;
                while (i < n && Character.isLetter(line.charAt(i))) {
                    i++;
                }
                String lexeme = line.substring(start, i);
                if (KEYWORDS.contains(lexeme)) {
                    tokens.add(new Token(lexeme, TokenType.KEYWORD));
                } else if (isKeywordTypo(lexeme)) {
                    tokens.add(new Token(lexeme + " (possibly misspelled keyword)", TokenType.LEXICAL_ERROR));
                } else {
                    tokens.add(new Token(lexeme, TokenType.IDENTIFIER));
                }
                continue;
            }

            // 3) operator
            if (OPERATORS.contains(c)) {
                tokens.add(new Token(String.valueOf(c), TokenType.OPERATOR));
                i++;
                continue;
            }

            // 4) symbol
            if (SYMBOLS.contains(c)) {
                tokens.add(new Token(String.valueOf(c), TokenType.SYMBOL));
                i++;
                continue;
            }

            // 5) illegal character
            if (ILLEGAL_CHARS.contains(c)) {
                tokens.add(new Token(String.valueOf(c), TokenType.LEXICAL_ERROR));
                i++;
                continue;
            }

            // 6) anything else (e.g. punctuation, underscores, etc.) → error
            tokens.add(new Token(String.valueOf(c), TokenType.LEXICAL_ERROR));
            i++;
        }

        return tokens;
    }
}