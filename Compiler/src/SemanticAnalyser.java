import java.util.*;

public class SemanticAnalyser {

    private Set<String> declaredIdentifiers;

    private static final Set<String> RESERVED_KEYWORDS = Set.of("BEGIN", "INPUT", "INTEGER", "WRITE", "END", "LET");

    public SemanticAnalyser() {
        declaredIdentifiers = new HashSet<>();
    }

    // Register variables declared in lines like "INTEGER A, B, C"
    public void registerDeclarations(String line) {
        String cleaned = line.replace(",", "");
        String[] tokens = cleaned.split("\\s+");

        for (String token : tokens) {
            if (!RESERVED_KEYWORDS.contains(token.toUpperCase()) && token.matches("[A-Za-z]+")) {
                declaredIdentifiers.add(token);
            }
        }
    }

    // Analyze the semantic correctness of a line (variables declared?)
    public boolean analyzeLine(String line) {
        String upperLine = line.toUpperCase().trim();

        // Check for disallowed symbols
        if (line.matches(".*[\\%\\$&<>;].*")) {
            System.err.println("Semantic Error: Line contains disallowed symbol(s): %, $, &, <, >, ;");
            return false;
        }

        if (upperLine.startsWith("INTEGER") || upperLine.startsWith("INPUT")) {
            registerDeclarations(line);
            return true;
        }

        for (String keyword : RESERVED_KEYWORDS) {
            if (upperLine.equals(keyword)) return true;
        }

//        if (line.contains("=")) {
//            List<String> tokens = extractTokens(line);
//
//            for (String token : tokens) {
//                if (token.matches("[A-Za-z]+") && !RESERVED_KEYWORDS.contains(token.toUpperCase())) {
//                    if (!declaredIdentifiers.contains(token)) {
//                        System.err.println("Semantic Error: Identifier '" + token + "' used before declaration.");
//                        return false;
//                    }
//                }
//            }
//        }

        return true;
    }


    // Tokenize a line into identifiers and operators
    private List<String> extractTokens(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        for (char ch : line.toCharArray()) {
            if (Character.isLetter(ch)) {
                token.append(ch);
            } else {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }

                if ("=+-*/".indexOf(ch) >= 0) {
                    tokens.add(String.valueOf(ch));
                }
            }
        }

        if (token.length() > 0) {
            tokens.add(token.toString());
        }

        return tokens;
    }

    public Set<String> getDeclaredIdentifiers() {
        return declaredIdentifiers;
    }
}