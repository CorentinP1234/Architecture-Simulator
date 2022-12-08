public class TEST {

    // Archive
//        public static void oldStartExecution(ArrayList<String> codeLines, ALU alu, int PC) {
//        boolean lines_remaining = true;
//        boolean DATA_SECTION = false;
//        boolean CODE_SECTION = false;
//        boolean commandPrompt = true;
//
//        while(lines_remaining) {
//
//            // Fetch line corresponding to the PC
//            String line = codeLines.get(PC);
//
//            // Enter #CODE section and print all variable from #DATA section
//            if (Objects.equals(line, "#CODE")) {
//                DATA_SECTION = false;
//                CODE_SECTION = true;
//                alu.memory.printAllVar();
//                System.out.println("#DATA section finished\n");
//                System.out.println("Starting #CODE section..");
//                PC++;
//                continue;
//            }
//
//            // When in #DATA section, write variable in memory
//            if (DATA_SECTION) {
//                alu.memory.writeFromLine(line);
//            }
//
//            // When in #CODE section, perform the instruction and print the command prompt
//            if (CODE_SECTION && !Objects.equals(line, "HLT")) {
//
//                // Read instruction
//                PC = parseInstructions(line, alu, PC);
//
//                // Check if prompt has been disabled
//                if (commandPrompt) {
//
//                    // Read command
//                    commandPrompt = promptCommands(line, alu);
//                }
//            }
//
//            // Enter #DATA section
//            if (Objects.equals(line, "#DATA")) {
//                System.out.println("Starting #DATA section..");
//                DATA_SECTION = true;
//            }
//
//            // Stop execution when instruction HLT is found, print the command prompt
//            if (Objects.equals(line, "HLT")) {
//                System.out.println("> HLT\n");
//                alu.memory.printAllVar();
//                promptCommands("", alu);
//                lines_remaining = false;
//            }
//
//            // Increment PC after each instruction
//            PC++;
//        }
//    }
    // -------------------------------------------

    public static void TEST_binaryAddition() {
        System.out.println("TEST_binaryAddition");
        // number1 + number2
        // Change number to test
        int number1 = 1;
        int number2 = 127;

        Register reg1 = new Register("reg1");
        reg1.write(Tools.convertDecToBin32(number1));
        Register reg2 = new Register("reg2");
        reg1.write(Tools.convertDecToBin32(number2));

        reg1.print();
        reg2.print();

        System.out.println();

        String res = Tools.binaryAddition32bit(reg1.read(), reg2.read());
        System.out.println("res: " + res);
    }

    public static void TEST_binarySubtraction() {
        System.out.println("TEST_binarySubtraction");
        // number1 - number2
        // Change numbers to test
        int number1 = 64;
        int number2 = 128;

        Register reg1 = new Register("reg1");
        reg1.write(Tools.convertDecToBin32(number1));
        Register reg2 = new Register("reg2");
        reg1.write(Tools.convertDecToBin32(number2));

        reg1.print();
        reg2.print();

        System.out.println();

        String res = Tools.binarySubtraction32bit(reg1.read(), reg2.read());
        System.out.println(number1 + " - " + number2 + " = " + Tools.convertBin32ToDec(res) + String.format("(expected %d)", number1 - number2));
        System.out.println("res: " + res);
    }

    public static void TEST_twosComplement() {
        int numberToTest = 127;

        Register reg1 = new Register("reg1");
        reg1.write(Tools.convertDecToBin32(numberToTest));
        String binaryString = reg1.read();
        String complement = Tools.twosComplement(binaryString);
        System.out.println(binaryString);
        System.out.println(complement);
    }

    public  static void TEST_memory() {
        Memory memory = new Memory();
        String codeLine = "A 10";
        String codeLine2 = "B 127";
        memory.writeFromLine(codeLine);
        memory.writeFromLine(codeLine2);
        memory.print();
    }
}
