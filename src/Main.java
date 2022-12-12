import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static final Scanner scan = new Scanner(System.in);
    static boolean lastPrompt;

    public static void main(String[] args) throws FileNotFoundException {
        if (GUISelector()){
            // GUI
            Screen.main(args);
        }
        else {
            // Terminal
            while(true) {
                String fileToRun = FileReader.fileSelector();
                ArrayList<String> codeLines = FileReader.readFile_withoutComments(fileToRun);

                ALU alu = new ALU();
                alu.memory = new Memory();
                alu.stack = new Stack();

                startExecutionTerminal(codeLines, alu);
                System.out.println();
                System.out.println("Press Enter to continue:");
                scan.nextLine();
            }
        }

        // How is implemented the Stack ?
        // See first: - Stack.bitArray
        //            - Stack.push()
        //            - Stack.pop()

        // How is implemented the Memory ?
        // See first: - Memory.byteArray
        //            - Memory.writeFromCodeLine()
        //            - Memory.readFromName()

        // How is implemented the Registers ?
        // See first: - Register.bitArray
        //            - Register.write()
        //            - Register.read()
    }

    public static void readDataSection(ArrayList<String> dataSection, Memory memory) {
        for (String line : dataSection) {
            memory.writeFromLine(line);
        }
    }

    public static Object[] stepExecution(ArrayList<String> codeLines, ALU alu, int PC, boolean commandPrompt) {

        // Stop execution (last instruction was HLT)
        if (PC == -1) {
            Object[] res = new Object[2];
            res[0] = -1;
            res[1] = commandPrompt;
            return res;
        }

        String currentLine = codeLines.get(PC);
        boolean isLastInstruction = Objects.equals(currentLine, "HLT");

        if (!Objects.equals(currentLine, "HLT")) {
            PC = Instruction.parseInstructions(currentLine, alu, PC);
            PC++;
            if (commandPrompt){
                commandPrompt = promptCommands(currentLine, alu);
            }
        }

        if (isLastInstruction) {
            System.out.println("> HLT\n");
            alu.memory.printAllVar();
            // User can enter command after the execution to check the state of all type of memory
            if (lastPrompt)
                promptCommands("", alu);

            Object[] res = new Object[2];
            res[0] = -1;
            res[1] = commandPrompt;
            // PC = -1
            return res;
        }
        else {
            Object[] res = new Object[2];
            res[0] = PC;
            res[1] = commandPrompt;
            return res;
        }
    }

    public static void simulate(ArrayList<String> codeLines, ALU alu, boolean commandPrompt) {
        lastPrompt = commandPrompt;
        Object[] res;
        int PC = 0;
        // If PC is set to -1 it means that stepExecution has read "HLT"
        while(PC != -1) {
            res = stepExecution(codeLines, alu, PC, commandPrompt);
            PC = (int) res[0];
            commandPrompt = (boolean) res[1];
        }
    }

     public static void startExecutionTerminal(ArrayList<String> codeLines, ALU alu) {

        // Data Section
        System.out.println("Starting Data section..\n");
        ArrayList<String> dataSection = FileReader.getDataSection(codeLines);
        readDataSection(dataSection, alu.memory);
        alu.memory.printAllVar();
        System.out.println("\nDATA section finished\n");

        // Code Section
        System.out.println("Starting CODE section..\n");
        ArrayList<String> codeSection = FileReader.getCodeSection(codeLines);
        simulate(codeSection, alu, true);
        System.out.println("CODE section finished\n");
    }

    public static boolean promptCommands(String line, ALU alu) {
        boolean repeatPrompt = true;
        boolean showCommandAndCodeLine = true;

        // Repeat prompt printing until "next" or "end" command
        do {
            if (showCommandAndCodeLine){
                if (!Objects.equals(line, "")) {
                    System.out.print("> ");
                    // Print the code line above the prompt
                    System.out.println(line);
                }
                // Print prompt
                System.out.println("\nCommands: print <reg>/<var>, print memory/stack, next or end");
                showCommandAndCodeLine = false;
            }
            System.out.print(">>");

            // Take the command
            String input = scan.nextLine();
            System.out.println();

            // Split the command by word
            String[] lineElement = input.split(" ");

            String command = lineElement[0];

            // Select command
            switch (command) {

                case "print":
                    if (lineElement.length != 2)
                        continue;
                    String argument = lineElement[1];

                    // print <reg>
                    // Select register, return null if invalid register name
                    if (Register.isRegisterName(argument)) {
                        Register reg = Register.selectRegisterByName(argument, alu);
                        if (reg != null) {
                            reg.print();
                        } else {
                            System.out.println("Error: wrong register name:");
                            System.out.println("Example: T0, T1, T2, T3\n");
                        }
                    }

                    // print <var>
                    else if (Memory.isVarName(argument)){
                        try {
                            alu.memory.printVariable(lineElement[1]);
                        }
                        // If there is no variable with this name, print warning
                        catch(IllegalArgumentException ie) {
                            System.out.println("Wrong variable name");
                        }
                    }

                    // print memory
                    else if (Objects.equals(argument, "memory")) {
                        alu.memory.print();
                    }

                    // print stack
                    else if (Objects.equals(argument, "stack")) {
                        alu.stack.print();
                    }

                    else {
                        System.out.println("Invalid command\n");
                    }
                    break;

                // Go to next instruction
                case "next":
                case "n":
                    repeatPrompt = false;
                    break;

                // Finish executing with no prompt
                case "end":
                    return false;

                default:
                    break;
            }
        } while (repeatPrompt);

        return true;
    }

    public static boolean GUISelector() {
        boolean isInputValid = true;
        int selection = -1;
        do {
            try {
                System.out.println("Choose:");
                System.out.println("1. GUI");
                System.out.println("2. Terminal");
                selection = Integer.parseInt(scan.nextLine());
                if (selection == 1 || selection == 2)
                    isInputValid = false;
            } catch (Exception e){
                System.out.println("Invalid input !");
            }
        }while (isInputValid);
        System.out.println();
        return selection == 1;
    }
}
