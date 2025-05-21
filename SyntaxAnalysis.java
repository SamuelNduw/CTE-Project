import java.util.*;

public class SyntaxAnalysis {
//
    static final Map<String, Integer> identifierToDigit = new HashMap<>();

    static {
        for (int i = 0; i < 26; i++) {
            identifierToDigit.put(String.valueOf((char) ('A' + i)), i + 1);
            identifierToDigit.put(String.valueOf((char) ('a' + i)), i + 1);
        }
    }

    public static void main(String[] args) {
        String[] expressions = {
                "BEGIN",
                "INTEGER A, B, C, E, M, N, G, H, I, a, c",
                "INPUT A, B, C",
                "LET B = A */ M",
                "LET G = a + c",
                "temp = <s%**h - j / w +d +*$&;",
                "M = A/B+C",
                "N = G/H-I+a*B/c",
                "WRITE M",
                "WRITEE F;",
                "END"
        };

        for (String line : expressions) {
            System.out.println("\nGET A DERIVATION FOR: " + line);
            derive(line);
        }
    }

    public static void derive(String line) {
        // Remove LET, WRITE, etc., to isolate the expression
        line = line.replaceAll("LET|WRITE|INPUT|INTEGER|BEGIN|END", "").trim();

        // Handle assignment
        int equalIndex = line.indexOf('=');
        String rhs = (equalIndex != -1) ? line.substring(equalIndex + 1).trim() : line;

        // Tokenize using operators and whitespace
        String[] tokens = rhs.split("(?=[+\\-*/])|(?<=[+\\-*/])|\\s+");
        List<String> cleaned = new ArrayList<>();
        for (String t : tokens) {
            if (!t.isBlank()) cleaned.add(t.trim());
        }

        // Step 1: Identifier → E<digit>
        StringBuilder step1 = new StringBuilder();
        for (String token : cleaned) {
            if (isIdentifier(token)) {
                step1.append("E").append(identifierToDigit.get(token)).append(" ");
            } else {
                step1.append(token).append(" ");
            }
        }
        System.out.println(step1.toString().trim() + ";");

        // Step 2: E<digit> → digit<digit>
        String step2 = step1.toString();
        for (String token : cleaned) {
            if (isIdentifier(token)) {
                String eToken = "E" + identifierToDigit.get(token);
                String dToken = "digit" + identifierToDigit.get(token);
                step2 = step2.replaceFirst(eToken, dToken);
                System.out.println(step2.trim() + ";");
            }
        }

        // Step 3: digit<digit> → actual digit
        String step3 = step2;
        for (String token : cleaned) {
            if (isIdentifier(token)) {
                String dToken = "digit" + identifierToDigit.get(token);
                String number = identifierToDigit.get(token).toString();
                step3 = step3.replaceFirst(dToken, number);
                System.out.println(step3.trim() + ";");
            }
        }
    }

    public static boolean isIdentifier(String token) {
        return identifierToDigit.containsKey(token);
    }
}
