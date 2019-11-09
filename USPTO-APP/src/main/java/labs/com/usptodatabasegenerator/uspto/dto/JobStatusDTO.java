package labs.com.usptodatabasegenerator.uspto.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class JobStatusDTO {
    private String jobName;
    private Date startTime;
    private Date endTime;
    private String status;
    private Integer stepNumbers;
    private List<StepStatusDTO> stepsStatus;

    @Data
    @Builder
    static class StepStatusDTO{
        private String stepName;
        private Date startTime;
        private Date endTime;
        private String status;
        private Long currentItem;
        private Long itemsTotalNumber;
    }

}
