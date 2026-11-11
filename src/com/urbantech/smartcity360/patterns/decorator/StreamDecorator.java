package com.urbantech.smartcity360.patterns.decorator;

/**
 * Abstract decorator: it IS a DataStream and it HAS a DataStream (HAS-A in the class diagram).
 * Concrete decorators wrap another stream and add one extra behaviour.
 */
public abstract class StreamDecorator implements DataStream {

    protected DataStream stream;

    public StreamDecorator(DataStream stream) {
        this.stream = stream;
    }
}
