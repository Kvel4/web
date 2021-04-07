package ru.itmo.wp.model;

import org.apache.commons.codec.binary.Base64;
import ru.itmo.wp.util.ImageUtils;

import java.util.concurrent.ThreadLocalRandom;

public class Captcha {
    private static final int LOWER_BOUND = 100, UPPER_BOUND = 999;
    private final String answer;
    private final byte[] body;

    public Captcha() {
        answer = createAnswer();
        body = Base64.encodeBase64(ImageUtils.toPng(answer));
    }

    public String getAnswer() {
        return answer;
    }

    public byte[] getBody() {
        return body;
    }

    private static String createAnswer() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(LOWER_BOUND, UPPER_BOUND + 1));
    }
}
