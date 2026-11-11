package com.urbantech.smartcity360.panels;

import com.urbantech.smartcity360.patterns.chain.CityManager;
import com.urbantech.smartcity360.patterns.chain.Complaint;
import com.urbantech.smartcity360.patterns.chain.ComplaintHandler;
import com.urbantech.smartcity360.patterns.chain.LocalStaff;
import com.urbantech.smartcity360.patterns.chain.Supervisor;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Part C - Escalation of citizen complaints (Chain of Responsibility pattern). */
public class ChainPanel extends JPanel {

    private final JTextField txtCitizen = new JTextField();
    private final JComboBox<String> cmbCategory = new JComboBox<>(new String[]{
        "Garbage", "Street light", "Water leak", "Noise", "Power outage", "Road damage"});
    private final JSlider sldSeverity = new JSlider(1, 10, 5);
    private final JLabel lblSeverity = new JLabel();
    private final JTextArea txtDescription = new JTextArea(3, 20);
    private final JTextArea txtTrace = UI.outputArea();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Citizen", "Category", "Severity", "Handled by"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    private final List<Complaint> complaints = new ArrayList<>();
    private final ComplaintHandler firstHandler;
    private int nextId = 1;

    public ChainPanel() {
        // Build the chain once: LocalStaff -> Supervisor -> CityManager
        LocalStaff localStaff = new LocalStaff();
        Supervisor supervisor = new Supervisor();
        CityManager cityManager = new CityManager();
        localStaff.setHandler(supervisor);
        supervisor.setHandler(cityManager);
        firstHandler = localStaff;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        add(UI.header("Citizen complaints", "Part C  |  Chain of Responsibility pattern"), BorderLayout.NORTH);

        JPanel form = UI.formPanel("New complaint");
        UI.addRow(form, 0, "Citizen name", txtCitizen);
        UI.addRow(form, 1, "Category", cmbCategory);

        sldSeverity.setMajorTickSpacing(1);
        sldSeverity.setPaintTicks(true);
        sldSeverity.setPaintLabels(true);
        sldSeverity.setSnapToTicks(true);
        sldSeverity.addChangeListener(e -> updateSeverityLabel());
        JPanel sev = new JPanel(new BorderLayout(12, 0));
        sev.add(sldSeverity, BorderLayout.CENTER);
        sev.add(lblSeverity, BorderLayout.EAST);
        UI.addRow(form, 2, "Severity (1 - 10)", sev);
        updateSeverityLabel();

        txtDescription.setLineWrap(true);
        UI.addRow(form, 3, "Description", new JScrollPane(txtDescription));

        JButton btnSubmit = new JButton("Submit complaint");
        UI.primary(btnSubmit);
        JButton btnClear = new JButton("Clear form");
        btnSubmit.addActionListener(e -> submit());
        btnClear.addActionListener(e -> clearForm());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.add(btnSubmit);
        buttons.add(btnClear);
        UI.addRow(form, 4, "", buttons);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (!e.getValueIsAdjusting() && row >= 0) {
                txtTrace.setText(complaints.get(row).getTraceText());
            }
        });

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                UI.scroll(txtTrace, "Escalation trace"), UI.scroll(table, "Complaint history (click a row to see its trace)"));
        split.setResizeWeight(0.45);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.add(form, BorderLayout.NORTH);
        center.add(split, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private void updateSeverityLabel() {
        int v = sldSeverity.getValue();
        String band = v <= 3 ? "Low" : v <= 7 ? "Medium" : "High";
        lblSeverity.setText(v + "  (" + band + ")");
    }

    private void submit() {
        String citizen = txtCitizen.getText().trim();
        if (citizen.isEmpty()) {
            UI.warn(this, "Enter the citizen's name.");
            return;
        }
        Complaint complaint = new Complaint(nextId++, citizen, (String) cmbCategory.getSelectedItem(),
                sldSeverity.getValue(), txtDescription.getText().trim());

        // Chain of Responsibility: the panel only talks to the FIRST handler.
        firstHandler.handle(complaint);

        complaints.add(complaint);
        tableModel.addRow(new Object[]{complaint.getId(), complaint.getCitizen(), complaint.getCategory(),
            complaint.getSeverity(), complaint.getHandledBy()});
        int last = tableModel.getRowCount() - 1;
        table.setRowSelectionInterval(last, last);
        txtTrace.setText(complaint.getTraceText());
        clearForm();
    }

    private void clearForm() {
        txtCitizen.setText("");
        txtDescription.setText("");
        cmbCategory.setSelectedIndex(0);
        sldSeverity.setValue(5);
    }
}
