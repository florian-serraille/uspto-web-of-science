package labs.com.usptodatabasegenerator.uspto.batch.clean;

import labs.com.usptodatabasegenerator.uspto.domain.service.UsptoFileService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class CleanerTasklet implements Tasklet {
    
    private final UsptoCleaner usptoCleaner;
    private final String unzipDirectory;
    private final String workingDirectory;
    private final UsptoFileService usptoFileService;

    public CleanerTasklet(@Value("${directory.unzip}") String unzipDirectory,
                          @Value("${directory.working}") String workingDirectory,
                          UsptoCleaner usptoCleaner, UsptoFileService usptoFileService) {
        
        this.usptoCleaner = usptoCleaner;
        this.unzipDirectory = unzipDirectory;
        this.workingDirectory = workingDirectory;
        this.usptoFileService = usptoFileService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws IOException {
        
        RepeatStatus status;
        
        Optional<Path> ressource = getRessource();

        try {
            
            Path pathFile = ressource.orElseThrow(NoSuchElementException::new);
            usptoCleaner.clean(pathFile);
            usptoFileService.setAsProcessedFromUnzipedFile(pathFile);
            status = RepeatStatus.CONTINUABLE;
            
        } catch (NoSuchElementException e){
            status = RepeatStatus.FINISHED;
        }
        return status;

    }

    private synchronized Optional<Path> getRessource() {

        Optional<Path> path;
        
        try {
            path = getFile();
            path = Optional.of(moveFile(path.orElseThrow(IOException::new)));
            
        } catch (IOException e) {
            path = Optional.empty();
        }
        
        return path;
    }

    private Path moveFile(Path path) throws IOException {
        return Files.move(path, Paths.get(workingDirectory, path.getFileName().toString()));
    }

    private Optional<Path> getFile() throws IOException {
        return Optional.of(
                Files.list(
                        Paths.get(unzipDirectory))
                        .findFirst()
                        .orElseThrow(IOException::new)
        );
    }
}
