import java.util.*;

public class SyntaxAnalysis {

    static Set<String> validIdentifiers = new HashSet<>();

    static {
        for (char c = 'A'; c <= 'Z'; c++) validIdentifiers.add(String.valueOf(c));
        for (char c = 'a'; c <= 'z'; c++) validIdentifiers.add(String.valueOf(c));
    }

    void derive(String line) {
        String[] tokens = line.split("\\s+|(?=[+\\-*/=;<>%$&])|(?<=[+\\-*/=;<>%$&])");

        List<String> rhs = new ArrayList<>();
        boolean equalsSeen = false;
        for (String token : tokens) {
            if (token.equals("=")) {
                equalsSeen = true;
                continue;
            }
            if (equalsSeen && !token.isEmpty()) {
                rhs.add(token);
            }
        }

        if (rhs.isEmpty()) {
            System.out.println("No expression to derive.");
            return;
        }

        String step = "E -> " + String.join(" ", rhs);
        System.out.println(step);

        StringBuilder current = new StringBuilder(String.join(" ", rhs));

        for (String token : rhs) {
            if (isIdentifier(token)) {
                int index = current.indexOf(token);
                if (index >= 0) {
                    current.replace(index, index + token.length(), "E" + token);
                    System.out.println("-> " + current);
                }
            }
        }

        for (String token : rhs) {
            if (isIdentifier(token)) {
                int index = current.indexOf("E" + token);
                if (index >= 0) {
                    current.replace(index, index + token.length() + 1, token);
                    System.out.println("-> " + current);
                }
            }
        }
    }

    public static boolean isIdentifier(String token) {
        return validIdentifiers.contains(token);
    }
}