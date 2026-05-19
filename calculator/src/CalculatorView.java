import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Calculator View - UI display and user interaction
 */
public class CalculatorView extends JFrame {
    private JTextField displayField;
    private JButton[] numberButtons;
    private JButton[] operatorButtons;
    private JButton[] functionButtons;
    private JButton[] scienceButtons;

    private final String[] NUMBER_LABELS = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0"};
    private final String[] OPERATOR_LABELS = {"+", "-", "×", "÷", "="};
    private final String[] FUNCTION_LABELS = {"C", "CE", "←", "±", "."};
    private final String[] SCIENCE_LABELS = {"(", ")", "sin", "cos", "tan", "ln", "log"};

    public CalculatorView() {
        initialize();
    }

    private void initialize() {
        setTitle("Calculator");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(30, 30, 30));
        setContentPane(mainPanel);

        addDisplayArea(mainPanel);
        addButtonArea(mainPanel);
    }

    private void addDisplayArea(JPanel parent) {
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(40, 40, 40));
        displayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        displayField = new JTextField("0");
        displayField.setEditable(false);
        displayField.setBackground(new Color(40, 40, 40));
        displayField.setForeground(Color.WHITE);
        displayField.setFont(new Font("Microsoft YaHei", Font.PLAIN, 48));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setBorder(null);
        displayPanel.add(displayField, BorderLayout.CENTER);

        parent.add(displayPanel, BorderLayout.NORTH);
    }

    private void addButtonArea(JPanel parent) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout(5, 5));
        buttonPanel.setBackground(new Color(30, 30, 30));

        createButtons();

        JPanel sciencePanel = new JPanel();
        sciencePanel.setLayout(new GridLayout(2, 4, 5, 5));
        sciencePanel.setBackground(new Color(30, 30, 30));
        int btnCount = 0;
        for (JButton btn : scienceButtons) {
            sciencePanel.add(btn);
            btnCount++;
        }
        for (int i = btnCount; i < 8; i++) {
            JLabel emptyLabel = new JLabel();
            emptyLabel.setBackground(new Color(30, 30, 30));
            sciencePanel.add(emptyLabel);
        }
        buttonPanel.add(sciencePanel, BorderLayout.NORTH);

        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        mainButtonPanel.setBackground(new Color(30, 30, 30));

        mainButtonPanel.add(functionButtons[0]);
        mainButtonPanel.add(functionButtons[1]);
        mainButtonPanel.add(functionButtons[2]);
        mainButtonPanel.add(operatorButtons[3]);

        mainButtonPanel.add(numberButtons[0]);
        mainButtonPanel.add(numberButtons[1]);
        mainButtonPanel.add(numberButtons[2]);
        mainButtonPanel.add(operatorButtons[2]);

        mainButtonPanel.add(numberButtons[3]);
        mainButtonPanel.add(numberButtons[4]);
        mainButtonPanel.add(numberButtons[5]);
        mainButtonPanel.add(operatorButtons[1]);

        mainButtonPanel.add(numberButtons[6]);
        mainButtonPanel.add(numberButtons[7]);
        mainButtonPanel.add(numberButtons[8]);
        mainButtonPanel.add(operatorButtons[0]);

        mainButtonPanel.add(functionButtons[3]);
        mainButtonPanel.add(numberButtons[9]);
        mainButtonPanel.add(functionButtons[4]);
        mainButtonPanel.add(operatorButtons[4]);

        buttonPanel.add(mainButtonPanel, BorderLayout.CENTER);

        parent.add(buttonPanel, BorderLayout.CENTER);
    }

    private void createButtons() {
        numberButtons = new JButton[NUMBER_LABELS.length];
        for (int i = 0; i < NUMBER_LABELS.length; i++) {
            numberButtons[i] = createButton(NUMBER_LABELS[i], Color.WHITE, new Color(60, 60, 60));
        }

        operatorButtons = new JButton[OPERATOR_LABELS.length];
        for (int i = 0; i < OPERATOR_LABELS.length; i++) {
            operatorButtons[i] = createButton(OPERATOR_LABELS[i], Color.WHITE, new Color(255, 150, 0));
        }

        functionButtons = new JButton[FUNCTION_LABELS.length];
        for (int i = 0; i < FUNCTION_LABELS.length; i++) {
            functionButtons[i] = createButton(FUNCTION_LABELS[i], Color.BLACK, new Color(200, 200, 200));
        }

        scienceButtons = new JButton[SCIENCE_LABELS.length];
        for (int i = 0; i < SCIENCE_LABELS.length; i++) {
            scienceButtons[i] = createButton(SCIENCE_LABELS[i], Color.WHITE, new Color(60, 100, 200));
        }
    }

    private JButton createButton(String label, Color textColor, Color bgColor) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setBorder(null);
        button.setFocusPainted(false);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    public void setDisplayValue(String value) {
        int maxDisplayLength = 20;
        int baseFontSize = 36;
        int maxLengthForNormalSize = 12;
        
        if (value.length() > maxDisplayLength) {
            displayField.setText("Number too large");
            displayField.setFont(new Font("Microsoft YaHei", Font.PLAIN, baseFontSize));
        } else if (value.length() <= maxLengthForNormalSize) {
            displayField.setText(value);
            displayField.setFont(new Font("Microsoft YaHei", Font.PLAIN, baseFontSize));
        } else {
            int fontSize = Math.max(14, baseFontSize - (value.length() - maxLengthForNormalSize) * 2);
            displayField.setText(value);
            displayField.setFont(new Font("Microsoft YaHei", Font.PLAIN, fontSize));
        }
    }

    public String getDisplayValue() {
        return displayField.getText();
    }

    public void addNumberButtonListener(ActionListener listener) {
        for (JButton btn : numberButtons) {
            btn.addActionListener(listener);
        }
    }

    public void addOperatorButtonListener(ActionListener listener) {
        for (JButton btn : operatorButtons) {
            btn.addActionListener(listener);
        }
    }

    public void addFunctionButtonListener(ActionListener listener) {
        for (JButton btn : functionButtons) {
            btn.addActionListener(listener);
        }
    }

    public void addScienceButtonListener(ActionListener listener) {
        for (JButton btn : scienceButtons) {
            btn.addActionListener(listener);
        }
    }

    public String getNumberLabel(int index) {
        return NUMBER_LABELS[index];
    }

    public String getOperatorLabel(int index) {
        return OPERATOR_LABELS[index];
    }

    public String getFunctionLabel(int index) {
        return FUNCTION_LABELS[index];
    }

    public String getScienceLabel(int index) {
        return SCIENCE_LABELS[index];
    }
}