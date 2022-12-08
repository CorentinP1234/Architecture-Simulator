public class Instruction {

    public static int parseInstructions(String line, ALU alu, int PC) {
        String[] lineElements = line.split(" ");
        int numberOfArgument = lineElements.length - 1;

        // Check for label
        if (numberOfArgument == 0) {

            // Label example : "LOOP:"
            int labelLength = lineElements[0].length();

            // Remove ":" at the end
            String label = lineElements[0].substring(0, labelLength-1);

            // Store in Map
            alu.labelToCodeLine.put(label, PC);
        }

        String instruction = lineElements[0];


        switch (instruction) {
            case "LDA":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.LDA(lineElements[1], lineElements[2], alu);
                break;

            case "STR":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.STR(lineElements[1], lineElements[2], alu);
                break;

            case "PUSH":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line))
                    Instruction.PUSH(lineElements[1], alu);
                break;

            case "POP":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line))
                    Instruction.POP(lineElements[1], alu);
                break;

            case "AND":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.AND(lineElements[1], lineElements[2], alu);
                break;

            case "OR":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.OR(lineElements[1], lineElements[2], alu);
                break;

            case "NOT":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line))
                    Instruction.NOT(lineElements[1], alu);
                break;

            case "ADD":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.ADD(lineElements[1], lineElements[2], alu);
                break;

            case "SUB":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.SUB(lineElements[1], lineElements[2], alu);
                break;

            case "DIV":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.DIV(lineElements[1], lineElements[2], alu);
                break;

            case "MUL":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.MUL(lineElements[1], lineElements[2], alu);
                break;

            case "MOD":
                if (isNumberOfArgumentValid((numberOfArgument == 2), line))
                    Instruction.MOD(lineElements[1], lineElements[2], alu);
                break;

            case "INC":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line))
                    Instruction.INC(lineElements[1], alu);
                break;

            case "DEC":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line))
                    Instruction.DEC(lineElements[1], alu);
                break;

            case "BEQ":
                if (isNumberOfArgumentValid((numberOfArgument == 3), line))
                    return Instruction.BEQ(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "BNE":
                if (isNumberOfArgumentValid((numberOfArgument == 3), line))
                    return Instruction.BNE(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "BBG":
                if (isNumberOfArgumentValid((numberOfArgument == 3), line))
                    return Instruction.BBG(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "BSM":
                if (isNumberOfArgumentValid((numberOfArgument == 3), line))
                    return Instruction.BSM(lineElements[1], lineElements[2], lineElements[3], PC, alu);
                break;

            case "JMP":
                if (isNumberOfArgumentValid((numberOfArgument == 1), line)) {
                    return Instruction.JMP(lineElements[1], alu);
                }
                break;
        }
        return PC;
    }

    public static boolean isNumberOfArgumentValid(boolean comparison, String line) {
        if (comparison)
            return true;
        else {
            System.out.println("\nError: invalid number of argument:");
            System.out.println("In line: " + line);
            System.exit(1);
            return false;
        }
    }
    // 1. LDA <reg1> <reg2>/<var>/<const>
    public static void LDA(String param1, String param2, ALU alu) {

        Register reg1;

        // Select register (param1)
        if (Register.isRegisterName(param1)) {

            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if Second parameter is a Register
            if (Register.isRegisterName(param2)) {
                Register reg2 = Register.selectRegisterByName(param2, alu);
                alu.LDA(reg1, reg2);
            }

            // Check if the Second parameter is a constant
            else if(Tools.isNumeric(param2)) {
                int decimalConstant = Integer.parseInt(param2);
                alu.LDA(reg1, decimalConstant);
            }

            // Second parameter is a Variable
            else {

                // Check if variable exists
                if(Memory.isVarName(param2)) {
                    alu.LDA(reg1, param2);
                }
                else {
                    System.out.println("Error: variable does not exist:");
                    System.out.println("Variable: " + param2);
                }

            }
        }
        else {
            System.out.println("Error LDA: invalid argument: first parameter is a register");
            System.out.println("Name given: " + param1);
            System.exit(1);
        }

        // Check if the Second parameter is a register

    }

    // 2. STR <var> <reg>/<const>
    public static void STR(String param1, String param2, ALU alu) {

        // Check if param 1 is a variable
        if(Memory.isVarName(param1)) {

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                Register reg2 = Register.selectRegisterByName(param2, alu);
                alu.STR(param1, reg2);
            }

            // Check if param2 is a constant
            else if(Tools.isNumeric(param2)) {
                int decimalConstant = Integer.parseInt(param2);
                alu.STR(param1, decimalConstant);
            }
            else {
                System.out.println("Error in STR: invalid argument");
                System.out.println("Argument: " + param2);
            }
        }
    }

    // 3. PUSH <reg>/<var>/<const>
    public static void PUSH(String param1, ALU alu) {
        Register reg1;
        int decimalConstant;

        // Check if param1 is a register
        if (Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);
            alu.PUSH(reg1);
        }

        // Check if param1 is a constant
        else if (Tools.isNumeric(param1)) {
            decimalConstant = Integer.parseInt(param1);
            alu.PUSH(decimalConstant);
        }

        // Check if param1 is a variable
        else if (Memory.isVarName(param1)) {
            alu.PUSH(param1);
        }
    }
    // 4. POP <reg>
    public static void POP(String param1, ALU alu) {
        Register reg1;

        // Check if param1 is a register
        if (Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);
            alu.POP(reg1);
        }
        else {
            System.out.println("Error: POP only takes a register as parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }
    // 5. AND <reg1> <reg2>/<var>/<const>
    public static void AND(String param1, String param2, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                alu.AND(reg1, reg2);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                alu.AND(reg1, decimalConstant);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                alu.AND(reg1, param2);
            }
        }
        else {
            System.out.println("Error: AND takes a register as first parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }

    // 6. OR <reg1> <reg2>/<var>/<const>
    public static void OR(String param1, String param2, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                alu.OR(reg1, reg2);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                alu.OR(reg1, decimalConstant);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                alu.OR(reg1, param2);
            }
        }
        else {
            System.out.println("Error: OR takes a register as first parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }

    // 7. NOT <reg>
    public static void NOT(String param1, ALU alu) {
        Register reg1;

        if (Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);
            alu.NOT(reg1);
        }
        else {
            System.out.println("Error: NOT takes a register as first parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }

    //8. ADD <reg1> <reg2>/<var>/<const>
    public static void ADD(String param1, String param2, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                alu.ADD(reg1, reg2);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                alu.ADD(reg1, decimalConstant);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                alu.ADD(reg1, param2);
            }
        }
        else {
            System.out.println("Error: ADD takes a register as first parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }

    // 9. SUB <reg1> <reg2>/<var>/<const>
    public static void SUB(String param1, String param2, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                alu.SUB(reg1, reg2);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                alu.SUB(reg1, decimalConstant);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                alu.SUB(reg1, param2);
            }
        }
        else {
            System.out.println("Error: SUB takes a register as first parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }

    // 10. DIV <reg1> <reg2>/<var>/<const>
    public static void DIV(String param1, String param2, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                alu.DIV(reg1, reg2);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                alu.DIV(reg1, decimalConstant);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                alu.DIV(reg1, param2);
            }
        }
        else {
            System.out.println("Error: DIV takes a register as first parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }

    // 11. MUL <reg1> <reg2>/<var>/<const>
    public static void MUL(String param1, String param2, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                alu.MUL(reg1, reg2);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                alu.MUL(reg1, decimalConstant);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                alu.MUL(reg1, param2);
            }
        }
        else {
            System.out.println("Error: MUL takes a register as first parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }

    // 12. MOD <reg1> <reg2>/<var>/<const>
    public static void MOD(String param1, String param2, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                alu.MOD(reg1, reg2);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                alu.MOD(reg1, decimalConstant);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                alu.MOD(reg1, param2);
            }
        }
        else {
            System.out.println("Error: MOD takes a register as first parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }

    // 13. INC <reg>
    public static void INC(String param1, ALU alu) {
        Register reg1;

        if (Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);
            alu.INC(reg1);
        }
        else {
            System.out.println("Error: INC takes a register as first parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }

    // 14. DEC <reg>
    public static void DEC(String param1, ALU alu) {
        Register reg1;

        if (Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);
            alu.DEC(reg1);
        }
        else {
            System.out.println("Error: INC takes a register as first parameter");
            System.out.println("Argument given: " + param1);
            System.exit(1);
        }
    }
    // 15. BEQ <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    public static int BEQ(String param1, String param2, String LABEL, int PC, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BEQ(reg1, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BEQ(reg1, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BEQ(reg1, param2, LABEL, PC);
            }
        }

        // Check if param1 is a constant
        else if (Tools.isNumeric(param1)) {
            decimalConstant = Integer.parseInt(param1);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BEQ(decimalConstant, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BEQ(decimalConstant, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BEQ(decimalConstant, param2, LABEL, PC);
            }
        }

        // Check if param1 is a variable
        else if (Memory.isVarName(param1)) {

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BEQ(param1, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BEQ(param1, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BEQ(param1, param2, LABEL, PC);
            }
        }
        return PC;
    }

    // 16. BNE <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    public static int BNE(String param1, String param2, String LABEL, int PC, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BNE(reg1, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BNE(reg1, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BNE(reg1, param2, LABEL, PC);
            }
        }

        // Check if param1 is a constant
        else if (Tools.isNumeric(param1)) {
            decimalConstant = Integer.parseInt(param1);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BNE(decimalConstant, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BNE(decimalConstant, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BNE(decimalConstant, param2, LABEL, PC);
            }
        }

        // Check if param1 is a variable
        else if (Memory.isVarName(param1)) {

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BNE(param1, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BNE(param1, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BNE(param1, param2, LABEL, PC);
            }
        }
        return PC;
    }
    // 17. BBG <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    public static int BBG(String param1, String param2, String LABEL, int PC, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BBG(reg1, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BBG(reg1, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BBG(reg1, param2, LABEL, PC);
            }
        }

        // Check if param1 is a constant
        else if (Tools.isNumeric(param1)) {
            decimalConstant = Integer.parseInt(param1);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BBG(decimalConstant, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BBG(decimalConstant, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BBG(decimalConstant, param2, LABEL, PC);
            }
        }

        // Check if param1 is a variable
        else if (Memory.isVarName(param1)) {

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BBG(param1, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BBG(param1, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BBG(param1, param2, LABEL, PC);
            }
        }
        return PC;
    }

    // 18. BSM <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
     public static int BSM(String param1, String param2, String LABEL, int PC, ALU alu) {
        Register reg1;
        Register reg2;
        int decimalConstant;

        // Check if param1 is a register
        if(Register.isRegisterName(param1)) {
            reg1 = Register.selectRegisterByName(param1, alu);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BSM(reg1, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BSM(reg1, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BSM(reg1, param2, LABEL, PC);
            }
        }

        // Check if param1 is a constant
        else if (Tools.isNumeric(param1)) {
            decimalConstant = Integer.parseInt(param1);

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BSM(decimalConstant, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BSM(decimalConstant, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BSM(decimalConstant, param2, LABEL, PC);
            }
        }

        // Check if param1 is a variable
        else if (Memory.isVarName(param1)) {

            // Check if param2 is a register
            if (Register.isRegisterName(param2)) {
                reg2 = Register.selectRegisterByName(param2, alu);
                return alu.BSM(param1, reg2, LABEL, PC);
            }

            // Check if param2 is a constant
            else if (Tools.isNumeric(param2)) {
                decimalConstant = Integer.parseInt(param2);
                return alu.BSM(param1, decimalConstant, LABEL, PC);
            }

            // Check if param2 is a variable
            else  if (Memory.isVarName(param2)) {
                return alu.BSM(param1, param2, LABEL, PC);
            }
        }
        return PC;
    }

    /// 19. JMP <LABEL>
    public static int JMP(String LABEL, ALU alu) {
        return alu.JMP(LABEL);
    }
}
