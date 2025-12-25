package com.muky.toto;

import com.muky.toto.config.TestFirebaseConfiguration;
import com.muky.toto.config.TestMailConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({TestMailConfiguration.class, TestFirebaseConfiguration.class})
class TotoApplicationTests {

	@Test
	void contextLoads() {
	}

}
