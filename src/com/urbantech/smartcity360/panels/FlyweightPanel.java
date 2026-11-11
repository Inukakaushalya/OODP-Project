package com.urbantech.smartcity360.panels;

import com.urbantech.smartcity360.patterns.flyweight.SharedStreetLightPool;
import com.urbantech.smartcity360.patterns.flyweight.StreetLight;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

/** Part D - Efficient management of smart street lights (Flyweight pattern). */
public class FlyweightPanel extends JPanel {

    private record Preset(String type, int wattage, String colour) {

        @Override
        public String toString() {
            return type + " - " + wattage + "W - " + colour;
        }
    }

    // Rough object sizes (bytes) used ONLY for the estimate shown on screen.
    private static final int BYTES_EXTRINSIC = 32;
    private static final int BYTES_INTRINSIC = 40;

    private final Preset[] presets = {
        new Preset("LED", 50, "White"),
        new Preset("Solar", 30, "Cool white"),
        new Preset("Sodium", 70, "Yellow"),
        new Preset("Halogen", 100, "Warm white")};

    private final JComboBox<Preset> cmbType = new JComboBox<>(presets);
    private final JTextField txtLocation = new JTextField();
    private final JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"ON", "OFF"});

    private final JLabel lblTotal = new JLabel();
    private final JLabel lblShared = new JLabel();
    private final JLabel lblMemory = new JLabel();
    private final JLabel lblInfo = new JLabel(" ");

    private final List<StreetLight> lights = new ArrayList<>();
    private final LightTableModel tableModel = new LightTableModel();
    private final JTable table = new JTable(tableModel);
    private final Random random = new Random();
    private int nextId = 1;

    public FlyweightPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        add(UI.header("Street lights", "Part D  |  Flyweight pattern"), BorderLayout.NORTH);

        JPanel form = UI.formPanel("Add street lights");
        UI.addRow(form, 0, "Light type", cmbType);
        UI.addRow(form, 1, "Location", txtLocation);
        UI.addRow(form, 2, "Status", cmbStatus);

        JButton btnAdd = new JButton("Add light");
        UI.primary(btnAdd);
        JButton btnBulk = new JButton("Add 10,000 lights");
        JButton btnToggle = new JButton("Toggle ON / OFF");
        JButton btnClear = new JButton("Clear all");
        btnAdd.addActionListener(e -> addOne());
        btnBulk.addActionListener(e -> addBulk(10000));
        btnToggle.addActionListener(e -> toggle());
        btnClear.addActionListener(e -> clearAll());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.add(btnAdd);
        buttons.add(btnBulk);
        buttons.add(btnToggle);
        buttons.add(btnClear);
        UI.addRow(form, 3, "", buttons);

        JPanel counters = new JPanel(new GridLayout(2, 2, 16, 4));
        counters.setBorder(BorderFactory.createTitledBorder("Memory view"));
        for (JLabel l : new JLabel[]{lblTotal, lblShared, lblMemory}) {
            l.setFont(l.getFont().deriveFont(Font.BOLD, 14f));
            counters.add(l);
        }
        lblInfo.setForeground(UI.MUTED);
        counters.add(lblInfo);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(22);

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.add(form, BorderLayout.NORTH);
        top.add(counters, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.add(top, BorderLayout.NORTH);
        center.add(UI.scroll(table, "Street lights (same \"Shared object\" id = one shared flyweight)"),
                BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
        updateCounters();
    }

    private void addOne() {
        String location = txtLocation.getText().trim();
        if (location.isEmpty()) {
            UI.warn(this, "Enter the light's location, for example Galle Road, Pole 12.");
            return;
        }
        Preset p = (Preset) cmbType.getSelectedItem();
        lights.add(new StreetLight(nextId++, p.type(), p.wattage(), p.colour(), location,
                cmbStatus.getSelectedIndex() == 0));
        int last = lights.size() - 1;
        tableModel.fireTableRowsInserted(last, last);
        txtLocation.setText("");
        lblInfo.setText("Added light " + (nextId - 1));
        updateCounters();
        table.scrollRectToVisible(table.getCellRect(last, 0, true));
    }

    private void addBulk(int count) {
        long start = System.nanoTime();
        int first = lights.size();
        for (int i = 0; i < count; i++) {
            Preset p = presets[random.nextInt(presets.length)];
            int id = nextId++;
            lights.add(new StreetLight(id, p.type(), p.wattage(), p.colour(),
                    "Pole-" + id, random.nextBoolean()));
        }
        tableModel.fireTableRowsInserted(first, lights.size() - 1);
        long ms = (System.nanoTime() - start) / 1_000_000;
        lblInfo.setText("Created " + String.format("%,d", count) + " lights in " + ms + " ms");
        updateCounters();
    }

    private void toggle() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UI.warn(this, "Select a street light in the table first.");
            return;
        }
        StreetLight light = lights.get(row);
        light.setOn(!light.isOn());
        tableModel.fireTableRowsUpdated(row, row);
        lblInfo.setText("Light " + light.getId() + " is now " + (light.isOn() ? "ON" : "OFF")
                + " (only its own status changed)");
    }

    private void clearAll() {
        lights.clear();
        SharedStreetLightPool.clear();
        nextId = 1;
        tableModel.fireTableDataChanged();
        lblInfo.setText("Everything cleared");
        updateCounters();
    }

    private void updateCounters() {
        int total = lights.size();
        int shared = SharedStreetLightPool.getPoolSize();
        long with = (long) total * BYTES_EXTRINSIC + (long) shared * BYTES_INTRINSIC;
        long without = (long) total * (BYTES_EXTRINSIC + BYTES_INTRINSIC);
        lblTotal.setText("StreetLight objects: " + String.format("%,d", total));
        lblShared.setText("Shared flyweight objects: " + shared);
        if (total == 0) {
            lblMemory.setText("Estimated memory: 0 KB");
        } else {
            long saved = Math.round(100.0 * (without - with) / without);
            lblMemory.setText("Estimated memory: " + with / 1024 + " KB (without flyweight: "
                    + without / 1024 + " KB, saved " + saved + "%)");
        }
    }

    private class LightTableModel extends AbstractTableModel {

        private final String[] columns = {"ID", "Type", "Wattage (W)", "Colour", "Location", "Status", "Shared object"};

        @Override
        public int getRowCount() {
            return lights.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int c) {
            return columns[c];
        }

        @Override
        public Object getValueAt(int row, int col) {
            StreetLight l = lights.get(row);
            return switch (col) {
                case 0 -> l.getId();
                case 1 -> l.getSharedStreetLight().getType();
                case 2 -> l.getSharedStreetLight().getWattage();
                case 3 -> l.getSharedStreetLight().getColour();
                case 4 -> l.getLocation();
                case 5 -> l.isOn() ? "ON" : "OFF";
                default -> "#" + Integer.toHexString(System.identityHashCode(l.getSharedStreetLight()));
            };
        }
    }
}
