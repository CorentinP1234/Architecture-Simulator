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

    public void print() {

        // Get decimal value from the array of bits
        StringBuilder binaryString = new StringBuilder();
        for(int i = 0; i < 32; i++) {
           binaryString.append(bitArray[i]);
        }
        int decimalValue = Tools.convertBin32ToDec(binaryString.toString());

        // Print the register name and decimal value
        System.out.println("Register " + registerName + " contains " + decimalValue + " : " + binaryString.toString());

        System.out.print("\n");
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
}
