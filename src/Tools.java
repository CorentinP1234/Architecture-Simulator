public class Tools {
    public static String convertDecimalTo32BitString(int decimalValue) {

        // Convert decimal integer in binary string
        String binaryString = Integer.toBinaryString(decimalValue);

        // Get a 32-bit format by adding 0 before the first significant bit
        String formattedBinaryString = String.format("%32s", binaryString).replace(" ", "0");

        return formattedBinaryString;
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
        String tmpString = twosComplement(binaryString2);
        String res = binaryAddition32bit(binaryString1, tmpString);
        return res;
    }

    public static int binaryStringToDecimal(String binaryString) {
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
}
