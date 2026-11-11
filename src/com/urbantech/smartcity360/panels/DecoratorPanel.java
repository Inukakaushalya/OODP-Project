package com.urbantech.smartcity360.panels;

import com.urbantech.smartcity360.patterns.decorator.AuthenticationDecorator;
import com.urbantech.smartcity360.patterns.decorator.CompressionDecorator;
import com.urbantech.smartcity360.patterns.decorator.DataStream;
import com.urbantech.smartcity360.patterns.decorator.EncryptionDecorator;
import com.urbantech.smartcity360.patterns.decorator.EnergyMeterStream;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/** Part A - Dynamic security wrappers for data streams (Decorator pattern). */
public class DecoratorPanel extends JPanel {

    private final JTextField txtMeterId = new JTextField("EM-1001");
    private final JTextField txtReading = new JTextField("245.5");
    private final JCheckBox chkCompression = new JCheckBox("Compression", true);
    private final JCheckBox chkEncryption = new JCheckBox("Encryption", true);
    private final JCheckBox chkAuthentication = new JCheckBox("Authentication", true);
    private final JTextArea txtOutput = UI.outputArea();

    public DecoratorPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        add(UI.header("Secure energy data stream", "Part A  |  Decorator pattern"), BorderLayout.NORTH);

        JPanel form = UI.formPanel("Energy meter data");
        UI.addRow(form, 0, "Meter ID", txtMeterId);
        UI.addRow(form, 1, "Meter reading (kWh)", txtReading);

        JPanel layers = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        layers.add(chkCompression);
        layers.add(chkEncryption);
        layers.add(chkAuthentication);
        UI.addRow(form, 2, "Security layers", layers);

        JLabel hint = new JLabel("Layers are always wrapped in this order: Compression, then Encryption, then Authentication.");
        hint.setForeground(UI.MUTED);
        UI.addRow(form, 3, "", hint);

        JButton btnProcess = new JButton("Process data");
        UI.primary(btnProcess);
        JButton btnClear = new JButton("Clear");
        btnProcess.addActionListener(e -> process());
        btnClear.addActionListener(e -> {
            txtMeterId.setText("");
            txtReading.setText("");
            txtOutput.setText("");
        });
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.add(btnProcess);
        buttons.add(btnClear);
        UI.addRow(form, 4, "", buttons);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.add(form, BorderLayout.NORTH);
        center.add(UI.scroll(txtOutput, "Output"), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private void process() {
        String meterId = txtMeterId.getText().trim();
        if (meterId.isEmpty()) {
            UI.warn(this, "Enter a meter ID, for example EM-1001.");
            return;
        }
        double reading;
        try {
            reading = Double.parseDouble(txtReading.getText().trim());
            if (reading < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            UI.warn(this, "Enter the meter reading as a positive number, for example 245.5.");
            return;
        }

        // Decorator pattern: wrap the same object again and again, the core class never changes.
        DataStream stream = new EnergyMeterStream(meterId, reading);
        if (chkCompression.isSelected()) {
            stream = new CompressionDecorator(stream);
        }
        if (chkEncryption.isSelected()) {
            stream = new EncryptionDecorator(stream);
        }
        if (chkAuthentication.isSelected()) {
            stream = new AuthenticationDecorator(stream);
        }

        txtOutput.setText("Stream: " + stream.getDescription() + "\n\n"
                + stream.getLog() + "\n\nFinal data:\n" + stream.getData());
        txtOutput.setCaretPosition(0);
    }
}
