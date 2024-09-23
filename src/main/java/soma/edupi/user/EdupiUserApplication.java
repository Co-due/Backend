package soma.edupi.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EdupiUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdupiUserApplication.class, args);
    }

}
