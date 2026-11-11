package com.urbantech.smartcity360.patterns.interpreter;

/** Terminal expression: one comparison such as "noise > 70". It contains no smaller expressions. */
public class ComparisonExpression implements Expression {

    private final String variable;
    private final String operator;
    private final int value;

    public ComparisonExpression(String variable, String operator, int value) {
        this.variable = variable;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean interpret(Context context) {
        int actual = context.get(variable);
        return switch (operator) {
            case ">" -> actual > value;
            case "<" -> actual < value;
            case ">=" -> actual >= value;
            case "<=" -> actual <= value;
            case "==" -> actual == value;
            case "!=" -> actual != value;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    @Override
    public String display(String indent, Context context) {
        return indent + variable + " " + operator + " " + value + "   [" + context.get(variable) + " "
                + operator + " " + value + " -> " + interpret(context) + "]";
    }
}
