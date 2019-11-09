package labs.com.usptodatabasegenerator;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableBatchProcessing
@SpringBootApplication
public class UsptoDatabaseGeneratorApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UsptoDatabaseGeneratorApplication.class, args);
    }
}
