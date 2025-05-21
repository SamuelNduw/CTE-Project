import java.util.*;

public class SyntaxAnalysis {

    static final Map<String, Integer> identifierToDigit = new HashMap<>();

    static {
        for (int i = 0; i < 26; i++) {
            identifierToDigit.put(String.valueOf((char) ('A' + i)), i + 1);
            identifierToDigit.put(String.valueOf((char) ('a' + i)), i + 1);
        }
    }

    void derive(String line) {
        System.out.println("GET A DERIVATION FOR: " + line);

        // Strip known keywords
        line = line.replaceAll("LET|WRITE|INPUT|INTEGER|BEGIN|END", "").trim();

        // Handle assignment
        int equalIndex = line.indexOf('=');
        String lhs = equalIndex != -1 ? line.substring(0, equalIndex).trim() : "";
        String rhs = equalIndex != -1 ? line.substring(equalIndex + 1).trim() : line;

        // Tokenize
        String[] tokens = rhs.split("(?=[+\\-*/()])|(?<=[+\\-*/()])|\\s+");
        List<String> cleaned = new ArrayList<>();
        for (String t : tokens) {
            if (!t.isBlank()) cleaned.add(t.trim());
        }

        // Step-by-step transformations
        List<String> steps = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        // Step 1: Replace all identifiers with E<digit>
        for (String token : cleaned) {
            if (isIdentifier(token)) {
                current.append("E").append(identifierToDigit.get(token)).append(" ");
            } else {
                current.append(token).append(" ");
            }
        }
        steps.add(current.toString().trim());

        // Step 2: Replace E<digit> with identifier, one at a time (in reverse of original cleaned list)
        String step = current.toString();
        for (String token : cleaned) {
            if (isIdentifier(token)) {
                String eToken = "E" + identifierToDigit.get(token);
                step = step.replaceFirst(eToken, token);
                steps.add(step.trim());
            }
        }

        // Print all steps
        for (String s : steps) {
            System.out.println(s);
        }
        System.out.println();
    }


    public static boolean isIdentifier(String token) {
        return identifierToDigit.containsKey(token);
    }
}