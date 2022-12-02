import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Memory {
    public int[][] byteArray;

    private int writingPointer = 0;

    // Give the address of a given variable
    public final Map<String, Integer> NameToAddressMap = new HashMap<>();

    Memory() {
        // 8192 bytes Memory
        this.byteArray = new int[8192][8];
    }

    public void writeFromCodeLine(String codeLine) {

        // Extract variable name and value from the code line
        String[] elements = codeLine.split(" ");
        if (elements.length != 2 ) {
            System.out.println("Error reading variable: format <var> <value>: " + codeLine);
            System.exit(1);
        }
        String varName = elements[0];

        // Check if variableName is a register name
        // If yes stop execution
        if (Tools.isRegisterName(varName)) {
            System.out.println("Error: register name are reserved:");
            System.out.println("In line: " + codeLine);
            System.exit(1);
        }

        int value = Integer.parseInt(elements[1]);

        // Compute the binary representation
        String binaryString = Tools.convertDecToBin32(value);

        // Write variable in memory
        int byteIndex = 0;
        for (int i = 0; i < 32; i++) {
            char binaryChar = binaryString.charAt(i);
            this.byteArray[writingPointer + byteIndex][i%8] = Character.getNumericValue(binaryChar);
            if (i%8 == 7)
                byteIndex++;
        }

        // Update NameToAddressMap
        NameToAddressMap.put(varName, writingPointer);

        // Update pointer to next memory space
        writingPointer += 4;
    }

    public String readFromName(String varName) {
        // Get address of the variable, if there is no such variable return an empty string
        int address = NameToAddressMap.getOrDefault(varName, -1);
        if (address == -1) {
            return "";
        }
        // Return the binary representation of the variable
        return readFromAddress(address);
    }

    public String readFromAddress(int address) {

        // Temporary string array to build the binary number
        StringBuilder binaryString = new StringBuilder();
        int byteIndex = 0;
        for (int i = 0; i < 32; i++) {
            binaryString.append(byteArray[address + byteIndex][i % 8]);
            if(i%8 == 7)
                byteIndex++;
        }

        // Return the binary number
        return binaryString.toString();
    }

    public void setValueFromName(String binaryString, String Name) {
        int address = NameToAddressMap.get(Name);
        setValueFromAddress(binaryString, address);
    }

    public void setValueFromAddress(String binaryString, int address) {
        int byteIndex = 0;
        for (int i = 0; i < 32; i++) {
            byteArray[address + byteIndex][i%8] = Character.getNumericValue(binaryString.charAt(i));
            if (i%8 == 7)
                byteIndex++;
        }
    }

    public void printBits(int numberOfBits) {
        // Print memory
        int byteIndex = 0;
        System.out.printf("%2d: ", 1);
        for (int i = 0; i < numberOfBits; i++) {
            System.out.print(byteArray[byteIndex][i%8] + " ");
            if(i%8 == 7) {
                byteIndex++;
                System.out.println();
                if (i != numberOfBits-1)
                    System.out.printf("%2s: ", (byteIndex+1));
            }

        }
        System.out.println();

        // Print NameToAddressMap
        System.out.println("Variable Addresses (in Bytes) : ");
        for(Map.Entry<String, Integer> entry : NameToAddressMap.entrySet()) {
            String Name = entry.getKey();
            int address = entry.getValue();
            System.out.println(Name + " stored at " + (address + 1));
        }
        System.out.println();
        System.out.println();
    }

    public void printAllVar() {
        // Loop for all key and value
        for (Map.Entry<String, Integer> entry : NameToAddressMap.entrySet()) {
            String varName = entry.getKey();
            int address = entry.getValue();

            String bin = readFromAddress(address);
            int decimalValue = Tools.convertBin32ToDec(bin);
            System.out.println("VAR " + varName + " = " + decimalValue);
        }
    }

    public void printVariable(String variable) {
        String bin = readFromName(variable);
        if (Objects.equals(bin, "")) {
            throw new IllegalArgumentException();
        } else {
            int decimalValue = Tools.convertBin32ToDec(bin);

            System.out.println("VAR " + variable + " = " + decimalValue + " : " + bin + "\n");
        }

    }
}
