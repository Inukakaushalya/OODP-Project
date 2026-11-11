package com.urbantech.smartcity360.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** Small helper methods shared by all panels, so every screen looks the same. */
final class UI {

    static final Color ACCENT = new Color(0x1F6FEB);
    static final Color GOOD = new Color(0x1A7F37);
    static final Color BAD = new Color(0xCF222E);
    static final Color MUTED = new Color(0x57606A);

    private UI() {
    }

    /** Title + subtitle block shown at the top of every panel. */
    static JPanel header(String title, String subtitle) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 24f));
        JLabel s = new JLabel(subtitle);
        s.setFont(s.getFont().deriveFont(Font.PLAIN, 13f));
        s.setForeground(MUTED);
        p.add(t);
        p.add(javax.swing.Box.createVerticalStrut(2));
        p.add(s);
        return p;
    }

    static JPanel formPanel(String title) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));
        return p;
    }

    /** Adds "label + field" as one row of a GridBagLayout form. */
    static void addRow(JPanel form, int row, String label, JComponent field) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = row;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 4, 5, 12);
        form.add(new JLabel(label), c);

        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 1;
        f.gridy = row;
        f.weightx = 1;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.insets = new Insets(5, 0, 5, 4);
        form.add(field, f);
    }

    static JTextArea outputArea() {
        JTextArea a = new JTextArea();
        a.setEditable(false);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        a.setMargin(new Insets(8, 10, 8, 10));
        return a;
    }

    static JScrollPane scroll(Component c, String title) {
        JScrollPane sp = new JScrollPane(c);
        if (c instanceof javax.swing.JTable table) {
            sp.setColumnHeaderView(table.getTableHeader());
        }
        sp.setBorder(BorderFactory.createTitledBorder(title));
        return sp;
    }

    static void primary(JButton b) {
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
    }

    static void warn(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Check your input", JOptionPane.WARNING_MESSAGE);
    }

    static JPanel pad(int top, int left, int bottom, int right, java.awt.LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        return p;
    }
}
