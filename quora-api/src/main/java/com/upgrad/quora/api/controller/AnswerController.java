package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
public class AnswerController {

    @Autowired
    AnswerService answerService;

    @RequestMapping(method = RequestMethod.POST, path="/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> postAnswer(@RequestBody AnswerRequest answerRequest, @PathVariable("questionId") String questionId, @RequestHeader("Authorization") String authorization)
    {
        System.out.println("post answer");
        String token = authorization.split("Bearer ")[1];
        AnswerEntity answerEntity=new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setAnswer(answerRequest.getAnswer());
        AnswerEntity createdAnswerEntity= answerService.postAnswer(answerEntity,questionId,token);
        AnswerResponse answerResponse= new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER POSTED SUCCESSFULLY");
        return new ResponseEntity<AnswerResponse>(answerResponse,HttpStatus.CREATED);
    }
}
