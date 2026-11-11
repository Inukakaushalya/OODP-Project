package com.urbantech.smartcity360.patterns.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite: a City, District or Street. It keeps a list of ZoneComponent children
 * (HAS-A) and passes every command down to them.
 */
public class Zone implements ZoneComponent {

    private final String level;
    private final String name;
    private final List<ZoneComponent> children = new ArrayList<>();
    private boolean powerOn = true;

    public Zone(String level, String name) {
        this.level = level;
        this.name = name;
    }

    public void add(ZoneComponent component) {
        children.add(component);
    }

    public void remove(ZoneComponent component) {
        children.remove(component);
    }

    public List<ZoneComponent> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public double getUsage() {
        double total = 0;
        for (ZoneComponent child : children) {
            total += child.getUsage();
        }
        return total;
    }

    @Override
    public boolean isPowerOn() {
        if (children.isEmpty()) {
            return powerOn;
        }
        for (ZoneComponent child : children) {
            if (child.isPowerOn()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String reportUsage(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append(level).append(": ").append(name)
                .append(" = ").append(getUsage()).append(" kWh\n");
        for (ZoneComponent child : children) {
            sb.append(child.reportUsage(indent + "    "));
        }
        return sb.toString();
    }

    @Override
    public String shutdownPower(String indent) {
        powerOn = false;
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("Power OFF -> ").append(level).append(": ").append(name).append("\n");
        for (ZoneComponent child : children) {
            sb.append(child.shutdownPower(indent + "    "));
        }
        return sb.toString();
    }

    @Override
    public String restorePower(String indent) {
        powerOn = true;
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("Power ON  -> ").append(level).append(": ").append(name).append("\n");
        for (ZoneComponent child : children) {
            sb.append(child.restorePower(indent + "    "));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return level + ": " + name + " (" + (isPowerOn() ? "ON" : "OFF") + ")";
    }
}
