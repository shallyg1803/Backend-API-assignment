package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    QuestionBusinessService questionBusinessService;

    @RequestMapping(method= RequestMethod.POST , path="/question/create", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader("authorization") final String autherisation) throws AuthorizationFailedException {
        String content = questionRequest.getContent();
        QuestionEntity questionEntity = questionBusinessService.createQuestion(autherisation, content);
        QuestionResponse questionResponse = new QuestionResponse().id(questionEntity.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity(questionResponse, HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.GET , path="/question/all", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String autherisation) throws AuthorizationFailedException {
        List<QuestionEntity> questionsList = questionBusinessService.getQuestions(autherisation);
        List<QuestionDetailsResponse> questionDetailsResponse = new LinkedList<>();
        for(QuestionEntity q:questionsList){
            QuestionDetailsResponse qDetailsResponse = new QuestionDetailsResponse().id(q.getUuid()).content(q.getContent());
            questionDetailsResponse.add(qDetailsResponse);
        }
        return new ResponseEntity(questionDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.PUT , path="/question/edit/{questionId}", consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent (final QuestionEditRequest questionEditRequest, @PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String autherisation) throws AuthorizationFailedException, InvalidQuestionException {
        String content = questionEditRequest.getContent();
        QuestionEntity questionEntity = questionBusinessService.editQuestion(autherisation,questionId,content);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");

        return new ResponseEntity(questionEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.DELETE , path="/question/delete/{questionId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String autherisation) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionBusinessService.deleteQuestion(autherisation,questionId);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid()).status("QUESTION DELETED");

        return new ResponseEntity(questionDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.GET , path="question/all/{userId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser (@PathVariable("userId") final String userId, @RequestHeader("authorization") final String autherisation) throws AuthorizationFailedException, InvalidQuestionException, UserNotFoundException {
        List<QuestionEntity> questionEntity = questionBusinessService.getAllQuestions(autherisation,userId);
        List<QuestionDetailsResponse> questionDetailsResponse = new LinkedList<>();
        for(QuestionEntity q: questionEntity){
            QuestionDetailsResponse questionDetailsResponse1 = new QuestionDetailsResponse().id(q.getUuid()).content(q.getContent());
            questionDetailsResponse.add(questionDetailsResponse1);
        }

        return new ResponseEntity(questionDetailsResponse, HttpStatus.OK);
    }

}
