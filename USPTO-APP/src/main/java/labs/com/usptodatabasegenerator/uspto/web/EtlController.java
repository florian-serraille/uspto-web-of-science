package labs.com.usptodatabasegenerator.uspto.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import labs.com.usptodatabasegenerator.uspto.batch.JobEnum;
import labs.com.usptodatabasegenerator.uspto.domain.service.EtlService;
import labs.com.usptodatabasegenerator.uspto.dto.JobStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "ETL")
@CrossOrigin
@RestController
@RequestMapping("/uspto-generator/etl")
public class EtlController {

    private static final Logger LOG = LoggerFactory.getLogger(EtlController.class);

    private final EtlService etlService;

    public EtlController(EtlService etlService) {
        this.etlService = etlService;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Unzip os arquivos USPTO")
    @GetMapping(value = "extract", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity extract() {

        LOG.info("Unzip job requested");

        Boolean result = etlService.runExtractJob(JobEnum.EXTRACTOR);
        HttpStatus status = (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).build();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Prepare os arquivos USPTO para extração das patentes")
    @GetMapping(value = "transform", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity transform() {

        LOG.info("Cleaner job requested");

        Boolean result = etlService.runExtractJob(JobEnum.CLEANER);
        HttpStatus status = (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).build();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Carregue os arquivos USPTO no banco de dados")
    @GetMapping(value = "load", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity load() {

        LOG.info("Loader job requested");

        Boolean result = etlService.runExtractJob(JobEnum.LOAD);
        HttpStatus status = (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).build();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Unzip os arquivos USPTO, limpa e persist as patentes")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity etl() {

        LOG.info("ETL job requested");

        Boolean result = etlService.runExtractJob(JobEnum.UNZIP_CLEAN_LOAD);
        HttpStatus status = (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).build();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Estado do job corrente")
    @GetMapping(value = "/status")
    public ResponseEntity<?> getStatus() {
        
        JobStatusDTO jobStatusDTO = etlService.getJobStatusDTO();
        HttpStatus status = (jobStatusDTO != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(jobStatusDTO);
    }
}