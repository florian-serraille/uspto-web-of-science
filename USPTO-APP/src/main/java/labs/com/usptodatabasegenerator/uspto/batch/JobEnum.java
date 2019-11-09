package labs.com.usptodatabasegenerator.uspto.batch;

import java.util.Arrays;

public enum JobEnum {

    DOWNLOAD ("downloadJob", 1),
    UNZIP ("unzipJob", 1),
    CLEANER ("cleanerJob", 1),
    LOAD ("loadJob", 1),
    UNZIP_CLEAN_LOAD ("fullJob", 3),
    EXTRACTOR("extractorJob", 1);

    private String jobName;
    private Integer stepNumber;

    JobEnum(String jobName, Integer stepNumber) {
        this.jobName = jobName;
        this.stepNumber = stepNumber;
    }

    public static Integer stepNumberFromJobName(String jobName) {
        return getJobFromName(jobName).stepNumber;
    }

    private static JobEnum getJobFromName(String jobName) {
        return Arrays.stream(JobEnum.values())
                .filter(jobEnum -> jobEnum.getJobName().equals(jobName))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public String getJobName() {
        return this.jobName;
    }
}
