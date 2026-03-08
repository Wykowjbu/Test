package huyde180519.exercise1_seleniumbasic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class Exercise1SeleniumBasicApplicationTests {

    @Test
    void contextLoads() {
    }

}
