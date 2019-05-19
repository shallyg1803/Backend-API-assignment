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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionBusinessService {

    @Autowired
    QuestionDao questionDao;

    public QuestionEntity createQuestion(String content, final String autherisation) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = questionDao.getAccessToken(autherisation);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthEntity.getLogout_at()==null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(content);
        final ZonedDateTime now = ZonedDateTime.now();
        questionEntity.setDate(now);
        questionEntity.setUuid(UUID.randomUUID().toString());
        QuestionEntity persistedQuestio = questionDao.saveQuestion(questionEntity);
        return persistedQuestio;



    }

    public List<QuestionEntity> getQuestions(final String autherisation) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = questionDao.getAccessToken(autherisation);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuthEntity.getLogout_at()==null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }

        List<QuestionEntity> allQuestions = questionDao.getQuestionsOfAnyUser();

        return allQuestions;


    }

    public QuestionEntity editQuestion(final String autherisation,String questionId,String content) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = questionDao.getAccessToken(autherisation);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuthEntity.getLogout_at()==null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }

        QuestionEntity questionEntity = questionDao.getUserByQuestionId(questionId);
        UsersEntity user = questionEntity.getUsersEntity();
        UsersEntity usersEntity = userAuthEntity.getUsersEntity();
        if(usersEntity != user){
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }

        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        questionEntity.setContent(content);
        questionDao.updateQuestionEntity(questionEntity);
        return questionEntity;


    }

    public QuestionEntity deleteQuestion(String autherisation,String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = questionDao.getAccessToken(autherisation);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthEntity.getLogout_at()==null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }
        QuestionEntity questionEntity = questionDao.getUserByQuestionId(questionId);
        UsersEntity user = questionEntity.getUsersEntity();
//        String questionOwner = user.getUserName();
        UsersEntity usersEntity = userAuthEntity.getUsersEntity();
        String role = usersEntity.getRole();
//        String userName = usersEntity.getUserName();
        if((usersEntity != user)||(!(role.equals("admin")))){
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
//        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
//        UsersEntity usersEntity = userAuthEntity.getUsersEntity();
//        UsersEntity user = questionEntity.getUsersEntity();
//        String role = usersEntity.getRole();
//        if(usersEntity != user){
//            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can delete the question");
//        }
//        if(role.equals("nonadmin")){
//            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can delete the question");
//        }

        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        questionDao.deleteQuestionEntity(questionId);
        return questionEntity;


    }

    public List<QuestionEntity> getAllQuestion(String autherisation,String userId) throws AuthorizationFailedException, InvalidQuestionException, UserNotFoundException {
        UserAuthEntity userAuthEntity = questionDao.getAccessToken(autherisation);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthEntity.getLogout_at()==null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }
//        QuestionEntity questionEntity = questionDao.getUserByQuestionId(questionId);
//        UsersEntity usersEntity = userAuthEntity.getUsersEntity();
//        String uuid = usersEntity.getUuid();
        UsersEntity user = questionDao.checkUser(userId);
        if(user == null){
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
//        List<QuestionEntity> allQuestions = user.getQuestions();
        List<QuestionEntity> allQuestions = questionDao.getAllQuestions(userId);
        return allQuestions;


    }
}
