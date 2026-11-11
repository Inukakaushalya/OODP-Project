package com.urbantech.smartcity360.patterns.interpreter;

/** Non-terminal expression: true only when both smaller expressions are true. */
public class AndExpression implements Expression {

    private final Expression left;
    private final Expression right;

    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(Context context) {
        return left.interpret(context) && right.interpret(context);
    }

    @Override
    public String display(String indent, Context context) {
        return indent + "AND -> " + interpret(context) + "\n"
                + left.display(indent + "    ", context) + "\n"
                + right.display(indent + "    ", context);
    }
}
