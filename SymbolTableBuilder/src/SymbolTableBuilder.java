import java.io.*;
import java.util.*;
import java.util.regex.*;
import static java.lang.System.out;

public class SymbolTableBuilder {
    private static final Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else", "enum",
            "extends", "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new", "null",
            "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"));

    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b");

    public static void main(String[] args) {
        if (args.length != 1) {
            out.println("Usage: java SymbolTableBuilder <path-to-java-source-file>");
            return;
        }// end if

        String fileName = args[0];

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            Map<String, List<Integer>> symbolTable = new LinkedHashMap<>();
            Set<String> foundReservedWords = new HashSet<>();
            int lineNumber = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                Matcher matcher = IDENTIFIER_PATTERN.matcher(line);
                while (matcher.find()) {
                    String identifier = matcher.group(1);

                    if (RESERVED_WORDS.contains(identifier)) {
                        foundReservedWords.add(identifier); // Track reserved words found in the code
                    } else {
                        symbolTable.putIfAbsent(identifier, new ArrayList<>());
                        symbolTable.get(identifier).add(lineNumber);
                    }// end if else
                }// end while nested
            }// end while

            out.println("*** SYMBOL TABLE ***");
            int identifierCount = 0;
            for (Map.Entry<String, List<Integer>> entry : symbolTable.entrySet()) {
                out.print(entry.getKey() + " ");
                List<Integer> lineNumbers = entry.getValue();
                out.println(lineNumbers);
                identifierCount++;
            }// end for

            out.println("*** SUMMARY ***");
            out.println("Total Number of Identifiers (No reserved words): " + identifierCount);
            out.println("Total Number of Java Reserved Words: " + foundReservedWords.size());

        } catch (IOException e) {
            out.println("Error reading file: " + e.getMessage());
        }// end try catch
    }// end main
}// end class