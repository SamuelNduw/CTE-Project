```
import java.util.List;

public class CodeOptimisationTest {
    public static void main(String[] args) {
        List<String> oneAddress = List.of(
                "LDA c",
                "MUL d",
                "STR r1",
                "LDA b",
                "MUL r1",
                "STR r2",
                "LDA r2",
                "STR a"
        );

        CodeOptimisation odeOptimisationTest = new OneToThreeAddress();
        List<String> threeAddress = codeOptimisationTest.convertToThreeAddress(oneAddress);
        for (String line : threeAddress) {
            System.out.println(line);
        }
    }
}
```