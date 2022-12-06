import java.util.Objects;

public class Register {
    int[] bitArray;
    String registerName;

    Register(String registerName) {

        // Register name is used by the print method
        this.registerName = registerName;

        // Initialize all bits to 0
        bitArray = new int[32];
        for (int i = 0; i < 32; i++) {
            bitArray[i] = 0;
        }
    }

    public String read() {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < 32; i++) {
            res.append(bitArray[i]);
        }
        return res.toString();
    }

    public void write(String binaryString) {
        for(int i = 0; i < 32; i++) {
            char bitCharacter = binaryString.charAt(i);
            int bitValue = Character.getNumericValue(bitCharacter);
            bitArray[i] = bitValue;
        }
    }

    public void print() {

        // Get decimal value from the array of bits
        String binaryString = read();
        int decimalValue = Tools.convertBin32ToDec(binaryString);

        // Print the register name and decimal value
        System.out.println("Register " + registerName + " contains " + decimalValue + " : " + binaryString);
        System.out.print("\n");
    }

    public static boolean isRegisterName(String str) {
        return Objects.equals(str, "T0") || Objects.equals(str, "T1")
                || Objects.equals(str, "T2") || Objects.equals(str, "T3");
    }

    public static Register selectRegisterByName(String registerName, ALU alu) {
        switch (registerName) {
            case "t0":
            case "T0":
                return alu.t0;
            case "t1":
            case "T1":
                return alu.t1;
            case "t2":
            case "T2":
                return alu.t2;
            case "t3":
            case "T3":
                return alu.t3;
            default:
                // For warning issues
                System.out.println("Error in selectRegisterByName: should not be printed");
                return alu.t0;
        }
    }
}
