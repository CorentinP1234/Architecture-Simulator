import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> codeLines = FileReader.readFile_withoutComments("first_file.txt");

        ALU alu = new ALU();
        alu.memory = new Memory();
        alu.stack = new Stack();

        int PC = 0;

        startExecution(codeLines, alu, PC);

        // Comment est implemente la Stack ?
        // Voir: - Stack.bitArray
        //       - Stack.push()
        //       - Stack.pop()

        // Comment est implemente la memoire ?
        // Voir: - Memory.byteArray
        //       - Memory.writeFromCodeLine()
        //       - Memory.readFromName()

        // Comment sont implemente les registres ?
        // Voir: - Register.bitArray
        //       - Register.write()
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
                PC = code_section(line, alu, PC);
                if (commandPrompt) {
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
                System.out.println("> Code section finished\n");
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
            System.out.println("\nCommands: print <reg>, printVar <var>,memory <bits>, \"next\" or \"end\" to finish execution");
            System.out.print(">>");

            // Take the command
            String input = scanner.nextLine();
            System.out.println();

            // Split the command by word
            String[] lineElement = input.split(" ");

            // Select command
            switch (lineElement[0]) {

                // Print register
                case "print":

                    // Select register, return null if invalid register name
                    Register reg = Tools.selectRegisterByName(lineElement[1], alu);
                    if (reg == null) {
                        System.out.println("Error: wrong register name:");
                        System.out.println("Example: T0, T1, T2, T3");
                    } else {
                        reg.print();
                    }
                    break;

                // Print variable
                case "printVar":
                    try {
                        alu.memory.printVariable(lineElement[1]);
                    }
                    // If there is no variable with this name, print warning
                    catch(IllegalArgumentException ie) {
                        System.out.println("Wrong variable name");
                    }
                    break;

                // Print region in memory, specify number of bits to read
                case "memory":
                    int numberOfBits;
                    try {
                        numberOfBits = Integer.parseInt(lineElement[1]);
                        alu.memory.printBits(numberOfBits);
                    }catch (Exception e) {
                        System.out.println("Invalid number of bits");
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

    public static int code_section(String line, ALU alu, int PC) {
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
                if (isNumberOfArgumentInvalid((numberOfArgument == 2), line))
                    Instructions.LDA(lineElements[1], lineElements[2], alu);
                break;

            case "STR":
                if (isNumberOfArgumentInvalid((numberOfArgument == 2), line))
                    Instructions.STR(lineElements[1], lineElements[2], alu);
                break;

            case "PUSH":
                if (isNumberOfArgumentInvalid((numberOfArgument == 1), line))
                    Instructions.PUSH(lineElements[1], alu);
                break;

            case "POP":
                if (isNumberOfArgumentInvalid((numberOfArgument == 1), line))
                    Instructions.POP(lineElements[1], alu);
                break;

            case "AND":
                if (isNumberOfArgumentInvalid((numberOfArgument == 2), line))
                    Instructions.AND(lineElements[1], lineElements[2], alu);
                break;

            case "OR":
                if (isNumberOfArgumentInvalid((numberOfArgument == 2), line))
                    Instructions.OR(lineElements[1], lineElements[2], alu);
                break;

            case "NOT":
                if (isNumberOfArgumentInvalid((numberOfArgument == 1), line))
                    Instructions.NOT(lineElements[1], alu);
                break;

            case "ADD":
                if (isNumberOfArgumentInvalid((numberOfArgument == 2), line))
                    Instructions.ADD(lineElements[1], lineElements[2], alu);
                break;

            case "SUB":
                if (isNumberOfArgumentInvalid((numberOfArgument == 2), line))
                    Instructions.SUB(lineElements[1], lineElements[2], alu);
                break;

            case "DIV":
                if (isNumberOfArgumentInvalid((numberOfArgument == 2), line))
                    Instructions.DIV(lineElements[1], lineElements[2], alu);
                break;

            case "MUL":
                if (isNumberOfArgumentInvalid((numberOfArgument == 2), line))
                    Instructions.MUL(lineElements[1], lineElements[2], alu);
                break;

            case "MOD":
                if (isNumberOfArgumentInvalid((numberOfArgument == 2), line))
                    Instructions.MOD(lineElements[1], lineElements[2], alu);
                break;

            case "INC":
                if (isNumberOfArgumentInvalid((numberOfArgument == 1), line))
                    Instructions.INC(lineElements[1], alu);
                break;

            case "DEC":
                if (isNumberOfArgumentInvalid((numberOfArgument == 1), line))
                    Instructions.DEC(lineElements[1], alu);
                break;

            case "BEQ":
                if (isNumberOfArgumentInvalid((numberOfArgument == 3), line))
                    return Instructions.BEQ(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "BNE":
                if (isNumberOfArgumentInvalid((numberOfArgument == 3), line))
                    return Instructions.BNE(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "BBG":
                if (isNumberOfArgumentInvalid((numberOfArgument == 3), line))
                    return Instructions.BBG(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "BSM":
                if (isNumberOfArgumentInvalid((numberOfArgument == 3), line))
                    return Instructions.BSM(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "JMP":
                if (isNumberOfArgumentInvalid((numberOfArgument == 1), line))
                    return Instructions.JMP(lineElements[1], alu);
                break;
        }
        return PC;
    }

    public static boolean isNumberOfArgumentInvalid(boolean comparison, String line) {
        if (comparison)
            return true;
        else {
            System.out.println("Error: invalid number of argument:");
            System.out.println("In line: " + line);
            return false;
        }
    }
}
