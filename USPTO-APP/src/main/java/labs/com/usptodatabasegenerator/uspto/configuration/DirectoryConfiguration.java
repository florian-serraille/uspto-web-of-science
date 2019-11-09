package labs.com.usptodatabasegenerator.uspto.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Configuration
public class DirectoryConfiguration {

    @Value("${directory.export}")
    private String export;

    @Value("${directory.unzip}")
    private String unzip;

    @Value("${directory.download}")
    private String download;

    @Value("${directory.clean}")
    private String clean;

    @Value("${directory.working}")
    private String working;
    
    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Bean
    public void buildDirectory() {
        Arrays.asList(export, unzip, download, clean, working).forEach(this::createDirectory);
    }

    private void createDirectory(String directory) {

        Path path = Paths.get(directory);

        if (!Files.isDirectory(path)) {
            try {
                LOG.info("Creating directory " + path);
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create directory", e);
            }
        }
    }

}
