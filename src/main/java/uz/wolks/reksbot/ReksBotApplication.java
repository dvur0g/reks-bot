package uz.wolks.reksbot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ReksBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReksBotApplication.class, args);
    }
}
