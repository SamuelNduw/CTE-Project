
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> vlang = new ArrayList<>();
        vlang.add("BEGIN");
        vlang.add("INTEGER A, B, C, E, M, N, G, H, I, a, c");
        vlang.add("INPUT A, B, C");
        vlang.add("LET B = A */ M");
        vlang.add("LET G = a + c");
        vlang.add("temp = <s%**h - j / w +d +*$&;");
        vlang.add("M = A/B+C");
        vlang.add("N = G/H-I+a*B/c");
        vlang.add("WRITE M");
        vlang.add("WRITEE F;");
        vlang.add("END");

        Set<Integer> setOfInvalid = new HashSet<>();

        // Lexical Analysis
        System.out.println("--- LEXICAL ANALYSIS ---");
        LexicalAnalyzer lexer = new LexicalAnalyzer();
        for (int i = 0; i < vlang.size(); i++) {
            System.out.println("Line: " + vlang.get(i));
            List<LexicalAnalyzer.Token> toks = lexer.analyze(vlang.get(i));
            int finalI = i;
            toks.forEach(t -> {
                if(t.type.equals(LexicalAnalyzer.TokenType.LEXICAL_ERROR)){
                    setOfInvalid.add(finalI);
                    System.out.println("  " + t);
                }else{
                    System.out.println("  " + t);
                }
            });
            System.out.println();
        }

        // Removes lines that have errors
        List<String> cleaned = new ArrayList<>();
        for (int i = 0; i < vlang.size(); i++) {
            if (!setOfInvalid.contains(i)) {
                cleaned.add(vlang.get(i));
            } else {
                System.err.println("LEXICAL ERROR: " + vlang.get(i));
            }
        }
        vlang = new ArrayList<>(cleaned);

        setOfInvalid.clear();

        // Syntax Analysis
        System.out.println("--- SYNTAX ANALYSIS ---");
        SyntaxAnalysis analyser = new SyntaxAnalysis();
        for (int i = 0; i < vlang.size(); i++) {
            ArrayList<String> stepsList = analyser.derive(vlang.get(i));
            if (stepsList.size() == 1){
                char firstChar = stepsList.get(0).charAt(0);
                if(firstChar == ' '){
                    System.out.println("No derivation");
                    System.out.println();
                    continue;
                }
                System.out.println("Error in derivation");
                System.err.println(stepsList.get(0));
                setOfInvalid.add(i);
            }else{
                for(String strValue : stepsList){
                    System.out.println(strValue);
                }
            }
            System.out.println();
        }

        // Removes lines that have errors
        List<String> cleaned2 = new ArrayList<>();
        for (int i = 0; i < vlang.size(); i++) {
            if (!setOfInvalid.contains(i)) {
                cleaned2.add(vlang.get(i));
            }
        }
        vlang = new ArrayList<>(cleaned2);

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
        System.out.println();

        // Removes lines that have no assignment operator
        List<String> cleaned3 = new ArrayList<>();
        for (int i = 0; i < vlang.size(); i++) {
            int equalIndex = vlang.get(i).indexOf('=');
            if(equalIndex != -1){
                cleaned3.add(vlang.get(i));
            }
        }
        vlang = new ArrayList<>(cleaned3);

        // Intermediate Code Representation
//        System.out.println("--- INTERMEDIATE CODE REPRESENTATION ---");
        List<String> myList = new ArrayList<>();
        ICR icr = new ICR();
        for (String line : vlang) {
            if (icr.isReserved(line)) continue;

            String clean = line.replaceAll("(?i)LET", "").replaceAll("\\s+", "");
            if (!clean.contains("=")) continue;

            String[] parts = clean.split("=");
            String lhs = parts[0];
            String rhs = parts[1];

            List<String> postfix = icr.infixToPostfix(icr.tokenize(rhs));
            List<String> code = icr.generateCode(postfix, lhs);
            System.out.println();
            System.out.println("--- INTERMEDIATE CODE REPRESENTATION ---");
            for (String c : code) {
                System.out.println(c);
                myList.add(c);
            }

        }
        System.out.println();

        // Code Generation
        System.out.println("--- CODE GENERATION ---");
        CodeGeneration testCG = new CodeGeneration();
        List<String> assembly = testCG.generateAssembly(myList);
        for (String line : assembly) {
            System.out.println(line);
        }
        System.out.println();

        // Code Optimisation
        System.out.println("--- CODE OPTIMISATION ---");
        CodeOptimisation codeOptimisation = new CodeOptimisation();
        List<String> threeAddress = codeOptimisation.convertToThreeAddress(assembly);
        for (String line : threeAddress) {
            System.out.println(line);
        }
        System.out.println();

        // Target Machine Code
        System.out.println("--- TARGET MACHINE CODE ---");
        TargetMachineCode tmc = new TargetMachineCode();
        ArrayList<String> binaryCodeList = tmc.binaryConversion(threeAddress);
        for(String binaryString : binaryCodeList){
            System.out.println(binaryString);
        }
    }
}