package ge.evmittesttask.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class TelegramAuthValidator {
    //ToDo Пробросить бот-токен из .env через Value
    private final String botToken;

    public TelegramAuthValidator(String botToken) {
        this.botToken = botToken;
    }

    public boolean validate(String initData) {
        try {
            Map<String, String> params = parseInitData(initData);

            if (!params.containsKey("hash") || !params.containsKey("auth_date")) {
                return false;
            }

            long authDate = Long.parseLong(params.get("auth_date"));
            if (Instant.now().getEpochSecond() - authDate > 86400) {
                return false;
            }

            return verifySignature(params);
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
