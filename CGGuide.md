```
import java.util.*;

public class CGTest {
    public static void main(String[] args) {
        List<String> tac = List.of(
                "r1 = a + c",
                "G = r1",
                "r2 = A / B",
                "r3 = r2 + C",
                "M = r3",
                "r4 = G / H",
                "r5 = r4 - I",
                "r6 = a * B",
                "r7 = r6 / c",
                "r8 = r5 + r7",
                "N = r8"
        );

        CG testCG = new CG();
        List<String> assembly = testCG.generateAssembly(tac);
        for (String line : assembly) {
            System.out.println(line);
        }
    }
}

```