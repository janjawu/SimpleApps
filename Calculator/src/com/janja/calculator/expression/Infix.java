package com.janja.calculator.expression;

import java.util.ArrayList;
import java.util.Stack;

public class Infix {
    private ArrayList<String> expression;

    public Infix(ArrayList<String> infix) {
        this.expression = infix;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (String token : expression) {
            buffer.append(token);
        }
        return buffer.toString();
    }

    public String evaluate() {
        return toPostfix().evaluate();
    }

    public Postfix toPostfix() {
        Stack<String> stack = new Stack<String>();
        ArrayList<String> out = new ArrayList<String>();

        for (String token : expression) {
            if (isOperator(token)) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    if (operatorGreaterOrEqual(stack.peek(), token)) {
                        out.add(stack.pop());
                    } else {
                        break;
                    }
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    out.add(stack.pop());
                }

                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else { // operand
                out.add(token);
            }
        }
        while (!stack.empty()) {
            out.add(stack.pop());
        }
        return new Postfix(out);
    }

    private int getPriotity(String operator) {
        int ret = 0;
        if (operator.equals("-") || operator.equals("+")) {
            ret = 1;
        } else if (operator.equals("*") || operator.equals("/")) {
            ret = 2;
        }
        return ret;
    }

    private boolean operatorGreaterOrEqual(String op1, String op2) {
        return getPriotity(op1) >= getPriotity(op2);
    }

    private boolean isOperator(String val) {
        String operators = "-+/*";
        return operators.contains(val);
    }
}
