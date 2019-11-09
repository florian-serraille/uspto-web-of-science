package labs.com.usptodatabasegenerator.webofscience.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import labs.com.usptodatabasegenerator.uspto.domain.service.UsptoPatentService;
import labs.com.usptodatabasegenerator.uspto.dto.JobStatusDTO;
import labs.com.usptodatabasegenerator.webofscience.dto.ExportParametersDTO;
import labs.com.usptodatabasegenerator.webofscience.dto.PatentMatcherDTO;
import labs.com.usptodatabasegenerator.webofscience.service.ExportFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "USPTO Patent")
@CrossOrigin
@RestController
@RequestMapping("/uspto-generator/patent")
public class PatentController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ExportFileService exportFileService;
    private final UsptoPatentService usptoPatentService;

    public PatentController(ExportFileService exportFileService,
                            UsptoPatentService usptoPatentService) {
        this.exportFileService = exportFileService;
        this.usptoPatentService = usptoPatentService;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Retorna o numero de patentes que casam com os parametros do filtro")
    @PostMapping(value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByMatcher(@RequestBody PatentMatcherDTO patentMatcher) {

        LOG.info("\n");
        LOG.info("Searching for patent requested");
        LOG.info("Matcher: {}", patentMatcher.toString());

        return ResponseEntity.ok(usptoPatentService.findByPatentMatcher(patentMatcher));
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Executa um job que gera o arquivo formato WEB OF SCIENCE baseado na ultima consulta")
    @PostMapping(value = "generate")
    public ResponseEntity<?> generate(@RequestBody ExportParametersDTO exportParametersDTO) {

        LOG.info("\n");
        LOG.info("Generation of Web Of Science file requested");
        LOG.info("Export parameters: {}", exportParametersDTO.toString());

        Boolean result = exportFileService.createRessource(exportParametersDTO);
        HttpStatus status = (result == Boolean.TRUE) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).build();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Exporta o arquivo formato WEB OF SCIENCE baseado na ultima consulta")
    @GetMapping(value = "export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> export() {

        ResponseEntity responseEntity = null;

        LOG.info("\n");
        LOG.info("Export of Web Of Science file requested");

        InputStreamResource inputStreamResource = exportFileService.buildInputStream();
        String fileName = exportFileService.getExportFileName();

        if (inputStreamResource != null) {
            responseEntity = ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "filename=" + fileName + ".txt")
                    .body(inputStreamResource);
        } else {
            responseEntity = ResponseEntity.noContent().build();
        }

        return responseEntity;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @ApiOperation(value = "Retorna o status do job de geração de arquivo fromato WEB OF SCIENCE, valores possiveis sao ABANDONED, COMPLETED, STARTED ,STARTING, STOPPED, STOPPING, FAILED")
    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getStatus() {

        JobStatusDTO jobStatusDTO = exportFileService.getJobStatusDTO();
        HttpStatus status = (jobStatusDTO != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(jobStatusDTO);
    }
}
