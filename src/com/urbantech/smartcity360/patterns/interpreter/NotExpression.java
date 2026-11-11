package com.urbantech.smartcity360.patterns.interpreter;

/** Non-terminal expression: reverses the result of the smaller expression. */
public class NotExpression implements Expression {

    private final Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean interpret(Context context) {
        return !expression.interpret(context);
    }

    @Override
    public String display(String indent, Context context) {
        return indent + "NOT -> " + interpret(context) + "\n" + expression.display(indent + "    ", context);
    }
}
