package labs.com.usptodatabasegenerator.uspto.batch.download;

import labs.com.usptodatabasegenerator.uspto.domain.entity.file.UsptoFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@StepScope
public class DownloadProcessor implements ItemProcessor<LocalDate, UsptoFile> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final String rootUrl;
    private final LocalDate initialDatePattern2Constant;
    private final LocalDate initialDatePattern3Constant;
    private final String pattern1_1;
    private final String pattern1_2;
    private final String pattern2;
    private final String pattern3;

    public DownloadProcessor(@Value("${uspto.root-url}") String rootUrl,
                             @Value("${uspto.pattern2.initial-date}") String initialDatePattern2Constant,
                             @Value("${uspto.pattern3.initial-date}") String initialDatePattern3Constant,
                             @Value("${uspto.pattern1-1}") String pattern1_1,
                             @Value("${uspto.pattern1-2}") String pattern1_2,
                             @Value("${uspto.pattern2}") String pattern2,
                             @Value("${uspto.pattern3}") String pattern3){
        this.rootUrl = rootUrl;
        this.initialDatePattern2Constant = LocalDate.parse(initialDatePattern2Constant);
        this.initialDatePattern3Constant = LocalDate.parse(initialDatePattern3Constant);
        this.pattern1_1 = pattern1_1;
        this.pattern1_2 = pattern1_2;
        this.pattern2 = pattern2;
        this.pattern3 = pattern3;
    }

    @Override
    public UsptoFile process(LocalDate date){

        String fileName = buildFileName(date);

        return UsptoFile.builder()
                .name(fileName)
                .publicationDate(date)
                .url(buildUrl(date, fileName))
                .build();
    }

    private String buildFileName(LocalDate date) {

        if (isFirstPattern(date)) {
            return buildFileNameWithFirstPattern(date);
        } else if (isSecondPattern(date)) {
            return buildFileNameWithSecondPattern(date);
        } else {
            return buildFileNameWithThirdPattern(date);
        }
    }

    private boolean isSecondPattern(LocalDate date) {
        return date.isBefore(initialDatePattern3Constant);
    }

    private boolean isFirstPattern(LocalDate date) {
        return date.isBefore(initialDatePattern2Constant);
    }

    private String buildFileNameWithThirdPattern(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(pattern3));
    }

    private String buildFileNameWithSecondPattern(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(pattern2));
    }

    private String buildFileNameWithFirstPattern(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(pattern1_1)) + (date.minusWeeks(1)).format(DateTimeFormatter.ofPattern(pattern1_2));
    }

    private String buildUrl(LocalDate date, String fileName) {
        return rootUrl + "/" + date.getYear() + "/" + fileName;
    }
}
