package com.sgrej.mapper;

import com.sgrej.domain.pojo.Feedback;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedbackMapper {

    @Insert("INSERT INTO feedbacks (image_id, detection, mode_type, results, description, suggestion) " +
            "VALUES (#{imageId}, #{detectionType}, #{modeType}, #{results}, #{description}, #{suggestion})")
    void insertFeedback(Feedback feedback);
}
