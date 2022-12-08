
public class Stack {
    public int[][] byteArray;
    public int pointer = 0;

    Stack() {
        // 4096 bytes Stack
        this.byteArray = new int[4096][8];
    }

    public void push(String binaryString) {
        // Check if stack is full
        if(pointer + 4 >= 4096) {
            System.out.println("Error: can't push to Stack: Stack is Full");
        }

        // Write in stack
        int byteIndex = 0;
        for (int i = 0; i < 32; i++) {
            byteArray[pointer + byteIndex][i%8] = Character.getNumericValue(binaryString.charAt(i));
            if (i%8 == 7)
                byteIndex++;
        }

        // Increment pointer
        pointer += 4;
    }
    public String pop() {
        // Check if stack is empty
        if (pointer <= 0) {
            pointer = 0;
            System.out.println("Error: can't pop Stack : Stack is empty");
            return "11111111111111111111111111111111";
        }

        // Get a binaryString from the Stack
        StringBuilder binaryString = new StringBuilder();
        int byteIndex = 0;
        for (int i = 0; i < 32; i++) {
            binaryString.append(byteArray[pointer-4 + byteIndex][i % 8]);
            if (i%8 == 7)
                byteIndex++;
        }
        // Decrement pointer
        pointer -= 4;

        return binaryString.toString();
    }

    public void print() {
        if (pointer == 0) {
            System.out.println("Stack is empty");
            int byteIndex = 0;
            System.out.printf("%2d: ", 1);
            for (int i = 0; i < 4 * 8; i++) {
                System.out.print(byteArray[byteIndex][i%8] + " ");
                if(i%8 == 7) {
                    byteIndex++;
                    System.out.println();
                    if (i != ((4*8)-1))
                        System.out.printf("%2s: ", (byteIndex+1));
                }
            }
            System.out.println("Pointer: " + pointer);
            System.out.println();
        }
        else  {
            int byteIndex = 0;
            System.out.printf("%2d: ", 1);
            for (int i = 0; i < pointer * 8; i++) {
                System.out.print(byteArray[byteIndex][i%8] + " ");
                if(i%8 == 7) {
                    byteIndex++;
                    System.out.println();
                    if (i != ((pointer*8)-1))
                        System.out.printf("%2s: ", (byteIndex+1));
                }

            }
            System.out.println("Pointer: " + pointer);
            System.out.println();
            System.out.println("TODO : print numbers");
            System.out.println(getInfo());
            System.out.println();
        }
    }
    public String getInfo() {
        StringBuilder res = new StringBuilder();

        int tmpPointer = pointer;
        while((tmpPointer - 4) > 0) {
            res.append(getNumber(tmpPointer));
            res.append(" ");
            tmpPointer -= 4;
        }
        return res.toString();
    }
    public int getNumber(int pointer) {
        if (pointer == 0) {
            System.out.println("Error getNumber: stack is empty");
            System.exit(1);
        }
        StringBuilder number = new StringBuilder();
        int byteIndex = 0;
        for (int i = (pointer-4) * 8; i < pointer * 8; i++) {
            number.append(byteArray[byteIndex][i%8]);
             if(i%8 == 7)
                byteIndex++;
        }
        return Tools.convertBin32ToDec(number.toString());
    }
}

