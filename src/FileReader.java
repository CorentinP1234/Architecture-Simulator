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
        String fileToExecute = null;

        File project = new File(".");
        File[] listOfFiles = project.listFiles();
        if (listOfFiles == null) {
            System.out.println("Error no file found");
            System.exit(1);
        }
        int fileNumber;

        ArrayList<String> assemblyFileName = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Which file do you want to run ?");

            int counter = 1;
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.substring((fileName.length() - 3)).equals("txt")){

                        assemblyFileName.add(fileName);
                        System.out.print(counter++ + ". ");
                        System.out.println(fileName);
                    }
                }
            }
            System.out.print("\nEnter a number: ");
            fileNumber = Integer.parseInt(scanner.nextLine());
            if (fileNumber > 0 && fileNumber < (assemblyFileName.size() + 1)){
                isInputValid = true;
                fileToExecute = assemblyFileName.get(fileNumber - 1);
            }
        } while(!isInputValid);
        System.out.println();
        return fileToExecute;
    }
    public static ArrayList<String> getDataSection(ArrayList<String> lines) {
        ArrayList<String> dataSection = new ArrayList<>();
        for (String line : lines) {
            if (Objects.equals(line, "#DATA")) {
                continue;
            }
            if (Objects.equals(line, "#CODE"))
                break;
            dataSection.add(line);
        }
        return dataSection;
    }
    public static ArrayList<String> getCodeSection(ArrayList<String> lines) {
        ArrayList<String> codeSection = new ArrayList<>();
        boolean isCodeSection = false;
        for (String line : lines) {
            if (Objects.equals(line, "#CODE")) {
                isCodeSection = true;
                continue;
            }
            if (isCodeSection)
                codeSection.add(line);
        }
        return codeSection;
    }
}
