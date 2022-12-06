public class TEST {
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
        memory.writeFromCodeLine(codeLine);
        memory.writeFromCodeLine(codeLine2);
        memory.print();
    }
}
