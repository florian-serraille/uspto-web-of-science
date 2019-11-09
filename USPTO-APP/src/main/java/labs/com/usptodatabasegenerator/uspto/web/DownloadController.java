package labs.com.usptodatabasegenerator.uspto.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import labs.com.usptodatabasegenerator.uspto.domain.entity.file.UsptoFile;
import labs.com.usptodatabasegenerator.uspto.domain.service.DownloadService;
import labs.com.usptodatabasegenerator.uspto.dto.JobStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Api(value = "Download")
@CrossOrigin
@RestController
@RequestMapping("/uspto-generator/download")
public class DownloadController {

    private static final Logger LOG = LoggerFactory.getLogger(DownloadController.class);

    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Baixa os arquivos USPTO")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity download(@RequestParam(required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate initialDate,
                                   @RequestParam(required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finalDate) {

        LOG.info("Download requested for init date: " + initialDate + " and final date: " + finalDate);

        Boolean result = downloadService.runDownloadJob(initialDate, finalDate);
        HttpStatus status = (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).build();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Retorna o status do job, valores possiveis sao ABANDONED, COMPLETED, STARTED ,STARTING, STOPPED, STOPPING, FAILED")
    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getStatus() {

        LOG.info("Status Download USPTO File Job requested");
        JobStatusDTO jobStatusDTO = downloadService.getJobStatusDTO();
        HttpStatus status = (jobStatusDTO != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(jobStatusDTO);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Retorna o arquivo USPTO atualmente baixado")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCurrentUsptoFile() {

        UsptoFile currentUsptoFile = downloadService.getCurrentUsptoFileOnWorking();
        return (currentUsptoFile == null) ? ResponseEntity.noContent().build() : ResponseEntity.ok(currentUsptoFile);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Retorna a lista dos arquivos USPTO com seu estado")
    @GetMapping(value = "/files", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFiles() {

        List<UsptoFile> files = downloadService.getFiles();
        return (files == null) ? ResponseEntity.noContent().build() : ResponseEntity.ok(files);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Stop a execução do job de Download")
    @GetMapping(value = "/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity stop() {

        LOG.info("Download job interruption requested");

        Boolean stoped = downloadService.stopJob();
        HttpStatus status = (stoped == Boolean.TRUE) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).build();
    }
}
    
