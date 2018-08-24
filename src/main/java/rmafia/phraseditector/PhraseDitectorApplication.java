package rmafia.phraseditector;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PhraseDitectorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
        SpringApplication.run(PhraseDitectorApplication.class, args);
    }

}
