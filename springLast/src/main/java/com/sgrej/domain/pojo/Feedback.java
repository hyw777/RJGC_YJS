package com.sgrej.domain.pojo;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Feedback {
    private Integer id;
    private String imageId;
    private String detectionType;
    private String modeType;
    private String results;
    private String description;
    private String suggestion;
    private Timestamp createdAt;
}
