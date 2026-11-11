package com.urbantech.smartcity360.panels;

import com.urbantech.smartcity360.patterns.interpreter.Context;
import com.urbantech.smartcity360.patterns.interpreter.Expression;
import com.urbantech.smartcity360.patterns.interpreter.RuleParser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/** Part E - Interpreting city policies written in a small language (Interpreter pattern). */
public class InterpreterPanel extends JPanel {

    private static final String[][] POLICIES = {
        {"Night noise limit", "noise > 70 AND hour >= 22"},
        {"Business hours parking limit", "hour >= 8 AND hour <= 18 AND parked > 2"},
        {"Strict noise policy", "noise > 85 OR (noise > 70 AND hour >= 22)"},
        {"Quiet hours, no parking", "NOT (hour >= 6 AND hour <= 22) AND parked > 0"},
        {"Custom rule", ""}};

    private final JComboBox<String> cmbPolicy = new JComboBox<>();
    private final JTextField txtRule = new JTextField();
    private final JSpinner spnNoise = new JSpinner(new SpinnerNumberModel(78, 0, 200, 1));
    private final JSpinner spnHour = new JSpinner(new SpinnerNumberModel(23, 0, 23, 1));
    private final JSpinner spnParked = new JSpinner(new SpinnerNumberModel(3, 0, 24, 1));
    private final JLabel lblResult = new JLabel("Not evaluated yet", JLabel.CENTER);
    private final JTextArea txtTree = UI.outputArea();
    private final RuleParser parser = new RuleParser();

    public InterpreterPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        add(UI.header("City policies", "Part E  |  Interpreter pattern"), BorderLayout.NORTH);

        for (String[] p : POLICIES) {
            cmbPolicy.addItem(p[0]);
        }
        cmbPolicy.addActionListener(e -> {
            String rule = POLICIES[cmbPolicy.getSelectedIndex()][1];
            if (!rule.isEmpty()) {
                txtRule.setText(rule);
            }
        });
        txtRule.setText(POLICIES[0][1]);

        JPanel form = UI.formPanel("Policy rule and situation");
        UI.addRow(form, 0, "Policy", cmbPolicy);
        UI.addRow(form, 1, "Rule", txtRule);
        JLabel hint = new JLabel("A rule is true when the policy is VIOLATED. Variables: noise, hour, parked. "
                + "Operators: > < >= <= == !=  AND OR NOT ( )");
        hint.setForeground(UI.MUTED);
        UI.addRow(form, 2, "", hint);
        UI.addRow(form, 3, "Noise level (dB)", spnNoise);
        UI.addRow(form, 4, "Hour of day (0 - 23)", spnHour);
        UI.addRow(form, 5, "Parked hours", spnParked);

        JButton btnEvaluate = new JButton("Evaluate");
        UI.primary(btnEvaluate);
        btnEvaluate.addActionListener(e -> evaluate());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttons.add(btnEvaluate);
        UI.addRow(form, 6, "", buttons);

        lblResult.setOpaque(true);
        lblResult.setFont(lblResult.getFont().deriveFont(Font.BOLD, 20f));
        lblResult.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblResult.setBackground(new Color(0xEAEEF2));

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.add(form, BorderLayout.NORTH);
        JPanel lower = new JPanel(new BorderLayout(0, 8));
        lower.add(lblResult, BorderLayout.NORTH);
        lower.add(UI.scroll(txtTree, "Expression tree (how the rule was interpreted)"), BorderLayout.CENTER);
        center.add(lower, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private void evaluate() {
        Expression rule;
        try {
            rule = parser.parse(txtRule.getText());
        } catch (IllegalArgumentException ex) {
            lblResult.setText("Invalid rule");
            lblResult.setBackground(new Color(0xFFF1C2));
            lblResult.setForeground(Color.BLACK);
            txtTree.setText("Could not read the rule: " + ex.getMessage());
            return;
        }
        Context context = new Context();
        context.set("noise", (Integer) spnNoise.getValue());
        context.set("hour", (Integer) spnHour.getValue());
        context.set("parked", (Integer) spnParked.getValue());

        boolean violation = rule.interpret(context);
        lblResult.setText(violation ? "VIOLATION" : "ALLOWED");
        lblResult.setForeground(Color.WHITE);
        lblResult.setBackground(violation ? UI.BAD : UI.GOOD);
        txtTree.setText(rule.display("", context));
        txtTree.setCaretPosition(0);
    }
}
