import java.io.*;
import java.util.Scanner;
import java.util.regex.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        File inputFile = new File(scanner.next());
        FileReader inputFR = null;
        try {
            inputFR = new FileReader(inputFile);
        } catch (FileNotFoundException exc) {
            System.out.println("Bad file!");
            System.exit(1);
        }
        Scanner input = new Scanner(inputFR);

        File outputFile = new File("output.tex");
        FileWriter outputFW = null;
        try {
            outputFW = new FileWriter(outputFile);
        } catch (IOException exc) {
            System.out.println("Cannot create output file!");
            System.exit(2);
        }

        outputFW.append("\\documentclass{article}\n" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\\usepackage[russian]{babel}\n" +
                "\n" +
                "\\begin{document}\n" +
                "\\begin{tabular}{ | l | l | l |} \\hline\n");

        Pattern namePattern = Pattern.compile("[\\s\\w^]+(?=,)", Pattern.UNICODE_CHARACTER_CLASS);
        Pattern yearPattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
        Matcher nameMatcher = namePattern.matcher("");
        Matcher yearMatcher = yearPattern.matcher("");
        StringBuilder partOfInput = new StringBuilder();
        StringBuilder partOfOutput = new StringBuilder();

        while (input.hasNext()) {
            int numberOfLines = 0;
            for (int i = 0; i < 256 && input.hasNext(); i++) {
                partOfInput.append(input.nextLine()).append("\n");
                numberOfLines++;
            }

            nameMatcher.reset(partOfInput);
            yearMatcher.reset(partOfInput);

            for (int lineNumber = 0; lineNumber < numberOfLines; lineNumber++) {
                for (int i = 0; i < 2; i++) {
                    if (nameMatcher.find()) {
                        partOfOutput.append(nameMatcher.group()).append(" & ");
                    }
                }
                if (yearMatcher.find()) {
                    partOfOutput.append(yearMatcher.group());
                }
                partOfOutput.append(" \\\\ \\hline\n");
            }
            outputFW.append(partOfOutput);
            outputFW.flush();
            partOfOutput.delete(0, partOfOutput.length());
            partOfInput.delete(0, partOfInput.length());
        }
        outputFW.append("\\end{tabular}\n" +
                "\\end{document}");
        outputFW.flush();
    }
}
