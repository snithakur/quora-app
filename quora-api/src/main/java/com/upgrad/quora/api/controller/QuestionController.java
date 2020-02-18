package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class QuestionController {

    @Autowired
    QuestionService questionService;
    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> postQuestion(@RequestBody QuestionRequest question, @RequestHeader("Authorization") String authorization)
    {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(question.getContent());
        questionEntity.setDate(ZonedDateTime.now());
        String token = authorization.split("Bearer ")[1];
        QuestionEntity createdQuestionEntity=questionService.postQuestion(questionEntity,token);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid().toString()).status("QUESTION POSTED SUCCESSFULLY");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/questions/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        List<QuestionEntity> questionsEntityList=questionService.getAllQuestions(token);
        List<QuestionDetailsResponse> questionDetailsResponseList=new ArrayList<QuestionDetailsResponse>();
        for(QuestionEntity entity:questionsEntityList)
        {
            QuestionDetailsResponse questionDetailsResponse=new QuestionDetailsResponse();
            questionDetailsResponse.setId(entity.getUuid());
            questionDetailsResponse.setContent(entity.getContent());
            questionDetailsResponseList.add(questionDetailsResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList,HttpStatus.OK);
    }
}
