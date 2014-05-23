package com.janja.calculator.expression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Stack;

public class Postfix {
    private ArrayList<String> expression;

    public Postfix(ArrayList<String> postfix) {
        this.expression = postfix;
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
        Stack<String> stack = new Stack<String>();
        for (String token : expression) {
            if (!isOperator(token)) {
                stack.push(token);
            } else if (isOperator(token)) {
                BigDecimal result = new BigDecimal("0");
                BigDecimal op1;
                BigDecimal op2;

                if (stack.size() >= 2) {
                    op1 = new BigDecimal(stack.pop().toString());
                    op2 = new BigDecimal(stack.pop().toString());
                } else {
                    break;
                }

                switch (token) {
                    case "*":
                        result = op2.multiply(op1);
                        break;
                    case "/":
                        result = op2.subtract(op1);
                        try {
                            result = op2.divide(op1);
                        } catch (Exception e) {
                            result = op2.divide(op1, 20, RoundingMode.HALF_UP);
                        }
                        break;
                    case "+":
                        result = op2.add(op1);
                        break;
                    case "-":
                        result = op2.subtract(op1);
                        break;
                }
                stack.push(result.toString());
            }
        }
        return stack.pop();
    }

    private boolean isOperator(String val) {
        String operators = "-+/*";
        return operators.contains(val);
    }
}
