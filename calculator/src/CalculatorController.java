import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Calculator Controller - Free expression input mode
 * User enters a complete expression and presses equals to calculate
 */
public class CalculatorController {
    private CalculatorModel model;
    private CalculatorView view;
    private StringBuilder currentExpression;
    private boolean isNewExpression;

    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;
        this.currentExpression = new StringBuilder();
        this.isNewExpression = true;
        
        initListeners();
        updateDisplay();
    }

    private void initListeners() {
        view.addNumberButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                appendToExpression(command);
            }
        });

        view.addOperatorButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if ("=".equals(command)) {
                    calculateExpression();
                } else {
                    appendToExpression(command);
                }
            }
        });

        view.addFunctionButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                handleFunctionInput(command);
            }
        });

        view.addScienceButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                appendToExpression(command);
            }
        });

        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyInput(e);
            }
        });
        
        view.setFocusable(true);
        view.requestFocusInWindow();
    }

    private void appendToExpression(String text) {
        if (isNewExpression) {
            currentExpression = new StringBuilder();
            isNewExpression = false;
        }
        currentExpression.append(text);
        updateDisplay();
    }

    private void handleFunctionInput(String function) {
        switch (function) {
            case "C":
                currentExpression = new StringBuilder();
                isNewExpression = true;
                model.clear();
                updateDisplay();
                break;
            case "CE":
                break;
            case "←":
                if (currentExpression.length() > 0) {
                    currentExpression.setLength(currentExpression.length() - 1);
                }
                updateDisplay();
                break;
            case "±":
                appendToExpression("-");
                break;
            case ".":
                appendToExpression(".");
                break;
            default:
                break;
        }
    }

    private void calculateExpression() {
        if (currentExpression.length() == 0) {
            return;
        }
        
        String expression = currentExpression.toString();
        model.evaluateExpression(expression);
        
        String result = model.getDisplayValue();
        view.setDisplayValue(result);
        
        isNewExpression = true;
        currentExpression = new StringBuilder(result);
    }

    private void handleKeyInput(KeyEvent e) {
        char keyChar = e.getKeyChar();
        
        if (Character.isDigit(keyChar)) {
            appendToExpression(String.valueOf(keyChar));
        } else if (keyChar == '.') {
            appendToExpression(".");
        } else if (keyChar == '+') {
            appendToExpression("+");
        } else if (keyChar == '-') {
            appendToExpression("-");
        } else if (keyChar == '*') {
            appendToExpression("×");
        } else if (keyChar == '/') {
            appendToExpression("÷");
        } else if (keyChar == '=') {
            calculateExpression();
        } else if (keyChar == '\n') {
            calculateExpression();
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (currentExpression.length() > 0) {
                currentExpression.setLength(currentExpression.length() - 1);
                updateDisplay();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            currentExpression = new StringBuilder();
            isNewExpression = true;
            model.clear();
            updateDisplay();
        } else if (keyChar == '(') {
            appendToExpression("(");
        } else if (keyChar == ')') {
            appendToExpression(")");
        }
    }

    private void updateDisplay() {
        String display = currentExpression.toString();
        if (display.isEmpty()) {
            display = "0";
        }
        view.setDisplayValue(display);
    }
}