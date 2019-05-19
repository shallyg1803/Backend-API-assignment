package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    AnswerBusinessService answerBusinessService;

//    User can reply to a question by creating the answer.
    @RequestMapping(method= RequestMethod.POST , path="/question/{questionId}/answer/create", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest, @PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String autherisation) throws AuthorizationFailedException, InvalidQuestionException {
        String answer = answerRequest.getAnswer();
        AnswerEntity answerEntity = answerBusinessService.createAnswer(autherisation,questionId,answer);
        AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER CREATED");

        return new ResponseEntity(answerResponse, HttpStatus.OK);
    }

//  User can edit the answer if he has posted the answer.
    @RequestMapping(method= RequestMethod.PUT , path="/answer/edit/{answerId}", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent (final AnswerEditRequest answerEditRequest, @PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String autherisation) throws AuthorizationFailedException, InvalidQuestionException, AnswerNotFoundException {
        String content = answerEditRequest.getContent();
        AnswerEntity answerEntity = answerBusinessService.editedAnswerEntity(answerId,autherisation,content);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(answerEntity.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity(answerEditResponse, HttpStatus.OK);
    }



//    Admin or user can delete the answer if he has posted the answer.
    @RequestMapping(method= RequestMethod.DELETE , path="/answer/delete/{answerId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String autherisation ) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = answerBusinessService.deletedAnswer(answerId,autherisation);
//        answerBusinessService.deletedAnswer(answerId,autherisation);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");
//        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerId).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);

    }


//  User can get the details of all the answers for a specific question.
    @RequestMapping(method= RequestMethod.GET , path="answer/all/{questionId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion (@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String autherisation ) throws AuthorizationFailedException, InvalidQuestionException {

        List<AnswerEntity> answerEntity = answerBusinessService.getAllAnswers(questionId,autherisation);
        List<AnswerDetailsResponse> answerDetailsResponses = new LinkedList<>();
        for(AnswerEntity ans: answerEntity){
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse().id(ans.getUuid()).questionContent(ans.getQuestionEntity().getContent())
                                                        .answerContent(ans.getAns());
            answerDetailsResponses.add(answerDetailsResponse);
        }

        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponses, HttpStatus.OK);

    }
}
