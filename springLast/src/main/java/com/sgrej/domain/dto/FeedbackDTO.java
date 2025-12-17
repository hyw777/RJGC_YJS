package com.sgrej.domain.dto;

import lombok.Data;

@Data
public class FeedbackDTO {
    private String imageId;
    private String detectionType;
    private String modeType;
    private String results;
    private String description;
    private String suggestion;
}
