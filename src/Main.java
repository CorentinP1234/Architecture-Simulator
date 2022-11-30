import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Questions:
        // Est ce que les registres sont signés ou non signés ?

        // Read file
        ArrayList<String> codeLines_withComments = readFile("first_file.txt");
        ArrayList<String> codeLines = removeComments(codeLines_withComments);

        // Init Stack
        Stack stack = new Stack();

        // Init Memory for variable
        Memory memory = new Memory();

        // Init ALU
        ALU alu = new ALU();
        alu.memory = memory;
        alu.stack = stack;

        // Registers
        Register t0 = alu.t0;
        Register t1 = alu.t1;
        Register t2 = alu.t2;
        Register t3 = alu.t3;

        // Program Counter
        int PC = 0;

        // Start the execution
        // startExecution(codeLines, alu, PC, memory, stack);

        // TESTING
        // TEST_memory();
        // TEST_twosComplement();
        // TEST_binaryAddition();
        // TEST_binarySubtraction();
    }


    public static ArrayList<String> readFile(String fileName) throws FileNotFoundException {

        // Stores each line in an ArrayList "lines"
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        ArrayList<String> lines = new ArrayList<>();
        while(scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
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

    public static void startExecution(ArrayList<String> codeLines, ALU alu, int PC, Memory memory, Stack stack) {
        // Algo temporaire
        boolean lines_remaining = true;
        boolean DATA_SECTION = false;
        boolean CODE_SECTION = false;

        while(lines_remaining) {
            String line = codeLines.get(PC);

            if (Objects.equals(line, "#CODE")) {
                DATA_SECTION = false;
                CODE_SECTION = true;
                System.out.println("Data section finished");
                memory.print(128);
                System.out.println("Starting code section..");
            }

            if (DATA_SECTION) {
                memory.writeVariable(line);
            }

            if (CODE_SECTION) {
                ;
            }

            if (Objects.equals(line, "#DATA")) {
                System.out.println("Starting data section..");
                DATA_SECTION = true;
            }

            // Stop condition
            if (Objects.equals(line, "HLT")) {
                System.out.println("Code section finished");
                lines_remaining = false;
            }
            PC++;
        }
    }

    // TEST functions ------------------------------------
    public  static void TEST_memory() {
        Memory memory = new Memory();
        String codeLine = "A 10";
        String codeLine2 = "B 127";
        memory.writeVariable(codeLine);
        memory.writeVariable(codeLine2);
        memory.print(100);
    }
    public static void TEST_twosComplement() {
        Register reg1 = new Register("reg1");
        reg1.setFromDecimal(127);
        String binaryString = reg1.getBinaryString();
        String complement = Tools.twosComplement(binaryString);
        System.out.println(binaryString);
        System.out.println(complement);
    }
    public static void TEST_binaryAddition() {
        System.out.println("TEST_binaryAddition");
        // number1 + number2
        // Change number to test
        int number1 = 1;
        int number2 = 127;

        Register reg1 = new Register("reg1");
        reg1.setFromDecimal(number1);
        Register reg2 = new Register("reg2");
        reg2.setFromDecimal(number2);

        reg1.print();
        reg2.print();

        System.out.println();

        String res = Tools.binaryAddition32bit(reg1.getBinaryString(), reg2.getBinaryString());
        System.out.println("res: " + res);
    }

    public static void TEST_binarySubtraction() {
        System.out.println("TEST_binarySubtraction");
        // number1 - number2
        // Change numbers to test
        int number1 = 64;
        int number2 = 128;

        Register reg1 = new Register("reg1");
        reg1.setFromDecimal(number1);
        Register reg2 = new Register("reg2");
        reg2.setFromDecimal(number2);

        reg1.print();
        reg2.print();

        System.out.println();

        String res = Tools.binarySubtraction32bit(reg1.getBinaryString(), reg2.getBinaryString());
        System.out.println(number1 + " - " + number2 + " = " + Tools.binaryStringToDecimal(res) + String.format("(expected %d)", number1 - number2));
        System.out.println("res: " + res);
    }
}
