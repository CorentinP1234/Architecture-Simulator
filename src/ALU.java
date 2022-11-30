public class ALU {
    public Register t0;
    public Register t1;
    public Register t2;
    public Register t3;

    public Memory memory = null;
    public Stack stack = null;
    ALU() {
        this.t0 = new Register("t0");
        this.t1 = new Register("t1");
        this.t2 = new Register("t2");
        this.t3 = new Register("t3");
    }
    // TEST

    // 1. LDA <reg1> <reg2>/<var>/<const>
    // Load register reg1 with the contents of either the contents of reg2, or the memory var or a constant
    // const. Memory regions loads (load into a variable, for instance) are NOT ALLOWED.

    // !!
    // Pas sur de bien comprendre cette phrase:
    // Memory regions loads (load into a variable, for instance) are NOT ALLOWED.
    // !!
    public void LDA(Register reg1, Register reg2) {

        // Copy each bit of reg2 into reg1
        System.arraycopy(reg1.bitArray, 0, reg2.bitArray, 0, 32);
    }

    public void LDA(Register reg1, int constant) {
        reg1.setFromDecimal(constant);
    }

    public void LDA(Register reg1, String variable) {
        String binaryString =  memory.getBinaryStringFromName(variable);

        // Copy each bit
        for (int i = 0; i < 32; i++) {
            reg1.bitArray[i] = binaryString.charAt(i);
        }
    }
    // -------------------------------------------------------------------------------------------------


    // 2. STR <var> <reg>/<const>
    // Store in the memory position referred by var the value of register reg or a constant const. Register
    // stores (store into register t0, for instance) are NOT ALLOWED.

    // !!
    // Pas sur de bien comprendre cette phrase:
    // Register stores (store into register t0, for instance) are NOT ALLOWED.
    // !!

    public void STR(String variableName, Register reg) {
        String binaryString = reg.getBinaryString();
        memory.setValueFromName(binaryString, variableName);
    }

    public void STR(String variableName, int constant) {
        int value = memory.NameToAddressMap.get(variableName);
        String binaryString = Tools.convertDecimalTo32BitString(value);
        memory.setValueFromName(binaryString, variableName);
    }

    // -------------------------------------------------------------------------------------------------

    // 3.PUSH <reg>/<var>/<const>
    // Push to the top of the stack the contents of reg or var or a constant const
    public void PUSH(Register reg) {
        String binaryString = reg.getBinaryString();
        stack.push(binaryString);
    }

    public void PUSH(String variableName) {
        String binaryString = memory.getBinaryStringFromName(variableName);
        stack.push(binaryString);
    }

    public void PUSH(int constant) {
        String binaryString = Tools.convertDecimalTo32BitString(constant);
        stack.push(binaryString);
    }

    // -------------------------------------------------------------------------------------------------

    // POP <reg>
    // Pop from the top of the stack and store the value on reg. Storing in a memory region is NOT
    // ALLOWED.
    public String POP(Register reg) {
        String binaryString = stack.pop();
        return binaryString;
    }

    // AND <reg1> <reg2>/<var>/<const>
    //Performs a logical AND operation between reg1 and a register reg2, a variable var or a constant
    //const, and store the result on register reg1. Memory regions stores (store result into a variable, for
    //instance) are NOT ALLOWED.
    public void AND() {

    }

    // OR <reg1> <reg2>/<var>/<const>
    // Performs a logical OR operation between reg1 and a register reg2, a variable var or a constant
    // const, and store the result on register reg1. Memory regions stores (store result into a variable, for
    // instance) are NOT ALLOWED.
    public void OR() {

    }

}
