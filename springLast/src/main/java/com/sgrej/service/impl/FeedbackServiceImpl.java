package com.sgrej.service.impl;

import com.sgrej.domain.dto.FeedbackDTO;
import com.sgrej.domain.pojo.Feedback;
import com.sgrej.mapper.FeedbackMapper;
import com.sgrej.service.FeedbackService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public void saveFeedback(FeedbackDTO feedbackDTO) {
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(feedbackDTO, feedback);
        feedbackMapper.insertFeedback(feedback);
    }
}
