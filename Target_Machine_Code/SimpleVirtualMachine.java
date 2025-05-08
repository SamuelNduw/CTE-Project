package Target_Machine_Code;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SimpleVirtualMachine {
    static final int MEM_SIZE = 256;
    static byte[] memory = new byte[MEM_SIZE];
    static byte accumulator = 0;

    // Map memory addresses to variable names
    static final Map<Byte, String> addressToVar = new HashMap<>();

    static {
        // Initialize input variable values
        memory[0x01] = 5; // a
        memory[0x02] = 3; // b
        memory[0x03] = 2; // c
        memory[0x04] = 4; // d

        // Map memory addresses to variable names
        addressToVar.put((byte) 0x01, "a");
        addressToVar.put((byte) 0x02, "b");
        addressToVar.put((byte) 0x03, "c");
        addressToVar.put((byte) 0x04, "d");
        addressToVar.put((byte) 0x10, "t1");
        addressToVar.put((byte) 0x11, "t2");
        addressToVar.put((byte) 0x12, "t3");
    }

    public static void main(String[] args) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream("output.bin"))) {
            while (dis.available() > 0) {
                byte opcode = dis.readByte();
                byte operand = dis.readByte();
                dis.readByte(); // skip padding byte

                switch (opcode) {
                    case 0x01 -> accumulator = memory[operand];         // LOAD
                    case 0x02 -> accumulator += memory[operand];        // ADD
                    case 0x03 -> accumulator -= memory[operand];        // SUB
                    case 0x04 -> accumulator *= memory[operand];        // MUL
                    case 0x05 -> accumulator /= memory[operand];        // DIV
                    case 0x06 -> memory[operand] = accumulator;         // STORE
                    default -> System.out.println("Unknown opcode: " + opcode);
                }
            }

            System.out.println("\n=== Final Variable Values ===");
            for (Map.Entry<Byte, String> entry : addressToVar.entrySet()) {
                byte addr = entry.getKey();
                String var = entry.getValue();
                byte val = memory[addr];
                if (val != 0) {
                    System.out.printf("%s = %d%n", var, val);
                }
            }

            System.out.println("\n=== Raw Memory Dump (non-zero values) ===");
            for (int i = 0; i < memory.length; i++) {
                if (memory[i] != 0) {
                    System.out.printf("mem[%02X] = %d%n", i, memory[i]);
                }
            }

        } catch (IOException e) {
            System.err.println("Error running VM: " + e.getMessage());
        }
    }
}
