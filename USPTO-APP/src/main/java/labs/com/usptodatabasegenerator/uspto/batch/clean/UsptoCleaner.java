package labs.com.usptodatabasegenerator.uspto.batch.clean;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Scope("prototype")
public class UsptoCleaner {

    public static AtomicLong currentItem = new AtomicLong();
    
    private static final String OPENING_CLAIM = "<claim id=";
    private static final String CLOSING_CLAIM = "</claim>";

    private static final String OPENING_CLAIM_TEXT = "<claim-text>";
    private static final String CLOSING_CLAIM_TEXT = "</claim-text>";

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final String formattedDirectory;
    private final String workingDirectory;
    private final String separator;

    public UsptoCleaner(@Value("${directory.working}") String workingDirectory,
                        @Value("${directory.clean}") String formattedDirectory) {
        this.formattedDirectory = formattedDirectory;
        this.workingDirectory = workingDirectory;
        separator = System.lineSeparator();
    }

    void clean(Path rawFile) {
        
        final File temporaryFile = new File(workingDirectory, String.valueOf(Math.random()));
        Boolean claimFlag = Boolean.FALSE;
        String fileName = temporaryFile.getName();
        boolean valid = Boolean.FALSE;
        int fileCount = 0;


        LOG.info("Cleaning up " + rawFile.toString());

        try (final LineIterator it = FileUtils.lineIterator(rawFile.toFile())) {

            while (it.hasNext()) {

                String line = getNextLine(it);

                if (isXmlDeclaration(line)) {
                    extractAvailablePatent(temporaryFile, fileName, fileCount, valid);
                    valid = setNotValid();
                } else if (isXMLRoot(line)) {
                    fileName = processRootLine(line);
                    valid = setValid();
                } else {
                    String lineBeforeFormat = line;
                    line = formatClaims(line, claimFlag);
                    claimFlag = updateClaimFlag(lineBeforeFormat, line);
                }
                writeLine(temporaryFile, line);
                fileCount = incrementFileCount(fileCount);
            }

            extractPatent(temporaryFile, fileName, valid);
            cleanUp(rawFile, temporaryFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info(rawFile.toString() + " clean");

    }

    private boolean setNotValid() {
        return Boolean.FALSE;
    }

    private boolean setValid() {
        return Boolean.TRUE;
    }

    private Boolean updateClaimFlag(String lineBeforeFormat, String line) {
        return !lineBeforeFormat.equals(line) && !line.startsWith(CLOSING_CLAIM_TEXT);
    }

    private void cleanUp(Path rawFile, File temporaryFile) {
        FileUtils.deleteQuietly(temporaryFile);
        FileUtils.deleteQuietly(rawFile.toFile());
    }

    private void writeLine(File temporaryFile, String line) throws IOException {
        FileUtils.writeStringToFile(temporaryFile, line + separator, StandardCharsets.UTF_8, Boolean.TRUE);

    }

    private String formatClaims(String line, Boolean claimFlag) {

        if (line.contains(OPENING_CLAIM)) {
            line = line.concat(OPENING_CLAIM_TEXT);

        } else if (line.contains(CLOSING_CLAIM)) {
            line = CLOSING_CLAIM_TEXT.concat(line);
            
        } else if (claimFlag == Boolean.TRUE) {
            line = removeTags(line);
        }

        return line;
    }

    private int incrementFileCount(int fileCount) {
        return ++fileCount;
    }

    // Remove todos os tags XML da linha
    private String removeTags(String line) {
        return RegExUtils.removeAll(line, "<[^>]+>");
    }

    // Se a TAG root do arquivo foi alcancando
    // Então podemos considerar o arquivo pronto para ser extraido
    private String processRootLine(String line) {
        return retrieveFileName(line);
    }

    private boolean isXMLRoot(String line) {
        return line.startsWith("<us-patent-grant");
    }

    private String retrieveFileName(String line) {
        return StringUtils.substringBetween(line, "file=\"", "\"");
    }

    // Se não é a primeira patente processada e que o registro é um XML Declaration
    // Então quer dizer que chegamos no fim da patente precedente e o arquivo precisa ser cortado  
    private void extractAvailablePatent(File temporaryFile, String fileName, int fileCount, boolean valid) {
        if (isNotFirstPatent(fileCount)) {
            extractPatent(temporaryFile, fileName, valid);
        }
    }

    private void extractPatent(File temporaryFile, String fileName, boolean valid) {
        try {
            if (valid) {
                LOG.info("Generating... " + fileName);
                FileUtils.moveFile(temporaryFile, new File(formattedDirectory, fileName));
            } else {
                LOG.info("Deleting... " + fileName);
                FileUtils.deleteQuietly(temporaryFile);
            }
            currentItem.getAndIncrement();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isNotFirstPatent(int fileCount) {
        return fileCount > 0;
    }

    private boolean isXmlDeclaration(String line) {
        return line.contains("<?xml version=");
    }

    private String getNextLine(LineIterator it) {
        return it.nextLine();
    }
}