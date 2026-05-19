import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calculator Model - Supports full expression parsing
 */
public class CalculatorModel {
    private BigDecimal result;
    private String displayValue;
    private static final int MAX_PRECISION = 15;
    private static final int MAX_SCALE = 12;

    public CalculatorModel() {
        clear();
    }

    public void clear() {
        result = BigDecimal.ZERO;
        displayValue = "0";
    }

    public void evaluateExpression(String expression) {
        try {
            if (!validateExpression(expression)) {
                displayValue = "Error";
                return;
            }
            String processed = preprocessExpression(expression);
            String[] postfix = infixToPostfix(processed);
            BigDecimal value = evaluatePostfix(postfix);
            value = roundForDisplay(value);
            result = value;
            displayValue = formatResult(value);
        } catch (Exception e) {
            displayValue = "Error";
        }
    }

    private boolean validateExpression(String expr) {
        if (expr.startsWith(".")) {
            return false;
        }
        
        String operators = "+-×÷*/";
        String[] functions = {"sin", "cos", "tan", "log", "ln", "sqrt", "abs"};
        
        for (int i = 0; i < expr.length() - 1; i++) {
            char c = expr.charAt(i);
            char next = expr.charAt(i + 1);
            
            if (operators.indexOf(c) >= 0 && next == '.') {
                return false;
            }
            
            if (c == '(' && next == '.') {
                return false;
            }
            
            if (Character.isDigit(c)) {
                for (String func : functions) {
                    if (expr.substring(i + 1).startsWith(func)) {
                        return false;
                    }
                }
            }
            
            if (operators.indexOf(c) >= 0 && operators.indexOf(next) >= 0) {
                if (next == '-' && (i == 0 || expr.charAt(i-1) == '(')) {
                    continue;
                }
                return false;
            }
        }
        
        return true;
    }

