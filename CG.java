import java.util.*;

public class CG {

    List<String> generateAssembly(List<String> tacLines) {
        List<String> asm = new ArrayList<>();

        for (String line : tacLines) {
            // Format: r1 = a + b
            line = line.replace(" ", "");
            String[] parts = line.split("=");

            if (parts.length != 2) continue;

            String target = parts[0]; // r1
            String expr = parts[1];   // a+b

            // Handle direct assignment: a = b
            if (!expr.contains("+") && !expr.contains("-") &&
                    !expr.contains("*") && !expr.contains("/")) {
                asm.add("LDA " + expr);
                asm.add("STR " + target);
                continue;
            }

            // Extract operands and operator
            String op1 = "", op2 = "", operator = "";
            for (char ch : new char[]{'+', '-', '*', '/'}) {
                if (expr.indexOf(ch) != -1) {
                    operator = Character.toString(ch);
                    String[] ops = expr.split("\\" + ch);
                    op1 = ops[0];
                    op2 = ops[1];
                    break;
                }
            }

            if (op1.isEmpty() || op2.isEmpty()) continue;

            asm.add("LDA " + op1);
            switch (operator) {
                case "+": asm.add("ADD " + op2); break;
                case "-": asm.add("SUB " + op2); break;
                case "*": asm.add("MUL " + op2); break;
                case "/": asm.add("DIV " + op2); break;
            }
            asm.add("STR " + target);
        }

        return asm;
    }

}
