package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
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

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> updateQuestion(@RequestBody QuestionEditRequest questionEditRequest, @PathVariable("questionId") String questionId, @RequestHeader("authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        QuestionEntity questionEntity=questionService.updateQuestion(questionId,questionEditRequest.getContent(),token);
        QuestionEditResponse questionEditResponse=new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION UPDATED SUCCESSFULLY");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") String questionId, @RequestHeader("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        QuestionEntity questionEntity=questionService.deleteQuestion(questionId,token);
        QuestionDeleteResponse questionDeleteResponse=new QuestionDeleteResponse().id(questionEntity.getUuid()).status("QUESTION DELETED SUCCESSFULLY");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getQuestionsByUser(@RequestHeader("Authorization") String authorization, @PathVariable("userId") String userId)
    {
        String token = authorization.split("Bearer ")[1];
        List<QuestionEntity> questionsEntityList=questionService.getQuestionsByUser(userId,token);
        List<QuestionDetailsResponse> questionDetailsResponseList=new ArrayList<QuestionDetailsResponse>();
        for(QuestionEntity entity:questionsEntityList)
        {
            if(entity.getUser().getUuid().equalsIgnoreCase(userId)) {
                QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
                questionDetailsResponse.setId(entity.getUuid());
                questionDetailsResponse.setContent(entity.getContent());
                questionDetailsResponseList.add(questionDetailsResponse);
            }
            else
                continue;
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList,HttpStatus.OK);
    }
}
