import java.util.*;

public class CodeOptimisation {

    List<String> convertToThreeAddress(List<String> oneAddressCode) {
        List<String> threeAddressCode = new ArrayList<>();

        for (int i = 0; i < oneAddressCode.size() - 2; i++) {
            String line1 = oneAddressCode.get(i).trim();
            String line2 = oneAddressCode.get(i + 1).trim();
            String line3 = oneAddressCode.get(i + 2).trim();

            if (line1.startsWith("LDA") && line3.startsWith("STR")) {
                String x = line1.split(" ")[1];
                String z = line3.split(" ")[1];

                if (line2.matches("(ADD|SUB|MUL|DIV)\\s+\\w+")) {
                    String[] opParts = line2.split(" ");
                    String op = opParts[0];
                    String y = opParts[1];

                    String tacOp = switch (op) {
                        case "ADD" -> "ADD";
                        case "SUB" -> "SUB";
                        case "MUL" -> "MUL";
                        case "DIV" -> "DIV";
                        default -> throw new IllegalArgumentException("Invalid operator: " + op);
                    };

                    threeAddressCode.add(tacOp + " " + z + ", " + x + ", " + y);
                    i += 2; // skip processed lines
                } else if (line2.equals("STR " + z)) {
                    threeAddressCode.add("MOV " + z + ", " + x);
                    i += 1;
                }                
            }
        }

        return threeAddressCode;
    }
}
