package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionBusinessService {

    @Autowired
    QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(String content, final String autherisation) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = questionDao.getAccessToken(autherisation);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthEntity.getLogout_at()!= null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(content);
        final ZonedDateTime now = ZonedDateTime.now();
        questionEntity.setDate(now);
        QuestionEntity persistedQuestion = questionDao.saveQuestion(questionEntity);
        return persistedQuestion;



    }

    public List<QuestionEntity> getQuestions(final String autherisation) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = questionDao.getAccessToken(autherisation);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuthEntity.getLogout_at()!= null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }

        List<QuestionEntity> allQuestions = questionDao.getQuestionsOfAnyUser();

        return allQuestions;


    }
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(final String autherisation,String questionId,String content) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = questionDao.getAccessToken(autherisation);
        QuestionEntity questionEntity = new QuestionEntity();
        if(userAuthEntity != null) {
            if (userAuthEntity.getLogout_at() == null) {
                questionEntity = questionDao.getUserByQuestionId(questionId);
                if (questionEntity != null) {
                    UsersEntity user = questionEntity.getUsersEntity();
                    String questionUser = user.getUserName();
                    String userName = (userAuthEntity.getUsersEntity()).getUserName();
                    if (questionUser.equals(userName)) {
                        questionEntity.setContent(content);
                        questionDao.updateQuestionEntity(questionEntity);
                        return questionEntity;
                    } else {
                        throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
                    }
                } else {
                    throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
                }
            } else {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
            }
        }else{
                throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }


    }



    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(String autherisation,String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = questionDao.getAccessToken(autherisation);
        if(userAuthEntity != null) {
            if (userAuthEntity.getLogout_at() == null) {
                QuestionEntity questionEntity = questionDao.getUserByQuestionId(questionId);
                if (questionEntity != null) {
                    String questionUser = (questionEntity.getUsersEntity()).getUserName();
                    String user = (userAuthEntity.getUsersEntity()).getUserName();
                    String role = (questionEntity.getUsersEntity()).getRole();
                    if((questionUser == user) || (role.equals("admin"))) {
                        questionDao.deleteQuestionEntity(questionEntity);
                        return questionEntity;
                    } else {
                        throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
                    }
                } else {
                    throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
                }
            } else {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
            }
        }else{
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        }


    public List<QuestionEntity> getAllQuestions(String autherisation,String userId) throws AuthorizationFailedException, InvalidQuestionException, UserNotFoundException {
        UserAuthEntity userAuthEntity = questionDao.getAccessToken(autherisation);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthEntity.getLogout_at()!= null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");
        }
       UsersEntity user = questionDao.checkUser(userId);
        if(user == null){
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }

        List<QuestionEntity> allQuestions = questionDao.getAllQuestions(userId);
        return allQuestions;


    }
}
