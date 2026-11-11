package com.urbantech.smartcity360.patterns.decorator;

import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/** Concrete decorator: attaches an HMAC-SHA256 token so the receiver can verify the sender. */
public class AuthenticationDecorator extends StreamDecorator {

    private static final String SECRET = "SmartCity360-demo-key";

    public AuthenticationDecorator(DataStream stream) {
        super(stream);
    }

    private String createToken(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                hex.append(String.format("%02X", hash[i]));
            }
            return "AUTH-" + hex;
        } catch (Exception e) {
            throw new IllegalStateException("Could not create authentication token", e);
        }
    }

    @Override
    public String getDescription() {
        return this.stream.getDescription() + " + Authentication";
    }

    @Override
    public String getData() {
        String data = this.stream.getData();
        return createToken(data) + "|" + data;
    }

    @Override
    public String getLog() {
        return this.stream.getLog() + "\n[Authentication] HMAC-SHA256 token attached: "
                + createToken(this.stream.getData());
    }
}
