package com.urbantech.smartcity360.patterns.composite;

/** Leaf: a building has no children. */
public class Building implements ZoneComponent {

    private final String name;
    private final double usage;
    private boolean powerOn = true;

    public Building(String name, double usage) {
        this.name = name;
        this.usage = usage;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLevel() {
        return "Building";
    }

    @Override
    public double getUsage() {
        return powerOn ? usage : 0;
    }

    @Override
    public boolean isPowerOn() {
        return powerOn;
    }

    @Override
    public String reportUsage(String indent) {
        return indent + getLevel() + ": " + name + " = " + getUsage() + " kWh"
                + (powerOn ? "" : " (power OFF)") + "\n";
    }

    @Override
    public String shutdownPower(String indent) {
        powerOn = false;
        return indent + "Power OFF -> " + getLevel() + ": " + name + "\n";
    }

    @Override
    public String restorePower(String indent) {
        powerOn = true;
        return indent + "Power ON  -> " + getLevel() + ": " + name + "\n";
    }

    @Override
    public String toString() {
        return getLevel() + ": " + name + " [" + usage + " kWh] (" + (powerOn ? "ON" : "OFF") + ")";
    }
}
