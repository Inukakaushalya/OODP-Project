package com.urbantech.smartcity360.patterns.decorator;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;

/** Concrete decorator: compresses the data of the wrapped stream (java.util.zip.Deflater). */
public class CompressionDecorator extends StreamDecorator {

    public CompressionDecorator(DataStream stream) {
        super(stream);
    }

    private byte[] deflate(String text) {
        byte[] input = text.getBytes(StandardCharsets.UTF_8);
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(input);
        deflater.finish();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[256];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            out.write(buffer, 0, count);
        }
        deflater.end();
        return out.toByteArray();
    }

    @Override
    public String getDescription() {
        return this.stream.getDescription() + " + Compression";
    }

    @Override
    public String getData() {
        return Base64.getEncoder().encodeToString(deflate(this.stream.getData()));
    }

    @Override
    public String getLog() {
        int before = this.stream.getData().getBytes(StandardCharsets.UTF_8).length;
        int after = deflate(this.stream.getData()).length;
        return this.stream.getLog() + "\n[Compression] " + before + " bytes -> " + after
                + " bytes (Deflater, shown as Base64 text)";
    }
}
