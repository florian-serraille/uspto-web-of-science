package labs.com.usptodatabasegenerator.uspto.batch;

import labs.com.usptodatabasegenerator.uspto.batch.clean.UsptoCleaner;
import labs.com.usptodatabasegenerator.uspto.batch.download.DownloadReader;
import labs.com.usptodatabasegenerator.uspto.batch.download.DownloadWriter;
import labs.com.usptodatabasegenerator.uspto.batch.extractor.ExtractorItemProcessor;
import labs.com.usptodatabasegenerator.uspto.batch.load.LoaderConfiguration;
import labs.com.usptodatabasegenerator.uspto.batch.load.LoaderWriter;
import labs.com.usptodatabasegenerator.uspto.batch.unzip.UnzipTasklet;
import labs.com.usptodatabasegenerator.uspto.domain.service.impl.UsptoPatentServiceImpl;

import java.util.Arrays;

public enum StepEnum {
    DOWNLOAD("downloadStep"),
    UNZIP("unzipStep"),
    CLEAN("cleanStep"),
    LOAD("loadStep"),
    EXTRACTOR("extractorStep");

    private String stepName;

    StepEnum(String stepName) {
        this.stepName = stepName;
    }

    public String getStepName() {
        return stepName;
    }

    public static Long totalItemNumberFromStepName(String stepName) {
        StepEnum step = stepEnumFromStepName(stepName);

        Long total;

        switch (step) {
            case DOWNLOAD:
                total = DownloadReader.totalItemNumber;
                break;

            case UNZIP:
                total = UnzipTasklet.totalItemNumber;
                break;

            case CLEAN:
                total = UsptoCleaner.currentItem.get();
                break;

            case LOAD:
                total = LoaderConfiguration.totalItemNumber;
                break;

            case EXTRACTOR:
                total = UsptoPatentServiceImpl.getUsptoPatendIdList();
                break;

            default:
                total = 0L;
        }

        return total;
    }

    public static Long currentItemFromStepName(String stepName) {

        StepEnum step = stepEnumFromStepName(stepName);

        Long current;

        switch (step) {
            case DOWNLOAD:
                current = DownloadWriter.currentItem;
                break;

            case UNZIP:
                current = UnzipTasklet.currentItem;
                break;

            case CLEAN:
                current = UsptoCleaner.currentItem.get();
                break;

            case LOAD:
                current = LoaderWriter.getCurentItem();
                break;

            case EXTRACTOR:
                current = ExtractorItemProcessor.currentItem;
                break;

            default:
                current = 0L;
        }

        return current;


    }

    private static StepEnum stepEnumFromStepName(String stepName) {
        return Arrays.stream(StepEnum.values())
                .filter(stepEnum -> stepEnum.getStepName().equals(stepName))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
