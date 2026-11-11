package com.urbantech.smartcity360.GUI;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.urbantech.smartcity360.panels.ChainPanel;
import com.urbantech.smartcity360.panels.CompositePanel;
import com.urbantech.smartcity360.panels.DashboardPanel;
import com.urbantech.smartcity360.panels.DecoratorPanel;
import com.urbantech.smartcity360.panels.FlyweightPanel;
import com.urbantech.smartcity360.panels.InterpreterPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * Main window: a sidebar with buttons and a CardLayout panel in the centre.
 * All panels are created ONCE and only shown/hidden, so the data inside them
 * (complaints, street lights, zones) is not lost when you switch between panels.
 */
public class Home extends JFrame {

    private static final String DASHBOARD = "dashboard";
    private static final String DECORATOR = "decorator";
    private static final String COMPOSITE = "composite";
    private static final String CHAIN = "chain";
    private static final String FLYWEIGHT = "flyweight";
    private static final String INTERPRETER = "interpreter";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final ButtonGroup navGroup = new ButtonGroup();
    private final Map<String, JToggleButton> navButtons = new LinkedHashMap<>();
    private final Map<String, String> titles = new LinkedHashMap<>();
    private final JLabel lblStatus = new JLabel();

    public Home() {
        setTitle("SmartCity360 - Smart City Management Platform");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        contentPanel.add(new DashboardPanel(this::showPanel), DASHBOARD);
        contentPanel.add(new DecoratorPanel(), DECORATOR);
        contentPanel.add(new CompositePanel(), COMPOSITE);
        contentPanel.add(new ChainPanel(), CHAIN);
        contentPanel.add(new FlyweightPanel(), FLYWEIGHT);
        contentPanel.add(new InterpreterPanel(), INTERPRETER);

        setMinimumSize(new Dimension(1100, 720));
        setExtendedState(MAXIMIZED_BOTH);
        showPanel(DASHBOARD);
    }

    private JLabel buildHeader() {
        JLabel header = new JLabel("Hi! Welcome to SmartCity360", JLabel.CENTER);
        header.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 24));
        header.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));

        addNavButton(sidebar, "Dashboard", DASHBOARD);
        addNavButton(sidebar, "Data Security", DECORATOR);
        addNavButton(sidebar, "Zone Manager", COMPOSITE);
        addNavButton(sidebar, "Complaints", CHAIN);
        addNavButton(sidebar, "Street Lights", FLYWEIGHT);
        addNavButton(sidebar, "City Policies", INTERPRETER);

        sidebar.add(Box.createVerticalGlue());
        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Lucida Fax", Font.PLAIN, 14));
        exit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        exit.setPreferredSize(new Dimension(190, 40));
        exit.addActionListener(e -> exitApplication());
        sidebar.add(exit);
        return sidebar;
    }

    private void addNavButton(JPanel sidebar, String text, String key) {
        JToggleButton button = new JToggleButton(text);
        button.setFont(new Font("Lucida Fax", Font.PLAIN, 14));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setPreferredSize(new Dimension(190, 40));
        button.setFocusable(false);
        button.addActionListener(e -> showPanel(key));
        navGroup.add(button);
        navButtons.put(key, button);
        titles.put(key, text);
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(14));
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        lblStatus.setForeground(java.awt.Color.GRAY);
        bar.add(lblStatus, BorderLayout.WEST);
        return bar;
    }

    private void showPanel(String key) {
        cardLayout.show(contentPanel, key);
        navButtons.get(key).setSelected(true);
        lblStatus.setText("Status: Ready   |   Panel: " + titles.get(key));
    }

    private void exitApplication() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Warning",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        FlatMacLightLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new Home().setVisible(true));
    }
}
