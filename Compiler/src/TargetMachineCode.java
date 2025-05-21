import java.util.ArrayList;
import java.util.List;

public class TargetMachineCode {
    ArrayList<String> binaryStringList = new ArrayList<>();
    ArrayList<String> binaryConversion(List<String> threeAddressList){
        for(String codeOpString: threeAddressList){
            StringBuilder sb = new StringBuilder();
            String[] arrOfStr = codeOpString.split(" ");
            for(String str: arrOfStr){
                char leadChar = str.charAt(0);
                int asciiValue = (int) leadChar;
                String binStr = Integer.toBinaryString(asciiValue);
                String formattedBinary = String.format("%8s", binStr).replace(' ', '0');
                sb.append(formattedBinary).append(" ");
            }
            binaryStringList.add(sb.toString());
        }
        return binaryStringList;
    }
}
