import java.util.Objects;

public class Tools {
    public static String convertDecToBin32(int decimalValue) {

        // Check negative number
        if (decimalValue < 0) {
            int positive_value = decimalValue * -1;
            String positiveBin = Integer.toBinaryString(positive_value);

            String formattedBinary = String.format("%32s", positiveBin).replace(" ", "0");
            return twosComplement(formattedBinary);
        }

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

//    public static String binaryAddition32bit(String binaryString1, String binaryString2) {
//        int value1 = convertBin32ToDec(binaryString1);
//        int value2 = convertBin32ToDec(binaryString2);
//
//        int res = value1 + value2;
//        System.out.println(value1);
//        System.out.println(value2);
//        System.out.println(res);
//        System.exit(1);
//
//        return convertDecToBin32(res);
//    }

//    public static String binarySubtraction32bit(String binaryString1, String binaryString2) {
//        // binaryString1 - binaryString2
//        String tmpString = twosComplement(binaryString2);
//        String res = binaryAddition32bit(binaryString1, tmpString);
//        return res;
//    }

    public static String binarySubtraction32bit(String binaryString1, String binaryString2) {
        // binaryString1 - binaryString2
        int value1 = convertBin32ToDec(binaryString1);
        int value2 = convertBin32ToDec(binaryString2);

        int res = value1 - value2;

        return convertDecToBin32(res);
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


    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }




}
