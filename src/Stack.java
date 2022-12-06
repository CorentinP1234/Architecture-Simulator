
public class Stack {
    public int[][] bitArray;
    public int pointer = 0;

    Stack() {
        // 4096 bytes Stack
        this.bitArray = new int[4096][8];
    }

    public void push(String binaryString) {
        // Check if stack is full
        if(pointer + 32 >= 4096) {
            System.out.println("Error: can't push to Stack: Stack is Full");
        }

        // Write in stack
        int byteIndex = 0;
        for (int i = 0; i < 32; i++) {
            bitArray[pointer + byteIndex][i%8] = binaryString.charAt(i);
            if (i%8 == 7)
                byteIndex++;
        }

        // Increment pointer
        pointer += 4;
    }
    public String pop() {
        // Check if stack is empty
        if (pointer <= 0) {
            System.out.println("Error: can't pop Stack : Stack is empty");
        }

        // Get a binaryString from the Stack
        StringBuilder binaryString = new StringBuilder();
        int byteIndex = 0;
        for (int i = 0; i < 32; i++) {
            binaryString.append(bitArray[pointer + byteIndex][i % 8]);
            if (i%8 == 7)
                byteIndex++;
        }
        // Decrement pointer
        pointer -= 4;

        return binaryString.toString();
    }

}

