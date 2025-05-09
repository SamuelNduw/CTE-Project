```
import java.util.*;

public class ICRTest {
    private static final String[] RESERVED_KEYWORDS = {"BEGIN", "INPUT", "INTEGER", "WRITE", "END"};
    private static int tempCount = 1;
    public static void main(String[] args) {
        String[] input = {
                "LET G = a + c",
                "M = A/B+C",
                "N = G/H-I+a*B/c",
                "BEGIN something",
                "WRITE this down",
                "   END"
        };

        ICR testICR = new ICR();
        for (String line : input) {
            if (testICR.isReserved(line)) continue;

            String clean = line.replaceAll("(?i)LET", "").replaceAll("\\s+", "");
            if (!clean.contains("=")) continue;

            String[] parts = clean.split("=");
            String lhs = parts[0];
            String rhs = parts[1];

            List<String> postfix = testICR.infixToPostfix(testICR.tokenize(rhs));
            List<String> code = testICR.generateCode(postfix, lhs);

            for (String c : code) System.out.println(c);
        }
    }
}

```