package Target_Machine_Code;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TargetCodeBinaryGenerator {

    // Define a simple instruction class
    static class Instruction {
        byte opcode;
        byte operand1;
        byte operand2;

        Instruction(byte opcode, byte operand1, byte operand2) {
            this.opcode = opcode;
            this.operand1 = operand1;
            this.operand2 = operand2;
        }

        byte[] toBytes() {
            return new byte[]{opcode, operand1, operand2};
        }
    }

    // Sample opcode mapping
    static Map<String, Byte> opcodeMap = new HashMap<>();

    static {
        opcodeMap.put("LOAD", (byte) 0x01);
        opcodeMap.put("STORE", (byte) 0x02);
        opcodeMap.put("ADD", (byte) 0x03);
        opcodeMap.put("SUB", (byte) 0x04);
        opcodeMap.put("MUL", (byte) 0x05);
        opcodeMap.put("MOV", (byte) 0x06);
        // Add more opcodes as needed
    }

    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : "optimized.icr";
        List<String> icrLines;

        try {
            icrLines = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            System.err.println("ERROR: Cannot read input file '" + fileName + "'.");
            return;
        }

        List<Instruction> instructions = generateInstructions(icrLines);

        List<byte[]> binaryInstructions = new ArrayList<>();
        for (Instruction instr : instructions) {
            binaryInstructions.add(instr.toBytes());
        }

        // Print the instructions in 1's and 0's
        System.out.println("Generated Binary Instructions (in bits):");
        for (byte[] instr : binaryInstructions) {
            for (byte b : instr) {
                String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
                System.out.print(binaryString + " ");
            }
            System.out.println();
        }

        // Write to binary file
        try (OutputStream out = new FileOutputStream("output.bin")) {
            for (byte[] instr : binaryInstructions) {
                out.write(instr);
            }
            System.out.println("\nBinary written to output.bin");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to write binary output.");
        }
    }

    private static List<Instruction> generateInstructions(List<String> icrLines) {
        List<Instruction> instructions = new ArrayList<>();

        for (String line : icrLines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] parts = line.split("\\s+|,");
            if (parts.length < 2) continue;

            String op = parts[0].toUpperCase();
            byte opcode = opcodeMap.getOrDefault(op, (byte) 0x00);

            byte operand1 = parts.length > 1 ? parseOperand(parts[1]) : 0;
            byte operand2 = parts.length > 2 ? parseOperand(parts[2]) : 0;

            instructions.add(new Instruction(opcode, operand1, operand2));
        }

        return instructions;
    }

    private static byte parseOperand(String token) {
        token = token.trim();
        if (token.toUpperCase().startsWith("R")) {
            return Byte.parseByte(token.substring(1)); // R1 -> 1
        }
        try {
            return Byte.parseByte(token);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
