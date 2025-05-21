
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] vlang = {
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

        String[] vlang2 = {
                "LET G = a + c",
                "M = A/B+C",
                "N = G/H-I+a*B/c",
        };

        System.out.println("--- LEXICAL ANALYSIS ---");
        // Lexical Analysis
        LexicalAnalyzer lexer = new LexicalAnalyzer();
        for (String line : vlang) {
            System.out.println("Line: " + line);
            List<LexicalAnalyzer.Token> toks = lexer.analyze(line);
            toks.forEach(t -> System.out.println("  " + t));
            System.out.println();
        }

        // Syntax Analysis
        SyntaxAnalysis analyser = new SyntaxAnalysis();
        for (String line : vlang) {
            System.out.println("\nGET A DERIVATION FOR: " + line);
            analyser.derive(line);
        }

        // Semantic Analysis
        SemanticAnalyser semanticAnalyser = new SemanticAnalyser();
        System.out.println("\n--- SEMANTIC ANALYSIS ---");
        for (String line : vlang) {
            boolean result = semanticAnalyser.analyzeLine(line);
            if (!result) {
                System.err.println("Semantic error found in line: " + line);
            } else {
                System.out.println("Semantic OK: " + line);
            }
        }
        System.out.println("\nDeclared Identifiers: " + semanticAnalyser.getDeclaredIdentifiers());

        // Intermediate Code Representation
        System.out.println("--- INTERMEDIATE CODE REPRESENTATION ---");
        List<String> myList = new ArrayList<>();
        ICR icr = new ICR();
        for (String line : vlang2) {
            if (icr.isReserved(line)) continue;

            String clean = line.replaceAll("(?i)LET", "").replaceAll("\\s+", "");
            if (!clean.contains("=")) continue;

            String[] parts = clean.split("=");
            String lhs = parts[0];
            String rhs = parts[1];

            List<String> postfix = icr.infixToPostfix(icr.tokenize(rhs));
            List<String> code = icr.generateCode(postfix, lhs);

            for (String c : code) {
                System.out.println(c);
                myList.add(c);
            }

        }

        // Code Generation
        System.out.println("--- CODE GENERATION ---");
        CodeGeneration testCG = new CodeGeneration();
        List<String> assembly = testCG.generateAssembly(myList);
        for (String line : assembly) {
            System.out.println(line);
        }

        // Code Optimisation
        System.out.println("--- CODE OPTIMISATION ---");
        CodeOptimisation codeOptimisation = new CodeOptimisation();
        List<String> threeAddress = codeOptimisation.convertToThreeAddress(assembly);
        for (String line : threeAddress) {
            System.out.println(line);
        }
    }
}