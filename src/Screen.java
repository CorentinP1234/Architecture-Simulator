import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Screen extends JFrame{

    public JPanel panelMain;
    public JPanel File;
    public JPanel MemoryInfo;
    public JPanel Buttons;
    public JPanel Registers;
    public JScrollPane codeScrollPane;
    public JTextArea codeArea;
    private JLabel fileNameLabel;
    private JLabel t0Label;
    private JLabel t1Label;
    private JLabel t2Label;
    private JLabel t3Label;
    private JTextArea memoryText;
    private JPanel thirdPanel;
    private JTextArea stackText;
    private JButton loadFileButton;
    private JButton simulateButton;
    private JButton stepSimulationButton;
    private JTextPane codeText;
    private JLabel nextInstructionLabel;
    private JLabel fileName;
    private JLabel nextInstruction;
    File file;
    ALU alu;
    ArrayList<String> lines;
    ArrayList<String> codeLines;
    int PC;
    boolean commandPrompt = false;

    public static void main(String[] args) {
        Screen screen = new Screen();
        screen.setVisible(true);
    }



    public Screen() {
        super("Architecture Simulator");
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(800, 800);

        t0Label.setText("T0 : " + 0);
        t1Label.setText("T1 : " + 0);
        t2Label.setText("T2 : " + 0);
        t3Label.setText("T3 : " + 0);

        loadFileButton.setText("Load File");
        simulateButton.setText("Simulate");
        stepSimulationButton.setText("Step Simulation");

        loadFileButton.addActionListener(e -> {
            JFileChooser dialogue = new JFileChooser("./");

            dialogue.showOpenDialog(null);

            file =  dialogue.getSelectedFile();
            fileName.setText(" " + file.getName());

            try {
                lines = FileReader.readFile_withoutComments(file.getName());
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }

            alu = new ALU();
            alu.stack = new Stack();
            alu.memory = new Memory();
            Memory.NameToAddressMap = new HashMap<>();
            PC = 0;

            memoryText.setText(getDataLines(lines, alu.memory));
            codeText.setText(getCodeLines(lines));
        });

        simulateButton.addActionListener(e -> {
            ArrayList<String> codeLines = FileReader.getCodeSection(lines);
            // If PC is set to -1 it means that stepExecution has read "HLT"
            while (PC != -1) {
                PC = Main.stepExecution(codeLines, alu, PC, false);
            }
//                readCodeSection(codeLines, alu);
            update(alu);
        });

        stepSimulationButton.addActionListener(e -> {
            if (PC != -1){
                 PC = stepExecution(codeLines, alu, PC);
                update(alu);
            }
        });
    }

     public static int stepExecution(ArrayList<String> codeLines, ALU alu, int PC) {
        if (PC == -1)
            return -1;
        String currentLine = codeLines.get(PC);
        boolean isLastInstruction = Objects.equals(currentLine, "HLT");

        if (!Objects.equals(currentLine, "HLT")) {
            PC = Instruction.parseInstructions(currentLine, alu, PC);
            PC++;
        }

        if (isLastInstruction) {
            return -1;
        }
        else
            return PC;
    }

    public void update(ALU alu) {
        memoryText.setText(alu.memory.getAllVar());
        if (PC != -1)
            nextInstruction.setText(codeLines.get(PC));
        else
            nextInstruction.setText("HLT");
        stackText.setText(alu.stack.getInfo());
        t0Label.setText("T0 : " + Tools.convertBin32ToDec(alu.t0.read()));
        t1Label.setText("T1 : " + Tools.convertBin32ToDec(alu.t1.read()));
        t2Label.setText("T2 : " + Tools.convertBin32ToDec(alu.t2.read()));
        t3Label.setText("T3 : " + Tools.convertBin32ToDec(alu.t3.read()));
    }

    public String getCodeLines(ArrayList<String> lines) {
        codeLines = FileReader.getCodeSection(lines);
        boolean isFirstLine = true;
        StringBuilder stringBuilder = new StringBuilder();
        for(String line : codeLines) {
            if (Objects.equals(line, "#CODE"))
                continue;
            if (isFirstLine) {
                nextInstruction.setText(line);
                isFirstLine = false;
            }

            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

    public String getDataLines(ArrayList<String> lines, Memory memory) {
        ArrayList<String> dataLines = FileReader.getDataSection(lines);
        Main.readDataSection(dataLines, memory);
        return memory.getAllVar();
    }
}

