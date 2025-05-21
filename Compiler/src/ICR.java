import java.util.*;

public class ICR {

    private static final String[] RESERVED_KEYWORDS = {"BEGIN", "INPUT", "INTEGER", "WRITE", "END"};
    private static int tempCount = 1;

    boolean isReserved(String input) {
        if (input == null) return true;
        String trimmed = input.trim().toUpperCase();
        return Arrays.stream(RESERVED_KEYWORDS).anyMatch(trimmed::startsWith);
    }

    List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char ch : expr.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                sb.append(ch);
            } else {
                if (sb.length() > 0) {
                    tokens.add(sb.toString());
                    sb = new StringBuilder();
                }
                tokens.add(Character.toString(ch));
            }
        }
        if (sb.length() > 0) tokens.add(sb.toString());
        return tokens;
    }

    List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        Map<String, Integer> precedence = Map.of(
                "+", 1, "-", 1,
                "*", 2, "/", 2
        );

        for (String token : tokens) {
            if (token.matches("[a-zA-Z0-9]+")) {
                output.add(token);
            } else {
                while (!stack.isEmpty() && precedence.getOrDefault(stack.peek(), 0) >= precedence.getOrDefault(token, 0)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }
        while (!stack.isEmpty()) output.add(stack.pop());
        return output;
    }

    List<String> generateCode(List<String> postfix, String resultVar) {
        List<String> code = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : postfix) {
            if (token.matches("[a-zA-Z0-9]+")) {
                stack.push(token);
            } else {
                String op2 = stack.pop();
                String op1 = stack.pop();
                String temp = "r" + tempCount++;
                code.add(temp + " = " + op1 + " " + token + " " + op2);
                stack.push(temp);
            }
        }
        code.add(resultVar + " = " + stack.pop());
        return code;
    }
}