import java.util.*;

public class SyntaxAnalysis {

    static Set<String> validIdentifiers = new HashSet<>();

    static {
        for (char c = 'A'; c <= 'Z'; c++) validIdentifiers.add(String.valueOf(c));
        for (char c = 'a'; c <= 'z'; c++) validIdentifiers.add(String.valueOf(c));
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
