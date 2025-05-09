`import java.util.Arrays;`

```
String[] input = {
    "LET G = a + c",
    "M = A/B+C",
    "N = G/H-I+a*B/c",
    "BEGIN something",
    "WRITE this down",
    "   END"
};
```

```
ICR testICR = new ICR();
char[][] output = ICR.convertStringsToCharArrays(input);
for (int i = 0; i < output.length; i++) {
    System.out.print("Array " + (i + 1) + ": ");
    if (output[i] != null) {
        System.out.println(Arrays.toString(output[i]));
    } else {
        System.out.println("null");
    }
}
```