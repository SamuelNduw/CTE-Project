# 🎯 Target Machine Code Generator (Java)

This module is responsible for converting **Intermediate Code Representation (ICR)** into **binary instructions** executable by a custom virtual machine. It is the **7th and final stage** in our compiler pipeline.

---

## 📁 Folder Structure

compiler_project/
├── lexer/
├── parser/
├── semantic/
├── icr_generator/
├── Target_Machine_Code/
│ └── TargetCodeBinaryGenerator.java
├── vm/
│ └── SimpleVirtualMachine.java



---

## 📌 What This Module Does

- Accepts a list of ICR strings (e.g., `"t1 = a + b"`)
- Maps each variable to a memory address
- Generates binary instructions using a simple instruction set (LOAD, ADD, STORE, etc.)
- Saves the instructions as a `.bin` file for the virtual machine to execute

---

## 🧪 How to Test

### 1. Compile and Run Manually

Open a terminal or command prompt, navigate to the project folder, and run:

```bash
javac Target_Machine_Code/TargetCodeBinaryGenerator.java
java Target_Machine_Code.TargetCodeBinaryGenerator
This runs a sample main() method that:

Defines 3 ICR instructions

Maps variable names to memory addresses

Generates output.bin

2. Run the Virtual Machine
Assuming SimpleVirtualMachine.java is implemented, you can execute the binary:

bash
Copy
Edit
javac vm/SimpleVirtualMachine.java
java vm.SimpleVirtualMachine output.bin
You should see memory output after executing the binary.

🧾 Input Format
Each ICR line must follow this format:

ini
Copy
Edit
target = left_operand operator right_operand
✅ Valid:

ini
Copy
Edit
t1 = a + b
t2 = t1 * c
t3 = t2 - d
❌ Invalid:

makefile
Copy
Edit
a + b = t1        # Reversed
t1 = a            # Incomplete
🧠 Memory Mapping Guidelines
Every variable or temporary must have a unique address.

Recommended ranges:

Variables: a, b, c, etc. → 0x01 to 0x0F

Temporaries: t1, t2, etc. → 0x10 and above

java
Copy
Edit
Map<String, Byte> memoryMap = Map.of(
  "a", (byte) 0x01,
  "b", (byte) 0x02,
  "c", (byte) 0x03,
  "d", (byte) 0x04,
  "t1", (byte) 0x10,
  "t2", (byte) 0x11,
  "t3", (byte) 0x12
);
💾 Binary Output Format
Each instruction is 3 bytes:

css
Copy
Edit
[opcode][operand][padding]
Example output for t1 = a + b:

bash
Copy
Edit
0x01 0x01 0x00   # LOAD a
0x02 0x02 0x00   # ADD b
0x06 0x10 0x00   # STORE t1
🧰 Instruction Set
Operation	Opcode
LOAD	0x01
ADD	0x02
SUB	0x03
MUL	0x04
DIV	0x05
STORE	0x06

Each ICR line produces three machine instructions:

LOAD left_operand

OPCODE right_operand

STORE target

🔗 Integration With Other Stages
After the ICR stage, call:

java
Copy
Edit
TargetCodeBinaryGenerator.compileICRToBinary(
    List<String> icrLines,
    Map<String, Byte> memoryMapping,
    "output.bin"
);
This can be placed at the end of your pipeline to generate the final machine-executable binary.

❌ Error Handling
This module will throw exceptions if:

The ICR line is not in the correct format

The operator is unsupported (+, -, *, / only)

A variable is not present in the memory map

Be sure earlier stages validate and sanitize input before reaching this stage.

🧱 Example Usage (Inside main())
java
Copy
Edit
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

TargetCodeBinaryGenerator.compileICRToBinary(icr, memoryMap, "output.bin");
🚀 Future Enhancements
Add support for conditional instructions and jumps

Improve error reporting with detailed line numbers

Add CLI support for compiling .icr text files directly

Add assembler-style output (text + binary)

