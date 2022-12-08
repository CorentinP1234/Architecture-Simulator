import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static Screen screen;
    static final Scanner scan = new Scanner(System.in);
    static boolean testBool = true;

    public static void main(String[] args) throws FileNotFoundException {
        //TODO
        // SUB might not work

        String fileToRun = FileReader.fileSelector();
        ArrayList<String> codeLines = FileReader.readFile_withoutComments(fileToRun);

        ALU alu = new ALU();
        alu.memory = new Memory();
        alu.stack = new Stack();

        startExecution(codeLines, alu);

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

    public static void readCodeSection(ArrayList<String> codeSection, ALU alu) {
        boolean commandPrompt = true;
        int PC = 0;

        // Fetch line corresponding to the PC
        String  currentLine = codeSection.get(PC);

        while (!Objects.equals(currentLine, "HLT")) {

            // Read instruction
            PC = parseInstructions(currentLine, alu, PC);

            // Don't show the command prompt if "end" command was used
            if (commandPrompt)
                // Print the prompt and do the command entered
                // Return false if the command is "end"
                commandPrompt = promptCommands(currentLine, alu);

            // Update the PC
            PC++;
            currentLine = codeSection.get(PC);
        }

        System.out.println("> HLT\n");
        alu.memory.printAllVar();

        // User can enter command after the execution to check the state of all type of memory
        promptCommands("", alu);
    }

    public static int stepExecution(ArrayList<String> codeLines, ALU alu, int PC) {
        String currentLine = codeLines.get(PC);
        boolean isLastInstruction = Objects.equals(currentLine, "HLT");

        if (!Objects.equals(currentLine, "HLT")) {
            PC = parseInstructions(currentLine, alu, PC);
            PC++;
            if (testBool){
                testBool = promptCommands(currentLine, alu);
            }
        }

        if (isLastInstruction) {
            System.out.println("> HLT\n");
            alu.memory.printAllVar();
            // User can enter command after the execution to check the state of all type of memory
            promptCommands("", alu);
            return -1;
        }
        else
            return PC;
    }
    public static void simulate(ArrayList<String> codeLines, ALU alu) {
        int PC = 0;
        // If PC is set to -1 it means that stepExecution has read "HLT"
        while(PC != -1) {
            PC = stepExecution(codeLines, alu, PC);
        }
    }


     public static void startExecution(ArrayList<String> codeLines, ALU alu) {

        System.out.println("Starting Data section..\n");
        ArrayList<String> dataSection = FileReader.getDataSection(codeLines);
        readDataSection(dataSection, alu.memory);
        alu.memory.printAllVar();
        System.out.println("\nDATA section finished\n");

        System.out.println("Starting CODE section..\n");
        ArrayList<String> codeSection = FileReader.getCodeSection(codeLines);
//        readCodeSection(codeSection, alu);
         simulate(codeSection, alu);
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

    public static int parseInstructions(String line, ALU alu, int PC) {
        String[] lineElements = line.split(" ");
        int numberOfArgument = lineElements.length - 1;

        // Check for label
        if (numberOfArgument == 0) {

            // Label example : "LOOP:"
            int labelLength = lineElements[0].length();

            // Remove ":" at the end
            String label = lineElements[0].substring(0, labelLength-1);

            // Store in Map
            alu.labelToCodeLine.put(label, PC);
        }

        String instruction = lineElements[0];


        switch (instruction) {
            case "LDA":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.LDA(lineElements[1], lineElements[2], alu);
                break;

            case "STR":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.STR(lineElements[1], lineElements[2], alu);
                break;

            case "PUSH":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line))
                    Instruction.PUSH(lineElements[1], alu);
                break;

            case "POP":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line))
                    Instruction.POP(lineElements[1], alu);
                break;

            case "AND":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.AND(lineElements[1], lineElements[2], alu);
                break;

            case "OR":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.OR(lineElements[1], lineElements[2], alu);
                break;

            case "NOT":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line))
                    Instruction.NOT(lineElements[1], alu);
                break;

            case "ADD":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.ADD(lineElements[1], lineElements[2], alu);
                break;

            case "SUB":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.SUB(lineElements[1], lineElements[2], alu);
                break;

            case "DIV":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.DIV(lineElements[1], lineElements[2], alu);
                break;

            case "MUL":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.MUL(lineElements[1], lineElements[2], alu);
                break;

            case "MOD":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.MOD(lineElements[1], lineElements[2], alu);
                break;

            case "INC":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line))
                    Instruction.INC(lineElements[1], alu);
                break;

            case "DEC":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line))
                    Instruction.DEC(lineElements[1], alu);
                break;

            case "BEQ":
                if (isNumberOfArgumentValid((numberOfArgument == 3), line))
                    return Instruction.BEQ(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "BNE":
                if (isNumberOfArgumentValid((numberOfArgument == 3), line))
                    return Instruction.BNE(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "BBG":
                if (isNumberOfArgumentValid((numberOfArgument == 3), line))
                    return Instruction.BBG(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "BSM":
                if (isNumberOfArgumentValid((numberOfArgument == 3), line))
                    return Instruction.BSM(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "JMP":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line)) {
                    return Instruction.JMP(lineElements[1], alu);
                }
                break;
        }
        return PC;
    }

    public static boolean isNumberOfArgumentValid(boolean comparison, String line) {
        if (comparison)
            return true;
        else {
            System.out.println("\nError: invalid number of argument:");
            System.out.println("In line: " + line);
            System.exit(1);
            return false;
        }
    }
}
