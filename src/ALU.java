import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ALU {
    public Register t0;
    public Register t1;
    public Register t2;
    public Register t3;

    public Memory memory = null;
    public Stack stack = null;

    public Map<String, Integer> labelToCodeLine = new HashMap<>();
    ALU() {
        this.t0 = new Register("t0");
        this.t1 = new Register("t1");
        this.t2 = new Register("t2");
        this.t3 = new Register("t3");
    }

    // 1. LDA <reg1> <reg2>/<var>/<const>
    //    Load register reg1 with the contents of either the contents of reg2, or the memory var or a constant
    //    const. Memory regions loads (load into a variable, for instance) are NOT ALLOWED.

    public void LDA(Register reg1, Register reg2) {

        // Copy each bit of reg2 into reg1
        System.arraycopy(reg1.bitArray, 0, reg2.bitArray, 0, 32);
    }

    public void LDA(Register reg1, int constant) {

        // Convert the decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Write it in reg1
        reg1.write(binConstant);
    }

    public void LDA(Register reg1, String variable) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable =  memory.readFromName(variable);

        // Write in register 1
        reg1.write(binVariable);
    }
    // -------------------------------------------------------------------------------------------------


    // 2. STR <var> <reg>/<const>
    //    Store in the memory position referred by var the value of register reg or a constant const. Register
    //    stores (store into register t0, for instance) are NOT ALLOWED.

    public void STR(String variableName, Register reg) {

        // read the binary number from the register
        String binaryString = reg.read();

        // write the binary number at the address of the variable in memory
        memory.setValueFromName(binaryString, variableName);
    }

    public void STR(String variableName, int constant) {

        // Convert the decimal constant to binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Write the constant at the address of the variable in memory
        memory.setValueFromName(binConstant, variableName);
    }

    // -------------------------------------------------------------------------------------------------

    // 3.PUSH <reg>/<var>/<const>
    //    Push to the top of the stack the contents of reg or var or a constant const
    public void PUSH(Register reg) {

        // Read the binary number in register
        String binaryString = reg.read();

        // Write it on the stack
        stack.push(binaryString);
    }

    public void PUSH(String variableName) {

        // Fetch memory to get the binary representation of a variable by its name
        String binaryString = memory.readFromName(variableName);

        // Write the variable on the stack
        stack.push(binaryString);
    }

    public void PUSH(int constant) {

        // Convert a decimal value to binary
        String binaryString = Tools.convertDecToBin32(constant);

        // Write the constant on the stack
        stack.push(binaryString);
    }

    // -------------------------------------------------------------------------------------------------

    // 4.POP <reg>
    //    Pop from the top of the stack and store the value on reg. Storing in a memory region is NOT
    //    ALLOWED.
    public void POP(Register reg) {

        // Get the last binary number added to the stack
        String binary = stack.pop();

        // Write in reg
        reg.write(binary);
    }

    // 5.AND <reg1> <reg2>/<var>/<const>
    //    Performs a logical AND operation between reg1 and a register reg2, a variable var or a constant
    //    const, and store the result on register reg1. Memory regions stores (store result into a variable, for
    //    instance) are NOT ALLOWED.
    public void AND(Register reg1, Register reg2) {

        // AND operation on the two registers
        String result = Tools.AndOperation(reg1.read(), reg2.read());

        // Write the result on reg1
        reg1.write(result);
    }
    public void AND(Register reg1, String variableName) {

        // Fetch memory to get the binary representation of a variable by its name
        String binaryVariable = memory.readFromName(variableName);

        // AND operation on the register and the variable
        String result = Tools.AndOperation(reg1.read(), binaryVariable);

        // Write the result on reg1
        reg1.write(result);

    }
    public void AND(Register reg1, int constant) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // AND operation on the register and the constant
        String result = Tools.AndOperation(reg1.read(), binConstant);

        // Write the result on reg1
        reg1.write(result);
    }

    // 6.OR <reg1> <reg2>/<var>/<const>
    //    Performs a logical OR operation between reg1 and a register reg2, a variable var or a constant
    //    const, and store the result on register reg1. Memory regions stores (store result into a variable, for
    //    instance) are NOT ALLOWED.
    public void OR(Register reg1, Register reg2) {

        // OR operation on the two registers
        String result = Tools.OrOperation(reg1.read(), reg2.read());

        // Write the result on reg1
        reg1.write(result);
    }
    public void OR(Register reg1, String variableName) {

        // Fetch memory to get the binary representation of a variable by its name
        String binaryVariable = memory.readFromName(variableName);

        // OR operation on the register and the variable
        String result = Tools.OrOperation(reg1.read(), binaryVariable);

        // Write the result on reg1
        reg1.write(result);
    }
    public void OR(Register reg1, int constant) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // OR operation on the register and the constant
        String result = Tools.OrOperation(reg1.read(), binConstant);

        // Write the result on reg1
        reg1.write(result);
    }

    // 7.NOT <reg>
    //    Performs a logical NOT operation on register reg and store the result on register reg. Memory
    //    regions stores (store result into a variable, for instance) are NOT ALLOWED.
    public void NOT(Register reg1) {
        // Read from register
        String bin = reg1.read();

        // Temporary string
        StringBuilder  stringBuilder = new StringBuilder(32);

        // NOT operation
        for(int i = 0; i < 32; i++) {
           if(bin.charAt(i) == '1')
               stringBuilder.append('1');
           else if(bin.charAt(i) == '0')
               stringBuilder.append('0');
        }

        // Write the result on register 1
        reg1.write(stringBuilder.toString());
    }

    // 8.ADD <reg1> <reg2>/<var>/<const>
    //    Performs the addition operation of reg1 and a register reg2, a variable var or a constant const, and
    //    store the result on register reg1. Memory regions stores (store result into a variable, for instance)
    //    are NOT ALLOWED.
    public void ADD(Register reg1, Register reg2) {

        // ADD operation
        String result = Tools.binaryAddition32bit(reg1.read(), reg2.read());

        // Write the result on register 1
        reg1.write(result);
    }

    public void ADD(Register reg1, String variableName) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // ADD operation
        String result = Tools.binaryAddition32bit(reg1.read(), binVariable);

        // Write the result on register 1
        reg1.write(result);
    }

    public void ADD(Register reg1, int constant) {

        // Convert the decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // ADD operation
        String result = Tools.binaryAddition32bit(reg1.read(), binConstant);

        // Write the result on register 1
        reg1.write(result);
    }

    // 9. SUB <reg1> <reg2>/<var>/<const>
    //    Performs the subtraction operation of reg1 and a register reg2, a variable var or a constant const,
    //    and store the result on register reg1. The operation is given by second argument minus the first
    //    argument (i.e., reg2 – reg1). Memory regions stores (store result into a variable, for instance) are
    //    NOT ALLOWED.
    public void SUB(Register reg1, Register reg2) {
        // SUB operation (reg2 - reg1)
        String result = Tools.binarySubtraction32bit(reg2.read(), reg1.read());

        // Write the result on register 1
        reg1.write(result);
    }
    public void SUB(Register reg1, String variableName) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // SUB operation (variable - reg1)
        String result = Tools.binarySubtraction32bit(binVariable, reg1.read());

        // Write the result on register 1
        reg1.write(result);
    }
    public void SUB(Register reg1, int constant) {

        // Convert the decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

         // SUB operation (constant - reg1)
        String result = Tools.binarySubtraction32bit(binConstant, reg1.read());

        // Write in register 1
        reg1.write(result);
    }
    // 10. DIV <reg1> <reg2>/<var>/<const>
    //    Performs the integer division operation of reg1 and a register reg2, a variable var or a constant
    //    const, and store the result on register reg1. The operation is given by second argument divided by
    //    the first argument (i.e., reg2 / reg1). Memory regions stores (store result into a variable, for
    //    instance) are NOT ALLOWED.

    public void DIV(Register reg1, Register reg2) {
        // Get decimal values from registers
        int value1 = Tools.convertBin32ToDec(reg1.read());
        int value2 = Tools.convertBin32ToDec(reg2.read());

        // Integer division
        int result = value2 / value1;

        // Convert result in binary
        String binResult = Tools.convertDecToBin32(result);

        // Write result in register 1
        reg1.write(binResult);
    }
    public void DIV(Register reg1, String variableName) {

         // Fetch memory to get the binary representation of a variable by its name
        String binaryVariable = memory.readFromName(variableName);

        // Get decimal values
        int value1 = Tools.convertBin32ToDec(reg1.read());
        int value2 = Tools.convertBin32ToDec(binaryVariable);

        // Integer division
        int result = value2 / value1;

        // Convert result in binary
        String binResult = Tools.convertDecToBin32(result);

        // Write result in register 1
        reg1.write(binResult);
    }
    public void DIV(Register reg1, int constant) {
        // Get decimal value
        int value1 = Tools.convertBin32ToDec(reg1.read());

        // Integer division
        int result = constant / value1;

        // Convert result in binary
        String binResult = Tools.convertDecToBin32(result);

        // Write result in register 1
        reg1.write(binResult);
    }

    // 11. MUL <reg1> <reg2>/<var>/<const>
    //    Performs the integer multiplication operation of reg1 and a register reg2, a variable var or a
    //    constant const, and store the result on register reg1. Memory regions stores (store result into a
    //    variable, for instance) are NOT ALLOWED.

    public void MUL(Register reg1, Register reg2) {
        // Get decimal values from registers
        int value1 = Tools.convertBin32ToDec(reg1.read());
        int value2 = Tools.convertBin32ToDec(reg2.read());

        // Multiplication
        int result = value2 * value1;

        // Convert result in binary
        String binResult = Tools.convertDecToBin32(result);

        // Write result in register 1
        reg1.write(binResult);
    }
    public void MUL(Register reg1, String variableName) {

         // Fetch memory to get the binary representation of a variable by its name
        String binaryVariable = memory.readFromName(variableName);

        // Get decimal values
        int value1 = Tools.convertBin32ToDec(reg1.read());
        int value2 = Tools.convertBin32ToDec(binaryVariable);

        // Multiplication
        int result = value2 * value1;

        // Convert result in binary
        String binResult = Tools.convertDecToBin32(result);

        // Write result in register 1
        reg1.write(binResult);
    }
    public void MUL(Register reg1, int constant) {
        // Get decimal value
        int value1 = Tools.convertBin32ToDec(reg1.read());

        // Multiplication
        int result = constant * value1;

        // Convert result in binary
        String binResult = Tools.convertDecToBin32(result);

        // Write result in register 1
        reg1.write(binResult);
    }

    // 12. MOD <reg1> <reg2>/<var>/<const>
    //    Performs the integer modulo operation of reg1 and a register reg2, a variable var or a constant
    //    const, and store the result on register reg1. The operation is given by second argument modulo the
    //    first argument (i.e., reg2 mod reg1). Memory regions stores (store result into a variable, for
    //    instance) are NOT ALLOWED.
    public void MOD(Register reg1, Register reg2) {
        // Get decimal values from registers
        int value1 = Tools.convertBin32ToDec(reg1.read());
        int value2 = Tools.convertBin32ToDec(reg2.read());

        // Modulo
        int result = value2 % value1;

        // Convert result in binary
        String binResult = Tools.convertDecToBin32(result);

        // Write result in register 1
        reg1.write(binResult);
    }
    public void MOD(Register reg1, String variableName) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Get decimal values
        int value1 = Tools.convertBin32ToDec(reg1.read());
        int value2 = Tools.convertBin32ToDec(binVariable);

        // Modulo
        int result = value2 % value1;

        // Convert result in binary
        String binResult = Tools.convertDecToBin32(result);

        // Write result in register 1
        reg1.write(binResult);
    }
    public void MOD(Register reg1, int constant) {
        // Get decimal value
        int value1 = Tools.convertBin32ToDec(reg1.read());

        // Modulo
        int result = constant % value1;

        // Convert result in binary
        String binResult = Tools.convertDecToBin32(result);

        // Write result in register 1
        reg1.write(binResult);
    }
    // 13. INC <reg>
    //   Increments the value of a register reg. Memory increments (incrementing a variable, for instance)
    //   are NOT ALLOWED.
    public void INC(Register reg) {
        // ADD 1
        String result = Tools.binaryAddition32bit(reg.read(), "00000000000000000000000000000001");

        // Write result in register 1
        reg.write(result);
    }

    // 14. DEC <reg>
    //    Decrements the value of a register reg. Memory increments (decrementing a variable, for instance)
    //    are NOT ALLOWED.
    public void DEC(Register reg) {
        // SUB 1
        String result = Tools.binarySubtraction32bit(reg.read(), "00000000000000000000000000000001");

        // Write result in register 1
        reg.write(result);
    }

    // 15. BEQ <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    //    Performs a comparison between two values, given by registers, variables or constants. Any
    //    combination is permitted. If they are equal, jump to the address defined by the label LABEL

    // Register as first parameter
    public int BEQ(Register reg1, Register reg2, String LABEL, int PC) {

        // Compare reg1 and reg2
        if(Objects.equals(reg1.read(), reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BEQ(Register reg1, String variableName , String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(Objects.equals(reg1.read(), binVariable))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BEQ(Register reg1, int constant, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(Objects.equals(reg1.read(), binConstant))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // VariableName as first parameter
    public int BEQ(String variableName, Register reg2, String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(Objects.equals(binVariable, reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BEQ(String variableName1, String variableName2, String LABEL, int PC) {
        // Fetch memory to get the binary representation of a variable by its name
        String binVariable1 = memory.readFromName(variableName1);
        String binVariable2 = memory.readFromName(variableName2);

        // Compare reg1 and reg2
        if(Objects.equals(binVariable1, binVariable2))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BEQ(String variableName, int constant , String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable1 = memory.readFromName(variableName);

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(Objects.equals(binVariable1, binConstant))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // Constant as first parameter
    public int BEQ(int constant, Register reg2, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(Objects.equals(binConstant, reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BEQ(int constant, String variableName , String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(Objects.equals(binConstant, binVariable))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BEQ(int constant1, int constant2, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant1 = Tools.convertDecToBin32(constant1);
        String binConstant2 = Tools.convertDecToBin32(constant2);

        // Compare reg1 and reg2
        if(Objects.equals(binConstant1, binConstant2))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // 16. BNE <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    //    Performs a comparison between two values, given by registers, variables or constants. Any
    //    combination is permitted. If they are different, jump to the address defined by the label LABEL

    // Register as first parameter
    public int BNE(Register reg1, Register reg2, String LABEL, int PC) {

        // Compare reg1 and reg2
        if(!Objects.equals(reg1.read(), reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BNE(Register reg1, String variableName , String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(!Objects.equals(reg1.read(), binVariable))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BNE(Register reg1, int constant, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(!Objects.equals(reg1.read(), binConstant))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // VariableName as first parameter
    public int BNE(String variableName, Register reg2, String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(!Objects.equals(binVariable, reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BNE(String variableName1, String variableName2, String LABEL, int PC) {
        // Fetch memory to get the binary representation of a variable by its name
        String binVariable1 = memory.readFromName(variableName1);
        String binVariable2 = memory.readFromName(variableName2);

        // Compare reg1 and reg2
        if(!Objects.equals(binVariable1, binVariable2))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BNE(String variableName, int constant , String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable1 = memory.readFromName(variableName);

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(!Objects.equals(binVariable1, binConstant))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // Constant as first parameter
    public int BNE(int constant, Register reg2, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(!Objects.equals(binConstant, reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BNE(int constant, String variableName , String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(!Objects.equals(binConstant, binVariable))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BNE(int constant1, int constant2, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant1 = Tools.convertDecToBin32(constant1);
        String binConstant2 = Tools.convertDecToBin32(constant2);

        // Compare reg1 and reg2
        if(!Objects.equals(binConstant1, binConstant2))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // 17. BBG <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    //    Performs a comparison between two values, given by registers, variables or constants. Any
    //    combination is permitted. If the first parameter is bigger than the second parameter, jump to the
    //    address defined by the label LABEL

    // Register as first parameter
    public int BBG(Register reg1, Register reg2, String LABEL, int PC) {


        // Compare reg1 and reg2
        if(Tools.isBigger(reg1.read(), reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BBG(Register reg1, String variableName , String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(Tools.isBigger(reg1.read(), binVariable))
            // assign to PC the label line number
            PC = labelToCodeLine.get(LABEL);
        return PC;
    }
    public int BBG(Register reg1, int constant, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(Tools.isBigger(reg1.read(), binConstant))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // VariableName as first parameter
    public int BBG(String variableName, Register reg2, String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(Tools.isBigger(binVariable, reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BBG(String variableName1, String variableName2, String LABEL, int PC) {
        // Fetch memory to get the binary representation of a variable by its name
        String binVariable1 = memory.readFromName(variableName1);
        String binVariable2 = memory.readFromName(variableName2);

        // Compare reg1 and reg2
        if(Tools.isBigger(binVariable1, binVariable2))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BBG(String variableName, int constant , String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable1 = memory.readFromName(variableName);

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(Tools.isBigger(binVariable1, binConstant))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // Constant as first parameter
    public int BBG(int constant, Register reg2, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(Tools.isBigger(binConstant, reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BBG(int constant, String variableName , String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(Tools.isBigger(binConstant, binVariable))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BBG(int constant1, int constant2, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant1 = Tools.convertDecToBin32(constant1);
        String binConstant2 = Tools.convertDecToBin32(constant2);

        // Compare reg1 and reg2
        if(Tools.isBigger(binConstant1, binConstant2))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // 18. BSM <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    //    Performs a comparison between two values, given by registers, variables or constants. Any
    //    combination is permitted. If the first parameter is smaller than the second parameter, jump to the
    //    address defined by the label LABEL

    // Register as first parameter
    public int BSM(Register reg1, Register reg2, String LABEL, int PC) {


        // Compare reg1 and reg2
        if(Tools.isSmaller(reg1.read(), reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BSM(Register reg1, String variableName , String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(Tools.isSmaller(reg1.read(), binVariable))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BSM(Register reg1, int constant, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(Tools.isSmaller(reg1.read(), binConstant))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // VariableName as first parameter
    public int BSM(String variableName, Register reg2, String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(Tools.isSmaller(binVariable, reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BSM(String variableName1, String variableName2, String LABEL, int PC) {
        // Fetch memory to get the binary representation of a variable by its name
        String binVariable1 = memory.readFromName(variableName1);
        String binVariable2 = memory.readFromName(variableName2);

        // Compare reg1 and reg2
        if(Tools.isSmaller(binVariable1, binVariable2))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BSM(String variableName, int constant , String LABEL, int PC) {

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable1 = memory.readFromName(variableName);

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(Tools.isSmaller(binVariable1, binConstant))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }

    // Constant as first parameter
    public int BSM(int constant, Register reg2, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Compare reg1 and reg2
        if(Tools.isSmaller(binConstant, reg2.read()))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BSM(int constant, String variableName , String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant = Tools.convertDecToBin32(constant);

        // Fetch memory to get the binary representation of a variable by its name
        String binVariable = memory.readFromName(variableName);

        // Compare reg1 and reg2
        if(Tools.isSmaller(binConstant, binVariable))
            // assign to PC the label line number
            return JMP(LABEL);
        return PC;
    }
    public int BSM(int constant1, int constant2, String LABEL, int PC) {

        // Convert a decimal constant in binary
        String binConstant1 = Tools.convertDecToBin32(constant1);
        String binConstant2 = Tools.convertDecToBin32(constant2);

        // Compare reg1 and reg2
        if(Tools.isSmaller(binConstant1, binConstant2))
            return JMP(LABEL);
        return PC;
    }

    // 19. JMP <LABEL>
    //    Jump to the address defined by the label LABEL

    // assign to PC the label line number
    public int JMP(String label) {
        // Check if label exists
       if (labelToCodeLine.containsKey(label)) {
           // Return the code line corresponding to the label
           return labelToCodeLine.get(label);
       }
       else {
           // Terminate program if not
           System.out.println("Error: can't jump to " + label + " : this label does not exists");
           System.exit(1);
           return -1;
       }
    }
}








