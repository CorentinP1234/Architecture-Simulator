import java.util.Objects;

public class Tools {
    public static String convertDecToBin32(int decimalValue) {

        // Convert decimal integer in binary string
        String binary = Integer.toBinaryString(decimalValue);

        // Get a 32-bit format by adding 0 before the first significant bit
        String formattedBinary = String.format("%32s", binary).replace(" ", "0");

        return formattedBinary;
    }
    public static String twosComplement(String binaryString) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(binaryString);

        // One's complement (flip each bit)
        for(int i = 0; i < 32; i++) {
            stringBuilder.setCharAt(i, flip(stringBuilder.charAt(i)));
        }

        // Add one to get the two's complement
        String twosComplement = binaryAddition32bit(stringBuilder.toString(), "00000000000000000000000000000001");

        return twosComplement;
    }
    public static char flip(char c) {
        if (c == '0')
            return '1';
        else
            return '0';
    }

    public static String binaryAddition32bit(String binaryString1, String binaryString2) {
        StringBuilder res = new StringBuilder();

        int carry = 0;
        int bit1;
        int bit2;
        int bit3;
        for(int i = 0; i < 32; i++) {
            bit1 = Character.getNumericValue(binaryString1.charAt(31-i));
            bit2 = Character.getNumericValue(binaryString2.charAt(31-i));
            bit3 = bit1 + bit2 + carry;
            if (bit3 == 2) {
                bit3 = 0;
                carry = 1;
            } else if (bit3 == 3) {
                bit3 = 1;
                carry = 1;
            }
            else {
                carry = 0;
            }
            res.append(Character.forDigit(bit3, 10));
        }
        res.reverse();
        return res.toString();
    }
    public static String binarySubtraction32bit(String binaryString1, String binaryString2) {
        // binaryString1 - binaryString2
        String tmpString = twosComplement(binaryString2);
        String res = binaryAddition32bit(binaryString1, tmpString);
        return res;
    }

    public static int convertBin32ToDec(String binaryString) {
        String tmpBinaryString;
        boolean isNegative = false;

        // Negative number
        if(binaryString.charAt(0) == '1') {
            tmpBinaryString = twosComplement(binaryString);
            isNegative = true;
        }
        // Positive number
        else {
            tmpBinaryString = binaryString;
        }

        int value = 0;
        for (int i = 0; i < 32; i++) {
            char charDigit = tmpBinaryString.charAt(i);
            int digit = Character.getNumericValue(charDigit);
            value += digit * Math.pow(2, 31-i);
        }

        if (isNegative) {
            value *= -1;
        }

        return value;
    }
    public static String AndOperation(String bin1, String bin2) {
        StringBuilder stringBuilder = new StringBuilder(32);

        for (int i = 0; i < 32; i++) {
            if(bin1.charAt(i) == '1' && bin2.charAt(i) == '1')
                stringBuilder.append('1');
            else {
                stringBuilder.append('0');
            }
        }

        return stringBuilder.toString();
    }

    public static String OrOperation(String bin1, String bin2) {
         StringBuilder stringBuilder = new StringBuilder(32);

        for (int i = 0; i < 32; i++) {
            if(bin1.charAt(i) == '1' || bin2.charAt(i) == '1')
                stringBuilder.append('1');
            else {
                stringBuilder.append('0');
            }
        }

        return stringBuilder.toString();
    }

    public static boolean isBigger(String bin1, String bin2) {
        int value1 = convertBin32ToDec(bin1);
        int value2 = convertBin32ToDec(bin2);

        return value1 > value2;
    }

    public static boolean isSmaller(String bin1, String bin2) {
        int value1 = convertBin32ToDec(bin1);
        int value2 = convertBin32ToDec(bin2);

        return value1 < value2;
    }
    public static boolean isRegisterName(String str) {
        return Objects.equals(str, "T0") || Objects.equals(str, "T1")
                || Objects.equals(str, "T2") || Objects.equals(str, "T3");
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isVariableInMemory(String param2, ALU alu) {
        if(alu.memory.NameToAddressMap.containsKey(param2)) {
                return true;
        }
        else {
            System.out.println("Error: variable does not exist:");
            System.out.println("Variable: " + param2);
            System.exit(1);
            return false;
        }
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

                return null;
        }
    }
}
