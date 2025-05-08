package Target_Machine_Code;

import java.io.*;
import java.util.*;

/**
 * This class translates ICR (Intermediate Code Representation) into binary instructions
 * understood by a custom virtual machine.
 */
public class TargetCodeBinaryGenerator {

    // Instruction structure
    private record Instruction(byte opcode, byte operand, byte padding) {}

    // Opcode definitions
    private static final byte LOAD = 0x01;
    private static final byte ADD = 0x02;
    private static final byte SUB = 0x03;
    private static final byte MUL = 0x04;
    private static final byte DIV = 0x05;
    private static final byte STORE = 0x06;

    /**
     * Compile a list of ICR statements into a binary file.
     *
     * @param icrLines       List of ICR strings (e.g., "t1 = a + b")
     * @param memoryMapping  Map of variable names to memory addresses (e.g., a = 0x01)
     * @param outputFile     Output path for binary file (e.g., "output.bin")
     */
    public static void compileICRToBinary(List<String> icrLines, Map<String, Byte> memoryMapping, String outputFile) {
        List<Instruction> instructions = new ArrayList<>();

        for (String line : icrLines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Parse ICR line: target = left OP right
            String[] parts = line.split("=");
            if (parts.length != 2) throw new IllegalArgumentException("Invalid ICR line: " + line);

            String target = parts[0].trim();         // e.g., t1
            String expr = parts[1].trim();           // e.g., a + b

            // Split expression into left operand, operator, right operand
            String[] exprParts = expr.split(" ");
            if (exprParts.length != 3) throw new IllegalArgumentException("Invalid expression in ICR: " + expr);

            String left = exprParts[0];
            String op = exprParts[1];
            String right = exprParts[2];

            byte opCode = switch (op) {
                case "+" -> ADD;
                case "-" -> SUB;
                case "*" -> MUL;
                case "/" -> DIV;
                default -> throw new IllegalArgumentException("Unsupported operator: " + op);
            };

            // Build machine instructions: LOAD left → [OP] right → STORE target
            instructions.add(new Instruction(LOAD, memoryMapping.get(left), (byte) 0));
            instructions.add(new Instruction(opCode, memoryMapping.get(right), (byte) 0));
            instructions.add(new Instruction(STORE, memoryMapping.get(target), (byte) 0));
        }

        // Write instructions to binary file
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
            for (Instruction inst : instructions) {
                dos.writeByte(inst.opcode());
                dos.writeByte(inst.operand());
                dos.writeByte(inst.padding());  // keep uniform 3-byte format
            }
            System.out.println("✅ Binary compiled and saved to: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Optional: For standalone testing (e.g., java TargetCodeBinaryGenerator)
    public static void main(String[] args) {
        List<String> icr = List.of(
            "t1 = a + b",
            "t2 = t1 * c",
            "t3 = t2 - d"
        );

        Map<String, Byte> memoryMap = new HashMap<>();
        memoryMap.put("a", (byte) 0x01);
        memoryMap.put("b", (byte) 0x02);
        memoryMap.put("c", (byte) 0x03);
        memoryMap.put("d", (byte) 0x04);
        memoryMap.put("t1", (byte) 0x10);
        memoryMap.put("t2", (byte) 0x11);
        memoryMap.put("t3", (byte) 0x12);

        compileICRToBinary(icr, memoryMap, "output.bin");
    }
}
