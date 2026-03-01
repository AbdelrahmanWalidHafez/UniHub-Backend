package com.unihub.auth.accountmanagement.job.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResultResponse {


    private String status;

    @JsonProperty("total_read")
    private long totalRead;


    private long writeCount;

    private long failed;

    @JsonProperty("process_failures")
    private long processFailures;

    @JsonProperty("write_failures")
    private long writeFailures;

    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    private LocalDateTime endTime;
}