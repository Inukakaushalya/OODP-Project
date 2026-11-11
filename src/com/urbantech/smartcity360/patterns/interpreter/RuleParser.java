package com.urbantech.smartcity360.patterns.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper that turns the rule text into an Expression tree.
 * Grammar:  rule := and ( OR and )*    and := unary ( AND unary )*
 *           unary := NOT unary | ( rule ) | variable operator number
 * (This class only BUILDS the tree; the Interpreter pattern itself is the Expression classes.)
 */
public class RuleParser {

    private static final Set<String> VARIABLES = Set.of("noise", "hour", "parked");
    private static final Pattern TOKEN = Pattern.compile("\\s*(>=|<=|==|!=|>|<|\\(|\\)|[A-Za-z_]+|\\d+)");

    private List<String> tokens;
    private int pos;

    public Expression parse(String text) {
        tokens = tokenize(text);
        pos = 0;
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("The rule is empty");
        }
        Expression result = parseOr();
        if (pos < tokens.size()) {
            throw new IllegalArgumentException("Unexpected '" + tokens.get(pos) + "'");
        }
        return result;
    }

    private List<String> tokenize(String text) {
        List<String> list = new ArrayList<>();
        Matcher m = TOKEN.matcher(text);
        int index = 0;
        while (index < text.length()) {
            m.region(index, text.length());
            if (!m.lookingAt()) {
                if (text.substring(index).isBlank()) {
                    break;
                }
                throw new IllegalArgumentException("Unexpected character near '" + text.substring(index).trim() + "'");
            }
            list.add(m.group(1));
            index = m.end();
        }
        return list;
    }

    private boolean isNext(String word) {
        return pos < tokens.size() && tokens.get(pos).equalsIgnoreCase(word);
    }

    private Expression parseOr() {
        Expression left = parseAnd();
        while (isNext("OR")) {
            pos++;
            left = new OrExpression(left, parseAnd());
        }
        return left;
    }

    private Expression parseAnd() {
        Expression left = parseUnary();
        while (isNext("AND")) {
            pos++;
            left = new AndExpression(left, parseUnary());
        }
        return left;
    }

    private Expression parseUnary() {
        if (isNext("NOT")) {
            pos++;
            return new NotExpression(parseUnary());
        }
        if (isNext("(")) {
            pos++;
            Expression inner = parseOr();
            if (!isNext(")")) {
                throw new IllegalArgumentException("Missing closing bracket )");
            }
            pos++;
            return inner;
        }
        return parseComparison();
    }

    private Expression parseComparison() {
        if (pos >= tokens.size()) {
            throw new IllegalArgumentException("The rule ended too early");
        }
        String variable = tokens.get(pos++).toLowerCase();
        if (!VARIABLES.contains(variable)) {
            throw new IllegalArgumentException("Unknown variable '" + variable + "' (use noise, hour or parked)");
        }
        if (pos >= tokens.size() || !List.of(">", "<", ">=", "<=", "==", "!=").contains(tokens.get(pos))) {
            throw new IllegalArgumentException("Expected a comparison operator after '" + variable + "'");
        }
        String operator = tokens.get(pos++);
        if (pos >= tokens.size() || !tokens.get(pos).matches("\\d+")) {
            throw new IllegalArgumentException("Expected a number after '" + variable + " " + operator + "'");
        }
        int value = Integer.parseInt(tokens.get(pos++));
        return new ComparisonExpression(variable, operator, value);
    }
}
