package com.urbantech.smartcity360.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Welcome screen with one card per module. It only shows information and lets the user jump
 * to a module - it does not share data between panels.
 */
public class DashboardPanel extends JPanel {

    public DashboardPanel(Consumer<String> navigator) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        add(UI.header("Welcome to SmartCity360", "Smart City Management Platform - UrbanTech Solutions"),
                BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.add(card("Data Security", "Part A", "Decorator",
                "Add encryption, authentication and compression layers to energy meter data streams.",
                "decorator", navigator));
        grid.add(card("Zone Manager", "Part B", "Composite",
                "Manage city, district, street and building zones with the same commands at every level.",
                "composite", navigator));
        grid.add(card("Complaints", "Part C", "Chain of Responsibility",
                "Escalate citizen complaints from local staff to supervisors to city managers.",
                "chain", navigator));
        grid.add(card("Street Lights", "Part D", "Flyweight",
                "Share common light data so thousands of street lights use very little memory.",
                "flyweight", navigator));
        grid.add(card("City Policies", "Part E", "Interpreter",
                "Read and evaluate city rules written in a small policy language.",
                "interpreter", navigator));
        grid.add(aboutCard());
        add(grid, BorderLayout.CENTER);
    }

    private JPanel card(String title, String part, String pattern, String text, String key,
            Consumer<String> navigator) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(0xD0D7DE)),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JPanel top = new JPanel(new GridLayout(3, 1, 0, 2));
        top.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 18f));
        JLabel p = new JLabel(part + "  |  Pattern: " + pattern);
        p.setForeground(UI.ACCENT);
        top.add(t);
        top.add(p);
        top.add(new JLabel(" "));
        card.add(top, BorderLayout.NORTH);

        JLabel d = new JLabel("<html><body style='width:230px'>" + text + "</body></html>");
        d.setVerticalAlignment(JLabel.TOP);
        d.setForeground(UI.MUTED);
        card.add(d, BorderLayout.CENTER);

        JButton open = new JButton("Open");
        open.addActionListener(e -> navigator.accept(key));
        JPanel south = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        south.setOpaque(false);
        south.add(open);
        card.add(south, BorderLayout.SOUTH);
        return card;
    }

    private JPanel aboutCard() {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(0xD0D7DE)),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        JLabel t = new JLabel("About this project");
        t.setFont(t.getFont().deriveFont(Font.BOLD, 18f));
        card.add(t, BorderLayout.NORTH);
        JLabel d = new JLabel("<html><body style='width:230px'>"
                + "JIAT/OODP II/EX/01<br>Java SE desktop application (Swing)<br>"
                + "5 modules, 5 design patterns<br>No database - data is kept in memory</body></html>");
        d.setVerticalAlignment(JLabel.TOP);
        d.setForeground(UI.MUTED);
        card.add(d, BorderLayout.CENTER);
        return card;
    }
}
