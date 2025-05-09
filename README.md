public class CompilerMain {
    public static void main(String[] args) {
        String[] program = {
            "BEGIN",
            "INTEGER A, B, C, E, M, N, G, H, I, a, c",
            "INPUT A, B, C",
            "LET B = A */ M",
            "LET G = a + c",      // line 5 ✅
            "temp = <s%**h - j / w +d +*$&;",  // invalid
            "M = A/B+C",          // line 7 ✅
            "N = G/H-I+a*B/c",    // line 8 ✅
            "WRITE M",
            "WRITEE F;",          // Lexical error
            "END"
        };

        SemanticAnalyzer analyzer = new SemanticAnalyzer();

        // First pass: Register declarations
        for (String line : program) {
            if (line.toUpperCase().startsWith("INTEGER") || line.toUpperCase().startsWith("INPUT")) {
                analyzer.analyzeLine(line);
            }
        }

        // Second pass: Analyze and handle lines
        for (int i = 0; i < program.length; i++) {
            String line = program[i].trim();

            // Only lines 5, 7, and 8 go through full compiler
            if (i == 4 || i == 6 || i == 7) {
                boolean semOK = analyzer.analyzeLine(line);
                if (semOK) {
                    System.out.println("Line " + (i + 1) + ": Passed Semantic Check, proceed to Code Generation.");
                    // → Continue with Lexical, Syntax, Semantic, ICR, CG, CO, TMC
                }
            } else {
                boolean semOK = analyzer.analyzeLine(line);
                if (!semOK) {
                    System.out.println("Line " + (i + 1) + ": Semantic check failed.");
                } else {
                    System.out.println("Line " + (i + 1) + ": OK (No further stages needed).");
                }
            }
        }
    }
}
