package com.urbantech.smartcity360.patterns.interpreter;

import java.util.HashMap;
import java.util.Map;

/** Holds the current values of the variables used in a rule (noise, hour, parked ...). */
public class Context {

    private final Map<String, Integer> variables = new HashMap<>();

    public void set(String name, int value) {
        variables.put(name, value);
    }

    public int get(String name) {
        Integer value = variables.get(name);
        if (value == null) {
            throw new IllegalArgumentException("Unknown variable: " + name);
        }
        return value;
    }
}
