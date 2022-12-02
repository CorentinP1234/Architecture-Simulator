import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class FileReader {
    public static ArrayList<String> readFile(String fileName) throws FileNotFoundException {

        // Stores each line in an ArrayList "lines"
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        ArrayList<String> lines = new ArrayList<>();
        while(scanner.hasNextLine()) {
            String newLine = scanner.nextLine();
            // Remove empty lines
            if (!Objects.equals(newLine, ""))
                lines.add(newLine);
        }

        return lines;
    }

    public static ArrayList<String> removeComments(ArrayList<String> codeLines_withComments){

        // Modify the ArrayList argument codeLines_withComments by removing each line beginning with '!'
        int number_of_lines = codeLines_withComments.size();
        ArrayList<String> codeLines = new ArrayList<>(number_of_lines);
        for (String line : codeLines_withComments) {
            if (line.charAt(0) != '!')
                codeLines.add(line);
        }

        return codeLines;
    }

    public static ArrayList<String> readFile_withoutComments(String fileName) throws FileNotFoundException {
        return removeComments(readFile(fileName));
    }
    public static String fileSelector() {
        boolean isInputValid = false;
        String fileName = null;

        do {
            System.out.println("Which file do you want to run ?");
            File project = new File(".");
            File[] listOfFiles = project.listFiles();
            ArrayList<String> assemblyFileName = new ArrayList<>();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fileName = file.getName();
                    if (fileName.substring((fileName.length() - 3)).equals("txt")){
                        String formattedFileName = fileName.substring(0, fileName.length() - 4);
                        assemblyFileName.add(formattedFileName);
                        System.out.println(formattedFileName);
                    }
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.print("\n> ");
            String inputFile = scanner.nextLine();
            if (assemblyFileName.contains(inputFile))
                isInputValid = true;
        } while(!isInputValid);
        return fileName;
    }
}
