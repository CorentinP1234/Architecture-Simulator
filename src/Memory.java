import java.util.HashMap;
import java.util.Map;

public class Memory {
    public int[][] byteArray;

    private int writingPointer = 0;

    // Give the address of a given variable
    public final Map<String, Integer> NameToAddressMap = new HashMap<>();

    Memory() {
        // 8192 bytes Memory
        this.byteArray = new int[8192][8];
    }

    public void writeVariable(String codeLine) {

        // Extract variable name and value from the code line
        String[] elements = codeLine.split(" ");
        if (elements.length > 2) {
            System.out.println("Error reading variable: incorrect format : " + codeLine);
        }
        String varName = elements[0];
        int value = Integer.parseInt(elements[1]);

        // Compute the binary representation
        String binaryString = Tools.convertDecimalTo32BitString(value);

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

    public String getBinaryStringFromName(String Name) {
        int address = NameToAddressMap.get(Name);
        return getStringBinaryFromAddress(address);

    }
    public String getStringBinaryFromAddress(int address) {
        StringBuilder binaryString = new StringBuilder();
        int byteIndex = 0;
        for (int i = 0; i < 32; i++) {
            binaryString.append(byteArray[address + byteIndex][i % 8]);
            if(i%8 == 7)
                byteIndex++;
        }
        return binaryString.toString();
    }
    public void setValueFromName(String binaryString, String Name) {
        int address = NameToAddressMap.get(Name);
        setValueFromAddress(binaryString, address);
    }
    public void setValueFromAddress(String binaryString, int address) {
        int byteIndex = 0;
        for (int i = 0; i < 32; i++) {
            byteArray[address + byteIndex][i%8] = binaryString.charAt(i);
        }
    }
    public void print(int numberOfBits) {
        // Print memory
        int byteIndex = 0;
        System.out.printf( "%2s: ", 1);
        for (int i = 0; i < numberOfBits; i++) {
            System.out.print(byteArray[byteIndex][i%8] + " ");
            if(i%8 == 7) {
                byteIndex++;
                System.out.println();
                System.out.printf("%2s: ", (byteIndex+1));
            }

        }
        System.out.println();

        // Print NameToAddressMap
        System.out.println(NameToAddressMap);
    }
}
