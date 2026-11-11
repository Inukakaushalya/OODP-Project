package com.urbantech.smartcity360.panels;

import com.urbantech.smartcity360.patterns.composite.Building;
import com.urbantech.smartcity360.patterns.composite.Zone;
import com.urbantech.smartcity360.patterns.composite.ZoneComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/** Part B - Hierarchical structure of smart zones (Composite pattern). */
public class CompositePanel extends JPanel {

    private final Zone root = new Zone("City", "Colombo");
    private final DefaultTreeModel treeModel = new DefaultTreeModel(null);
    private final JTree tree = new JTree(treeModel);

    private final JLabel lblSelected = new JLabel("-");
    private final JLabel lblUsage = new JLabel("-");
    private final JComboBox<String> cmbCommand = new JComboBox<>(
            new String[]{"Report usage", "Shutdown power", "Restore power"});
    private final JTextField txtName = new JTextField();
    private final JTextField txtUsage = new JTextField();
    private final JTextArea txtLog = UI.outputArea();

    private ZoneComponent selected;

    public CompositePanel() {
        buildSampleCity();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        add(UI.header("Zone manager", "Part B  |  Composite pattern"), BorderLayout.NORTH);

        tree.setRootVisible(true);
        tree.setRowHeight(24);
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree t, Object value, boolean sel, boolean exp,
                    boolean leaf, int row, boolean focus) {
                super.getTreeCellRendererComponent(t, value, sel, exp, leaf, row, focus);
                Object o = ((DefaultMutableTreeNode) value).getUserObject();
                if (!sel && o instanceof ZoneComponent z && !z.isPowerOn()) {
                    setForeground(UI.BAD);
                }
                return this;
            }
        });
        tree.addTreeSelectionListener(e -> onSelect());

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                UI.scroll(tree, "City zones"), buildRightSide());
        split.setResizeWeight(0.38);
        add(split, BorderLayout.CENTER);

        refreshTree(root);
    }

    private JPanel buildRightSide() {
        JPanel cmd = UI.formPanel("Command");
        UI.addRow(cmd, 0, "Selected zone", lblSelected);
        UI.addRow(cmd, 1, "Total usage", lblUsage);
        UI.addRow(cmd, 2, "Command", cmbCommand);
        JButton btnExecute = new JButton("Execute");
        UI.primary(btnExecute);
        btnExecute.addActionListener(e -> execute());
        JPanel b1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        b1.add(btnExecute);
        UI.addRow(cmd, 3, "", b1);

        JPanel add = UI.formPanel("Add or remove a zone");
        UI.addRow(add, 0, "New zone name", txtName);
        UI.addRow(add, 1, "Usage (kWh)", txtUsage);
        JButton btnAdd = new JButton("Add child");
        JButton btnRemove = new JButton("Remove selected");
        btnAdd.addActionListener(e -> addChild());
        btnRemove.addActionListener(e -> removeSelected());
        JPanel b2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        b2.add(btnAdd);
        b2.add(btnRemove);
        UI.addRow(add, 2, "", b2);
        JLabel hint = new JLabel("City > District > Street > Building. Usage is needed only for buildings.");
        hint.setForeground(UI.MUTED);
        UI.addRow(add, 3, "", hint);

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.add(cmd, BorderLayout.NORTH);
        top.add(add, BorderLayout.CENTER);

        JButton btnClearLog = new JButton("Clear log");
        btnClearLog.addActionListener(e -> txtLog.setText(""));
        JPanel logTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        logTools.add(btnClearLog);
        JPanel logPanel = new JPanel(new BorderLayout(0, 4));
        logPanel.add(UI.scroll(txtLog, "Log"), BorderLayout.CENTER);
        logPanel.add(logTools, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout(0, 8));
        right.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        right.add(top, BorderLayout.NORTH);
        right.add(logPanel, BorderLayout.CENTER);
        return right;
    }

    private void buildSampleCity() {
        Zone western = new Zone("District", "Western");
        Zone galle = new Zone("Street", "Galle Road");
        galle.add(new Building("Tower A", 450));
        galle.add(new Building("Tower B", 300));
        Zone main = new Zone("Street", "Main Street");
        main.add(new Building("City Mall", 600));
        main.add(new Building("Library", 120));
        western.add(galle);
        western.add(main);

        Zone central = new Zone("District", "Central");
        Zone kandy = new Zone("Street", "Kandy Road");
        kandy.add(new Building("Hospital", 700));
        kandy.add(new Building("School", 150));
        central.add(kandy);

        root.add(western);
        root.add(central);
    }

    private DefaultMutableTreeNode toNode(ZoneComponent component) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(component);
        if (component instanceof Zone zone) {
            for (ZoneComponent child : zone.getChildren()) {
                node.add(toNode(child));
            }
        }
        return node;
    }

    /** Rebuilds the JTree from the composite objects (the JTree is only a display). */
    private void refreshTree(ZoneComponent toSelect) {
        DefaultMutableTreeNode rootNode = toNode(root);
        treeModel.setRoot(rootNode);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        DefaultMutableTreeNode found = find(rootNode, toSelect);
        if (found != null) {
            tree.setSelectionPath(new TreePath(found.getPath()));
        }
        onSelect();
    }

    private DefaultMutableTreeNode find(DefaultMutableTreeNode node, ZoneComponent target) {
        if (node.getUserObject() == target) {
            return node;
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode r = find((DefaultMutableTreeNode) node.getChildAt(i), target);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    private void onSelect() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        selected = node == null ? null : (ZoneComponent) node.getUserObject();
        if (selected == null) {
            lblSelected.setText("-");
            lblUsage.setText("-");
            txtUsage.setEnabled(false);
            return;
        }
        lblSelected.setText(selected.getLevel() + ": " + selected.getName());
        lblUsage.setText(selected.getUsage() + " kWh");
        txtUsage.setEnabled(selected instanceof Zone z && z.getLevel().equals("Street"));
    }

    private void execute() {
        if (selected == null) {
            UI.warn(this, "Select a zone in the tree first.");
            return;
        }
        String command = (String) cmbCommand.getSelectedItem();
        String result = switch (cmbCommand.getSelectedIndex()) {
            case 0 -> selected.reportUsage("");
            case 1 -> selected.shutdownPower("");
            default -> selected.restorePower("");
        };
        txtLog.append("> " + command + " on " + selected.getLevel() + ": " + selected.getName() + "\n"
                + result + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
        refreshTree(selected);
    }

    private void addChild() {
        if (!(selected instanceof Zone parent)) {
            UI.warn(this, "Select a City, District or Street to add a child zone.");
            return;
        }
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            UI.warn(this, "Enter a name for the new zone.");
            return;
        }
        ZoneComponent child;
        switch (parent.getLevel()) {
            case "City" ->
                child = new Zone("District", name);
            case "District" ->
                child = new Zone("Street", name);
            default -> {
                double usage;
                try {
                    usage = Double.parseDouble(txtUsage.getText().trim());
                    if (usage < 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    UI.warn(this, "Enter the building's usage as a positive number, for example 250.");
                    return;
                }
                child = new Building(name, usage);
            }
        }
        parent.add(child);
        txtLog.append("Added " + child.getLevel() + ": " + name + " to " + parent.getLevel() + ": "
                + parent.getName() + "\n\n");
        txtName.setText("");
        txtUsage.setText("");
        refreshTree(child);
    }

    private void removeSelected() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null || node.getParent() == null) {
            UI.warn(this, "Select a zone other than the city root to remove it.");
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Remove " + selected.getLevel() + " \"" + selected.getName() + "\" and everything inside it?",
                "Remove zone", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        Zone parent = (Zone) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
        parent.remove(selected);
        txtLog.append("Removed " + selected.getLevel() + ": " + selected.getName() + "\n\n");
        refreshTree(parent);
    }
}
