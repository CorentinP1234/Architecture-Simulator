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

        // Return a 32-bit format by adding 0 before the first significant bit
        return String.format("%32s", binary).replace(" ", "0");
    }

    public static String twosComplement(String bin) {

        StringBuilder stringBuilder = new StringBuilder(bin);

        // Traverse the string to get first '1' from the last of string
        int i;
        for (i = 31; i >=0; i--) {
            if (bin.charAt(i) == '1')
                break;
        }

        if (i == -1) {
            return bin;
        }

        // Continue traversal after the position of first '1'
        for (int k = i-1; k >= 0; k--) {
            // flip the values
            if (stringBuilder.charAt(k) == '1')
                stringBuilder.setCharAt(k, '0');
            else
                stringBuilder.setCharAt(k, '1');

        }

        return stringBuilder.toString();
    }



    public static String binaryAddition32bit(String binaryString1, String binaryString2) {
        int value1 = convertBin32ToDec(binaryString1);
        int value2 = convertBin32ToDec(binaryString2);

        int res = value1 + value2;

        return convertDecToBin32(res);
    }

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
