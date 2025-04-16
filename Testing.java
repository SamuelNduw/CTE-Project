import java.util.StringTokenizer;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class Testing{

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        String alphabetRegex = "^[A-Za-z]+$";
        String operatorRegex = "[-+*/]";
        String digitRegex = "[.*\\d.*]";
        Pattern operatorPattern = Pattern.compile(operatorRegex);
        String validCharRegex = "^[A-Za-z\\+\\/\\-\\*]+$";
        Pattern validCharPattern = Pattern.compile(validCharRegex);
        Pattern alphabetPattern = Pattern.compile(alphabetRegex);
        Pattern digitPattern = Pattern.compile(digitRegex);


        System.out.println("Enter String: ");
        String inputString = sc.nextLine();

        // Exit program if user inputs 99
        if(inputString.equals("99")){
            return;
        };

        // Tokenize and store characters in an array
        String[] arr = inputString.split(" ");

        // Check for digits
        for(String x: arr){
            Matcher matcher = digitPattern.matcher(x);
            try{
                if(matcher.matches()){
                    throw new SyntaxErrorException(inputString);
                }
            } catch(SyntaxErrorException e){
                System.err.println(e.getUserMessage());
                System.err.println(e.getDigitsPresentMessage());
                e.printStackTrace();
                return;
            }
        }

        // Check for special characters
        for(String x : arr){
            Matcher matcher = validCharPattern.matcher(x);
            try{
                if(!matcher.matches()){
                    throw new SemanticErrorException(inputString);
                }
            } catch(SemanticErrorException e){
                System.err.println(e.getUserMessage());
                System.err.println(e.getSpecialCharactersUsed());
                e.printStackTrace();
                return;
            }
        }


        // Print output of tokens if all characters are valid
        System.out.println("Output =");

        boolean isOperator = false;
        int sum = 0;
        boolean consecutiveIdentifiers = false;
        for(int i = 0; i < arr.length; i++){
            Matcher matcher = alphabetPattern.matcher(arr[i]);
            if(arr[i].length() > 1){
                consecutiveIdentifiers = true;
            }
            int j = i+1;
            if(matcher.matches()){
                System.out.println("TOKEN#" + j + " " + arr[i] + " Identifier");
            }else{
                System.out.println("TOKEN#" + j + " " + arr[i] + " Operator");
                isOperator = true;
            }
            sum += 1;
        }
        System.out.println("TOTAL NUMBER OF TOKENS: " + sum);

        // Check for absence of no operators in the string
        try{
            if(!isOperator){
                throw new SemanticErrorException(inputString);
            }
        } catch(SemanticErrorException e){
            System.err.println(e.getUserMessage());
            System.err.println(e.getNoOperator());
            e.printStackTrace();
        }

        // Check for identifiers written together
        try{
            if(consecutiveIdentifiers){
                throw new SemanticErrorException(inputString);
            }
        }catch(SemanticErrorException e){
            System.err.println(e.getUserMessage());
            System.err.println(e.getConsecutiveCharactersMessage());
            e.printStackTrace();
        }
        sc.close();
    }
}

class SyntaxErrorException extends RuntimeException{
    private final String userMessage = "SYNTAX ERROR!";
    private final String passedString;
    private final String digitsPresentMessage;

    public SyntaxErrorException(String passedString){
        this.passedString = passedString;
        this.digitsPresentMessage = setDigitsPresentMessage(passedString);
    }

    private String setDigitsPresentMessage(String passedString){
        return "Numbers 0,1 to 9 are not allowed. String should contain A to Z and a to z & operators +,-,*,/ \nCONCLUSION-->Wrong expression: "+ passedString +"  No Derivation done! PLS RE-ENTER A VALID STRING";
    }
    String getUserMessage(){
        return userMessage;
    }
    String getDigitsPresentMessage(){
        return digitsPresentMessage;
    }

}

// Custom Exception for Semantic Errors
class SemanticErrorException extends RuntimeException{
    private final String userMessage = "Semantic Error!";
    private final String passedString;
    private final String specialCharactersUsed = "Use of Special Characters ie &, &&, $, %, !, , etc, not permitted";
    private final String noOperator;
    private final String consecutiveCharactersMessage = "Two operators (*,-,+,/) or Identifier (A to Z and a to z) cannot be written together!";

    public SemanticErrorException(String passedString){
        this.passedString = passedString;
        this.noOperator = setNoOperator(passedString);
    }

    public String getUserMessage(){
        return userMessage;
    }
    private String setNoOperator(String passedString){
        return "Invalid String! There is no operator in the String ( +, /, -, *,)CONCLUSION-->Wrong expression: " + passedString  + "\nNo Derivation done! PLS RE-ENTER A VALID STRING";
    }
    public String getNoOperator(){
        return noOperator;
    }

    public String getSpecialCharactersUsed(){
        return specialCharactersUsed;
    }
    public String getConsecutiveCharactersMessage(){
        return consecutiveCharactersMessage;
    }
}