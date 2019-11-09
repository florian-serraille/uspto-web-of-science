package labs.com.usptodatabasegenerator.webofscience.service;

import labs.com.usptodatabasegenerator.uspto.batch.JobEnum;
import labs.com.usptodatabasegenerator.uspto.batch.JobRunner;
import labs.com.usptodatabasegenerator.uspto.domain.service.UsptoPatentService;
import labs.com.usptodatabasegenerator.uspto.dto.JobStatusDTO;
import labs.com.usptodatabasegenerator.webofscience.dto.ExportParametersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

@Service
public class ExportFileService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final UsptoPatentService usptoPatentService;
    private final ApplicationContext applicationContext;
    private final JobRunner jobRunner;
    private final String exportDirectory;

    private JobExecution exportFileJobExecution;
    private ExportParametersDTO exportParametersDTO;

    public ExportFileService(@Value("${directory.export}") String exportDirectory,
                             UsptoPatentService usptoPatentService,
                             ApplicationContext applicationContext,
                             JobRunner jobRunner) {
        this.exportDirectory = exportDirectory;
        this.usptoPatentService = usptoPatentService;
        this.applicationContext = applicationContext;
        this.jobRunner = jobRunner;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public Boolean createRessource(ExportParametersDTO exportParametersDTO) {

        Boolean result = Boolean.FALSE;

        checkNameFile(exportParametersDTO);
        this.exportParametersDTO = exportParametersDTO;

        if (usptoPatentService.existResultFromQuery()) {
            result = runExtractorJob(exportParametersDTO);
        } else {
            LOG.info("No result found for query");
        }

        return result;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public InputStreamResource buildInputStream() {

        InputStreamResource inputStreamResource = null;

        try {
            Resource resource = getResource(exportDirectory + File.separator + exportParametersDTO.getFileName());
            inputStreamResource = new InputStreamResource(new FileInputStream(resource.getFile()));
        } catch (IOException e) {
            LOG.error("Error building input stream from export file: " + e);
        }

        return inputStreamResource;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public String getExportFileName() {
        return exportParametersDTO.getFileName();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public JobStatusDTO getJobStatusDTO() {
        return jobRunner.getJobStatusDTO(exportFileJobExecution);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    private void checkNameFile(ExportParametersDTO exportParametersDTO) {
        if (exportParametersDTO.getFileName().isEmpty()) {
            exportParametersDTO.setFileName("uspto-search");
        }
    }

    private Boolean runExtractorJob(ExportParametersDTO exportParametersDTO) {

        Boolean result;
        

        if (jobRunner.checkJobAlreadyRunning(exportFileJobExecution)) {
            result = Boolean.FALSE;
        } else {
            exportFileJobExecution = null;
            JobParameters jobParameters = buildJobParameter(exportParametersDTO);
            exportFileJobExecution = jobRunner.run(JobEnum.EXTRACTOR, jobParameters);
            result = (exportFileJobExecution != null) ? Boolean.TRUE : Boolean.FALSE;
        }

        return result;
    }

    private JobParameters buildJobParameter(ExportParametersDTO exportParametersDTO) {
        return new JobParametersBuilder()
                .addString("fileName", exportParametersDTO.getFileName())
                .addString("personType", exportParametersDTO.getPersonType().name())
                .addString("contentType", exportParametersDTO.getContent().name())
                .addString("referenceType", exportParametersDTO.getReference().name())
                .addDate("timestamp", new Date())
                .toJobParameters();
    }


    private Resource getResource(String fileName) throws FileNotFoundException {
        Resource resource = applicationContext.getResource("file:" + fileName);
        if (!resource.exists()) {
            throw new FileNotFoundException();
        }
        return resource;
    }
}
