package utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

@Slf4j
public abstract class TokenGenerator {

    private static final String hexadecimalCharacters = "ABCDEF0123456789";

    public static String getAlphaNumericToken() {
        return getAlphaNumericToken(32);
    }

    public static String getAlphaNumericToken(int length) {
        log.info("Создание токена длиной {} символа в буквенно-цифровом формате", length);
        return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
    }

    public static String getHexadecimalToken(int length) {
        log.info("Создание токена длиной {} символа в шестнадцатеричном формате", length);
        StringBuilder token = new StringBuilder();
        while (token.length() < length) {
            token.append(hexadecimalCharacters.charAt(new Random().nextInt(hexadecimalCharacters.length())));
        }
        return token.toString();
    }

    public static String getHexadecimalToken() {
        return getHexadecimalToken(32);
    }

}
