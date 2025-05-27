package ge.evmittesttask;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "telegram.bot.token=test_bot_token")
class EvMitTestTaskApplicationTests {

	@Test
	void contextLoads() {
	}

}