    private String preprocessExpression(String expr) {
        expr = expr.replace("×", "*");
        expr = expr.replace("÷", "/");
        expr = expr.replace("√", "sqrt");
        expr = expr.replace("π", String.valueOf(Math.PI));
        expr = expr.replace("e", String.valueOf(Math.E));
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            
            if (c == '-' && (i == 0 || expr.charAt(i-1) == '(' || 
                           expr.charAt(i-1) == '+' || expr.charAt(i-1) == '-' || 
                           expr.charAt(i-1) == '*' || expr.charAt(i-1) == '/')) {
                sb.append("(0-");
                int j = i + 1;
                while (j < expr.length()) {
                    char nextChar = expr.charAt(j);
                    if (nextChar == '+' || nextChar == '-' || nextChar == '*' || 
                        nextChar == '/' || nextChar == ')' || nextChar == '(') {
                        break;
                    }
                    sb.append(nextChar);
                    j++;
                }
                sb.append(")");
                i = j - 1;
                continue;
            }
            
            sb.append(c);
        }
        return sb.toString();
    }

    private String[] infixToPostfix(String expr) {
        Stack<String> ops = new Stack<>();
        java.util.List<String> output = new java.util.ArrayList<>();
        
        Pattern pattern = Pattern.compile("(sin|cos|tan|log|ln|sqrt|abs|\\d+\\.?\\d*|[+*/^()\\-]|pi|e)");
        Matcher matcher = pattern.matcher(expr);
        
        while (matcher.find()) {
            String token = matcher.group();
            
            if (token.matches("\\d+\\.?\\d*") || token.equals("pi") || token.equals("e")) {
                if (token.equals("pi")) {
                    output.add(String.valueOf(Math.PI));
                } else if (token.equals("e")) {
                    output.add(String.valueOf(Math.E));
                } else {
                    output.add(token);
                }
            } else if (isFunction(token)) {
                ops.push(token);
            } else if (token.equals("(")) {
                ops.push(token);
            } else if (token.equals(")")) {
                while (!ops.isEmpty() && !ops.peek().equals("(")) {
                    output.add(ops.pop());
                }
                ops.pop();
                
                if (!ops.isEmpty() && isFunction(ops.peek())) {
                    output.add(ops.pop());
                }
            } else {
                while (!ops.isEmpty() && !ops.peek().equals("(") && 
                       getPrecedence(ops.peek()) >= getPrecedence(token)) {
                    output.add(ops.pop());
                }
                ops.push(token);
            }
        }
        
        while (!ops.isEmpty()) {
            output.add(ops.pop());
        }
        
        return output.toArray(new String[0]);
    }

    private boolean isFunction(String token) {
        return token.equals("sin") || token.equals("cos") || token.equals("tan") ||
               token.equals("log") || token.equals("ln") || token.equals("sqrt") || token.equals("abs");
    }

    private int getPrecedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            case "sin":
            case "cos":
            case "tan":
            case "log":
            case "ln":
            case "sqrt":
            case "abs":
                return 4;
            default:
                return 0;
        }
    }

    private BigDecimal evaluatePostfix(String[] postfix) {
        Stack<BigDecimal> stack = new Stack<>();
        
        for (String token : postfix) {
            if (token.matches("\\d+\\.?\\d*")) {
                stack.push(new BigDecimal(token));
            } else if (isFunction(token)) {
                BigDecimal operand = stack.pop();
                stack.push(applyFunction(token, operand));
            } else {
                BigDecimal b = stack.pop();
                BigDecimal a = stack.pop();
                stack.push(applyOperator(a, b, token));
            }
        }
        
        return stack.pop();
    }

    private BigDecimal applyFunction(String func, BigDecimal operand) {
        double value = operand.doubleValue();
        double result;
        
        switch (func) {
            case "sin":
                result = Math.sin(value);
                break;
            case "cos":
                result = Math.cos(value);
                break;
            case "tan":
                result = Math.tan(value);
                break;
            case "log":
                result = Math.log10(value);
                break;
            case "ln":
                result = Math.log(value);
                break;
            case "sqrt":
                result = Math.sqrt(value);
                break;
            case "abs":
                result = Math.abs(value);
                break;
            default:
                result = value;
        }
        
        return BigDecimal.valueOf(result);
    }

    private BigDecimal applyOperator(BigDecimal a, BigDecimal b, String op) {
        switch (op) {
            case "+":
                return a.add(b);
            case "-":
                return a.subtract(b);
            case "*":
                return a.multiply(b);
            case "/":
                return a.divide(b, MAX_PRECISION, RoundingMode.HALF_EVEN);
            case "^":
                return BigDecimal.valueOf(Math.pow(a.doubleValue(), b.doubleValue()));
            default:
                return BigDecimal.ZERO;
        }
    }

    private BigDecimal roundForDisplay(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        
        try {
            double doubleValue = value.doubleValue();
            if (Double.isInfinite(doubleValue) || Double.isNaN(doubleValue)) {
                return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
        
        if (value.abs().compareTo(new BigDecimal("1E15")) > 0) {
            return value;
        }
        
        BigDecimal rounded = value.setScale(MAX_SCALE, RoundingMode.HALF_EVEN);
        
        if (rounded.scale() <= 0 || rounded.stripTrailingZeros().scale() <= 0) {
            return rounded.stripTrailingZeros();
        }
        
        String strValue = rounded.toPlainString();
        if (strValue.contains(".")) {
            int dotIndex = strValue.indexOf(".");
            String intPart = strValue.substring(0, dotIndex);
            String fracPart = strValue.substring(dotIndex + 1);
            
            if (fracPart.length() >= 6) {
                String firstSix = fracPart.substring(0, 6);
                if (firstSix.matches("0{6}")) {
                    return new BigDecimal(intPart);
                }
                if (firstSix.matches("9{6}")) {
                    BigDecimal intVal = new BigDecimal(intPart);
                    return intVal.add(BigDecimal.ONE);
                }
            }
        }
        
        return rounded.stripTrailingZeros();
    }

    private String formatResult(BigDecimal value) {
        String str = value.toPlainString();
        
        if (str.contains(".") && str.endsWith(".0")) {
            str = str.substring(0, str.length() - 2);
        }
        
        if (str.contains("E") || str.contains("e")) {
            str = value.toPlainString();
        }
        
        return str;
    }

    public BigDecimal getResult() {
        return result;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}