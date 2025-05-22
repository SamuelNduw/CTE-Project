import java.util.*;

public class SyntaxAnalysis {

    static final Map<String, Integer> identifierToDigit = new HashMap<>();

    static {
        for (int i = 0; i < 26; i++) {
            identifierToDigit.put(String.valueOf((char) ('A' + i)), i + 1);
            identifierToDigit.put(String.valueOf((char) ('a' + i)), i + 1);
        }
    }

    ArrayList<String> derive(String line) {

        ArrayList<String> strValue = new ArrayList<>();

        System.out.println("GET A DERIVATION FOR: " + line);

        // Disallow semicolons
        if (line.contains(";")) {
            strValue.add("SYNTAX ERROR: Unexpected ';' in line. -> " + line);
            return strValue;
        }

        // Strip known keywords
        line = line.replaceAll("LET|WRITE|INPUT|INTEGER|BEGIN|END", "").trim();

        // Handle assignment
        int equalIndex = line.indexOf('=');
        if (equalIndex == -1) {
            strValue.add(" ");
            return strValue;
        }

        String lhs = line.substring(0, equalIndex).trim();
        String rhs = line.substring(equalIndex + 1).trim();

        if (lhs.isEmpty() || rhs.isEmpty()) {
            strValue.add("SYNTAX ERROR: Empty left-hand side or right-hand side.");
            return strValue;
        }

        // Tokenize and clean
        String[] tokens = rhs.split("(?=[+\\-*/()])|(?<=[+\\-*/()])|\\s+");
        List<String> cleaned = new ArrayList<>();
        for (String t : tokens) {
            if (!t.isBlank()) cleaned.add(t.trim());
        }

        // Detect consecutive operators
        String operators = "+-*/";
        for (int i = 0; i < cleaned.size() - 1; i++) {
            if (operators.contains(cleaned.get(i)) && operators.contains(cleaned.get(i + 1))) {
                strValue.add("SYNTAX ERROR: Consecutive operators -> " + line);
                return strValue;
            }
        }

        // Derivation steps
        List<String> steps = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (String token : cleaned) {
            if (isIdentifier(token)) {
                current.append("E").append(identifierToDigit.get(token)).append(" ");
            } else {
                current.append(token).append(" ");
            }
        }
        steps.add(current.toString().trim());

        String step = current.toString();
        for (String token : cleaned) {
            if (isIdentifier(token)) {
                step = step.replaceFirst("E" + identifierToDigit.get(token), token);
                steps.add(step.trim());
            }
        }

        for (String s : steps) {
            strValue.add(s);
        }
        return strValue;
    }


    public static boolean isIdentifier(String token) {
        return identifierToDigit.containsKey(token);
    }
}