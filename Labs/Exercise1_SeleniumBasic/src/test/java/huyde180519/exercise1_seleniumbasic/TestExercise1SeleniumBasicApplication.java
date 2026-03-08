package huyde180519.exercise1_seleniumbasic;

import org.springframework.boot.SpringApplication;

public class TestExercise1SeleniumBasicApplication {

    public static void main(String[] args) {
        SpringApplication.from(Exercise1SeleniumBasicApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
