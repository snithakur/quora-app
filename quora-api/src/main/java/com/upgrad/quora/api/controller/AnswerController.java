package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
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
public class AnswerController {

    @Autowired
    AnswerService answerService;

    @RequestMapping(method = RequestMethod.POST, path="/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> postAnswer(@RequestBody AnswerRequest answerRequest, @PathVariable("questionId") String questionId, @RequestHeader("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        AnswerEntity answerEntity=new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setAnswer(answerRequest.getAnswer());
        AnswerEntity createdAnswerEntity= answerService.postAnswer(answerEntity,questionId,token);
        AnswerResponse answerResponse= new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER POSTED SUCCESSFULLY");
        return new ResponseEntity<AnswerResponse>(answerResponse,HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path="/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> updateAnswer(@RequestBody AnswerEditRequest answerEditRequest, @PathVariable("answerId") String answerId, @RequestHeader("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        AnswerEntity updatedEntity=answerService.updateAnswer(answerEditRequest.getContent(),answerId,token);
        AnswerEditResponse answerEditResponse=new AnswerEditResponse().id(updatedEntity.getUuid()).status("ANSWER UPDATED SUCCESSFULLY");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path="/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") String answerId, @RequestHeader("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        AnswerEntity updatedEntity=answerService.deleteAnswer(answerId,token);
        AnswerDeleteResponse answerDeleteResponse=new AnswerDeleteResponse().id(updatedEntity.getUuid()).status("ANSWER UPDATED SUCCESSFULLY");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAnswersByQuestion(@PathVariable("questionId") String questionId, @RequestHeader("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        List<AnswerEntity> answerEntityList=answerService.getAnswersByQuestion(token);
        List<AnswerDetailsResponse> answerDetailsResponsesList=new ArrayList<AnswerDetailsResponse>();
        for(AnswerEntity entity:answerEntityList)
        {
            if(entity.getQuestion().getUuid().equalsIgnoreCase(questionId)) {
                AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();
                answerDetailsResponse.setId(entity.getUuid());
                answerDetailsResponse.setQuestionContent(entity.getQuestion().getContent());
                answerDetailsResponse.setAnswerContent(entity.getAnswer());
                answerDetailsResponsesList.add(answerDetailsResponse);
            }
            else
                continue;
        }
        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponsesList,HttpStatus.OK);
    }
}
