package com.janja.calculator.expression;

import java.util.ArrayList;

public class InfixBuilder {
    private String infix = "";
    private String tempInfix = "";
    private StringBuffer operandBuffer;

    public InfixBuilder() {
        operandBuffer = new StringBuffer();
    }

    public Infix getInfix() {
        complete();
        return new Infix(toArrayList());
    }

    @Override
    public String toString() {
        return getInfix().toString();
    }

    public ArrayList<String> toArrayList() {
        ArrayList<String> out = new ArrayList<String>();
        char[] infixArray = infix.toCharArray();

        for (int i = 0; i < infixArray.length; i++) {
            char token = infixArray[i];
            if (token == '(') {
                String operand = "";
                while ((token = infixArray[++i]) != ')') {
                    operand = operand + String.valueOf(token);
                }
                out.add(operand);
            } else if (!isOperator(String.valueOf(token))) {
                String operand = String.valueOf(token);
                while (i + 1 < infixArray.length) {
                    token = infixArray[++i];
                    if (!isOperator(String.valueOf(token))) {
                        operand = operand + String.valueOf(token);
                    } else {
                        i--;
                        break;
                    }
                }
                out.add(operand);
            } else { // operand
                out.add(String.valueOf(token));
            }
        }
        return out;
    }

    public String getTempInfix() {
        String tempOperand = operandBuffer.toString();
        tempInfix = infix + tempOperand;
        return tempInfix;
    }

    public void addElement(String element) {
        if (isOperand(element)) {
            addOperandToBuffer(element);
        } else if (isOperator(element)) {
            if (hasOperandInBuffer()) {
                catchOperand();
            }
            catchOperator(element);
        } else if (isPoint(element)) {
            addPointToBuffer(element);
        }
    }

    public void removeLast() {
        if (hasOperandInBuffer()) {
            operandBuffer.deleteCharAt(operandBuffer.length() - 1);
        } else {
            if (infix.length() > 0) {
                String last = infix.substring(infix.length() - 1,
                        infix.length());
                if (last.equals("-") && hasSingleLeftBracket()) {
                    infix = infix.substring(0, infix.length() - 2);
                } else {
                    infix = infix.substring(0, infix.length() - 1);
                }
            }
        }

        if (hasSingleLeftBracket()) {
            String last = infix.substring(infix.length() - 1, infix.length());
            if (!hasOperandInBuffer() && !isOperand(last)) {
                infix = infix.substring(0, infix.length() - 2);
            }
        }
    }

    private void addOperandToBuffer(String element) {
        String operandHead = operandBuffer.toString();

        if (operandHead.startsWith("0") && !operandHead.contains(".")) {
            operandBuffer.deleteCharAt(0);
        }
        operandBuffer.append(element);
    }

    private void addPointToBuffer(String element) {
        String operandHead = operandBuffer.toString();
        if (operandHead.contains(".")) {
            return;
        }

        if (!hasOperandInBuffer()) {
            operandBuffer.append(0);
            operandBuffer.append(element);
        } else {
            operandBuffer.append(element);
        }
    }

    private void catchOperand() {
        String operand = operandBuffer.toString();
        operandBuffer.delete(0, operandBuffer.length());
        if (hasSingleLeftBracket()) {
            infix = infix + operand + ")";
        } else {
            infix = infix + operand;
        }
    }

    private void catchOperator(String element) {
        boolean infixIsEmpty = infix.length() == 0;
        if (infixIsEmpty) {
            if (element.equals("-")) {
                infix = infix + "(" + element;
            }
            return;
        }

        if (isRedundantOperator(element)) {
            String last = infix.substring(infix.length() - 1, infix.length());
            if (element.equals("-")) {
                if (last.equals("*") || last.equals("/")) {
                    infix = infix + "(" + element;
                    return;
                }
            }

            if (hasNegativeSign()) {
                if (element.equals("-")) {
                    return;
                } else {
                    boolean onlyNegativeSign = infix.length() == 2;
                    if (onlyNegativeSign) {
                        infix = infix.substring(0, 0);
                    } else {
                        infix = infix.substring(0, infix.length() - 3)
                                + element;
                    }
                }
            } else {
                infix = infix.substring(0, infix.length() - 1) + element;
            }
        } else {
            infix = infix + element;
        }
    }

    private boolean isRedundantOperator(String element) {
        String last = "";

        if (infix.length() > 0) {
            last = infix.substring(infix.length() - 1, infix.length());
        }

        return isOperator(element) && isOperator(last);
    }

    private boolean hasNegativeSign() {
        boolean hasNegative = false;
        if (infix.length() < 2) {
            return false;
        }

        // contain negative sign and left bracket
        boolean isHead = infix.length() == 2;
        if (isHead) {
            hasNegative = infix.equals("(-");
        } else if (!isHead) {
            String lastThreeChar = infix.substring(infix.length() - 3,
                    infix.length());
            hasNegative = lastThreeChar.equals("*(-")
                    || lastThreeChar.equals("/(-");
        }
        return hasNegative;
    }

    private boolean hasOperandInBuffer() {
        return operandBuffer.length() > 0;
    }

    private boolean hasSingleLeftBracket() {
        boolean hasLeftBracket = false;

        for (int i = infix.length() - 1; i >= 0; i--) {
            char element = infix.charAt(i);
            if (element == '(') {
                return true;
            } else if (element == ')') {
                return false;
            }
        }

        return hasLeftBracket;
    }

    private boolean isOperand(String val) {
        String operands = "0123456789";
        return operands.contains(val);
    }

    private boolean isPoint(String val) {
        String point = ".";
        return point.equals(val);
    }

    private boolean isOperator(String val) {
        String operators = "-+/*";
        return operators.contains(val);
    }

    private void complete() {
        if (infix.length() <= 0) {
            return;
        }
        String last = infix.substring(infix.length() - 1, infix.length());
        if (hasOperandInBuffer()) {
            catchOperand();
        } else if (isOperand(last)) {
            if (hasSingleLeftBracket()) {
                infix = infix + ")";
            }
        } else if (isOperator(last)) {
            if (hasSingleLeftBracket()) {
                infix = infix.substring(0, infix.length() - 2);
            } else {
                infix = infix.substring(0, infix.length() - 1);
            }
        }
    }

    public void clear() {
        infix = "";
        operandBuffer.delete(0, operandBuffer.length());
    }
}
