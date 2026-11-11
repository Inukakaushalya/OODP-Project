package com.urbantech.smartcity360.patterns.composite;

/**
 * Component interface of the Composite pattern (Part B).
 * A single building and a whole city are controlled through the same methods.
 * The indent parameter is only used to format the text report.
 */
public interface ZoneComponent {

    String getName();

    String getLevel();

    double getUsage();

    boolean isPowerOn();

    String reportUsage(String indent);

    String shutdownPower(String indent);

    String restorePower(String indent);
}
