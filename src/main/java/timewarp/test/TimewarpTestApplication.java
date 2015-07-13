package timewarp.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimewarpTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimewarpTestApplication.class, args);
    }
}
