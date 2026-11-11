package com.urbantech.smartcity360.patterns.decorator;

/**
 * Concrete decorator: encrypts the data of the wrapped stream.
 * A Caesar cipher is used only to keep the demo simple - it is NOT real security.
 */
public class EncryptionDecorator extends StreamDecorator {

    private static final int SHIFT = 3;

    public EncryptionDecorator(DataStream stream) {
        super(stream);
    }

    private String encrypt(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append((char) ('a' + (c - 'a' + SHIFT) % 26));
            } else if (c >= 'A' && c <= 'Z') {
                sb.append((char) ('A' + (c - 'A' + SHIFT) % 26));
            } else if (c >= '0' && c <= '9') {
                sb.append((char) ('0' + (c - '0' + SHIFT) % 10));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Override
    public String getDescription() {
        return this.stream.getDescription() + " + Encryption";
    }

    @Override
    public String getData() {
        return encrypt(this.stream.getData());
    }

    @Override
    public String getLog() {
        return this.stream.getLog() + "\n[Encryption] Caesar cipher (shift " + SHIFT
                + ") applied - demo only, not production security";
    }
}
