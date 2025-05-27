package ge.evmittesttask;

import ge.evmittesttask.security.TelegramAuthValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramAuthValidatorTest {

    @InjectMocks
    private TelegramAuthValidator validator;

    private final String validHash = "valid_hash";
    private final String validInitData = "query_id=test_query&auth_date=" +
            Instant.now().getEpochSecond() + "&hash=" + validHash;

    @Test
    void validate_ShouldReturnTrueForValidData() throws Exception {
        String botToken = "test_bot_token";
        ReflectionTestUtils.setField(validator, "botToken", botToken);

        try (var mocked = mockStatic(TelegramAuthValidator.class)) {
            mocked.when(() -> TelegramAuthValidator.calculateHash(any(), any()))
                    .thenReturn(validHash);

            assertTrue(validator.validate(validInitData));
        }
    }

    @Test
    void validate_ShouldReturnFalseWhenMissingHashOrAuthDate() {
        assertFalse(validator.validate("query_id=test_query&auth_date=123"));
        assertFalse(validator.validate("query_id=test_query&hash=valid_hash"));
    }

    @Test
    void validate_ShouldReturnFalseForExpiredAuthDate() {
        String expiredData = "query_id=test_query&auth_date=" +
                (Instant.now().getEpochSecond() - 86401) + "&hash=valid_hash";
        assertFalse(validator.validate(expiredData));
    }

    @Test
    void parseInitData_ShouldCorrectlyParseParameters() {
        Map<String, String> expected = new HashMap<>();
        expected.put("query_id", "test_query");
        expected.put("auth_date", "1234567890");
        expected.put("hash", "test_hash");

        String testData = "query_id=test_query&auth_date=1234567890&hash=test_hash";
        assertEquals(expected, validator.parseInitData(testData));
    }
}