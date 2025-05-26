package ge.evmittesttask.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TelegramAuthValidator {

    @Value("${telegram.bot.token}")
    private String botToken;

    public boolean validate(String initData) {
        log.info("Начало работы метода валидации данных");
        try {
            Map<String, String> params = parseInitData(initData);

            if (!params.containsKey("hash") || !params.containsKey("auth_date")) {
                log.warn("Нет данных hash или auth_date");
                return false;
            }

            long authDate = Long.parseLong(params.get("auth_date"));
            if (Instant.now().getEpochSecond() - authDate > 86400) {
                log.warn("auth_date не прошёл проверку");
                return false;
            }

//            return verifySignature(params);
            if (verifySignature(params)) {
                log.info("Подпись прошла верификацию");
                return true;
            } else {
                log.warn("Подпись не прошла верификацию");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, String> parseInitData(String initData) {
        return Arrays.stream(initData.split("&"))
                .map(pair -> pair.split("=", 2))
                .filter(kv -> kv.length == 2)
                .collect(Collectors.toMap(
                        kv -> kv[0],
                        kv -> kv[1],
                        (oldVal, newVal) -> newVal
                ));
    }

    private boolean verifySignature(Map<String, String> params) throws NoSuchAlgorithmException, InvalidKeyException {
        log.info("Начало работы метода верификации подписи");
        String receivedHash = params.get("hash");
        String dataCheckString = params.entrySet().stream()
                .filter(e -> !e.getKey().equals("hash"))
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));

        byte[] secretKey = calculate("WebAppData", botToken);
        String calculatedHash = calculate(dataCheckString, secretKey);
        return calculatedHash.equals(receivedHash);
    }

    public static byte[] calculate(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String calculate(String data, byte[] secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
