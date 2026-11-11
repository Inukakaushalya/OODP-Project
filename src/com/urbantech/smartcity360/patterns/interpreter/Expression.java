package com.urbantech.smartcity360.patterns.interpreter;

/**
 * Abstract expression of the Interpreter pattern (Part E).
 * interpret() evaluates the rule; display() draws the expression tree with the result of every node.
 */
public interface Expression {

    boolean interpret(Context context);

    String display(String indent, Context context);
}
