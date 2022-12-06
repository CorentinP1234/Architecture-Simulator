import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //TODO
        // SUB to working

        // file selector
        String fileToRun = FileReader.fileSelector();
        ArrayList<String> codeLines = FileReader.readFile_withoutComments(fileToRun);

        // Manual file selection
//         ArrayList<String> codeLines = FileReader.readFile_withoutComments("TEST_mul.txt");

        ALU alu = new ALU();
        alu.memory = new Memory();
        alu.stack = new Stack();

        int PC = 0;

        startExecution(codeLines, alu, PC);

        // Comment est implemente la Stack ?
        // Voir: - Stack.bitArray
//               - Stack.push()
        //       - Stack.pop()

        // Comment est implemente la memoire ?
        // Voir: - Memory.byteArray
        //       - Memory.writeFromCodeLine()
        //       - Memory.readFromName()

        // Comment sont implemente les registres ?
        // Voir: - Register.bitArray
//         Register.write()
        //       - Register.read()
    }

    public static void startExecution(ArrayList<String> codeLines, ALU alu, int PC) {
        boolean lines_remaining = true;
        boolean DATA_SECTION = false;
        boolean CODE_SECTION = false;

        boolean commandPrompt = true;

        while(lines_remaining) {

            // Fetch line corresponding to the PC
            String line = codeLines.get(PC);

            // Enter #CODE section and print all variable from #DATA section
            if (Objects.equals(line, "#CODE")) {
                DATA_SECTION = false;
                CODE_SECTION = true;
                alu.memory.printAllVar();
                System.out.println("#DATA section finished\n");
                System.out.println("Starting #CODE section..");
                PC++;
                continue;
            }

            // When in #DATA section, write variable in memory
            if (DATA_SECTION) {
                alu.memory.writeFromCodeLine(line);
            }

            // When in #CODE section, perform the instruction and print the command prompt
            if (CODE_SECTION && !Objects.equals(line, "HLT")) {

                // Read instruction
                PC = parseInstructions(line, alu, PC);

                // Check if prompt has been disable
                if (commandPrompt) {

                    // Read command
                    commandPrompt = promptCommands(line, alu);
                }
            }

            // Enter #DATA section
            if (Objects.equals(line, "#DATA")) {
                System.out.println("Starting #DATA section..");
                DATA_SECTION = true;
            }

            // Stop execution when instruction HLT is found, print the command prompt
            if (Objects.equals(line, "HLT")) {
                System.out.println("> HLT\n");
                alu.memory.printAllVar();
                promptCommands("", alu);
                lines_remaining = false;
            }

            // Increment PC after each instruction
            PC++;
        }
    }

    public static boolean promptCommands(String line, ALU alu) {
        Scanner scanner = new Scanner(System.in);
        boolean repeatPrompt = true;

        // Repeat prompt printing until "next" or "end" command
        do {

            // Print the code line above the prompt
            if (!Objects.equals(line, "")) {
                System.out.print("> ");
                System.out.println(line);
            }

            // Print prompt
            System.out.println("\nCommands: print <reg/var>, print memory, next or end");
            System.out.print(">>");

            // Take the command
            String input = scanner.nextLine();
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
                    // print <memory>
                    else if (Objects.equals(argument, "memory")) {
                        alu.memory.print();
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
