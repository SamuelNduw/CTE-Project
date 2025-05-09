public class ICR {

    final String[] RESERVED_KEYWORDS = {"BEGIN", "INPUT", "INTEGER", "WRITE", "END"};

    char[][] convertStringsToCharArrays(String[] inputStrings) {
        char[][] result = new char[inputStrings.length][];
        for (int i = 0; i < inputStrings.length; i++) {
            result[i] = processString(inputStrings[i]);
        }
        return result;
    }

    char[] processString(String input) {
        if (input == null) return null;

        String trimmed = input.trim();

        for (String keyword : RESERVED_KEYWORDS) {
            if (trimmed.toUpperCase().startsWith(keyword)) {
                return null;
            }
        }

        String cleaned = trimmed.replaceAll("(?i)LET", "").replaceAll("\\s+", "");
        return cleaned.toCharArray();
    }
}
