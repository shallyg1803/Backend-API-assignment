package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerBusinessService {

    @Autowired
    AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(String autherisation, String questionId, String answer) throws InvalidQuestionException, AuthorizationFailedException {
        QuestionEntity questionEntity = answerDao.findEntity(questionId);
        if(questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        UserAuthEntity userAuthEntity = answerDao.getAccessToken(autherisation);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        ZonedDateTime loggedOut = userAuthEntity.getLogout_at();
        if (loggedOut != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
        }
//        AnswerEntity answerEntity = questionEntity.getAnswerEntity();
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setQuestionEntity(questionEntity);
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setAns(answer);
        UsersEntity usersEntity = answerEntity.getUsersEntity();
        answerEntity.setUsersEntity(usersEntity);
        AnswerEntity answerEntity1 = answerDao.updateAnswer(answerEntity);
        return answerEntity1;


    }

//    public AnswerEntity getAnswerByAnswerId(String uuid) {
//        return answerDao.getUser(uuid);
//    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editedAnswerEntity(String answerId,String autherisation,String content) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = answerDao.getAccessToken(autherisation);
        if(userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        ZonedDateTime loggedOut = userAuthEntity.getLogout_at();
        if (loggedOut != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
        }
        UsersEntity usersEntity = userAuthEntity.getUsersEntity();
        String user = usersEntity.getUserName();
//        AnswerEntity answerEntity = getAnswerByAnswerId(answerId);
        AnswerEntity answerEntity = answerDao.getUser(answerId);
        UsersEntity user1 = answerEntity.getUsersEntity();
        String answerUser = user1.getUserName();
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        if (!(user.equals(answerUser))) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }

        answerEntity.setAns(content);
        return answerDao.editAnswer(answerEntity);


    }

//    @Transactional(propagation = Propagation.REQUIRED)
//    public void deletedAnswer(String answerId,String autherisation) throws AuthorizationFailedException, AnswerNotFoundException {
//        UserAuthEntity userAuthEntity = answerDao.getAccessToken(autherisation);
//        if(userAuthEntity != null) {
//            ZonedDateTime logOutAt = userAuthEntity.getLogout_at();
//            if (logOutAt == null) {
//                AnswerEntity answerEntity = answerDao.getUser(answerId);
//                UsersEntity answerUser = answerEntity.getUsersEntity();
//                String username = answerUser.getUserName();
//                UsersEntity usersEntity = userAuthEntity.getUsersEntity();
//                String user = usersEntity.getUserName();
//                String role = usersEntity.getRole();
//                if (answerEntity != null) {
//                    if ((username.equals(user)) || (role == "admin")) {
//                        answerDao.deleteUser(answerEntity);
//                    } else {
//                        throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
//                    }
//                } else {
//                    throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
//                }
//            } else {
//                throw new AuthorizationFailedException("ATHR-002", "'User is signed out.Sign in first to delete an answer");
//            }
//        }else{
//
//            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
//
//        }
//
//
//    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deletedAnswer(String answerId,String autherisation) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = answerDao.getAccessToken(autherisation);
        AnswerEntity answerEntity = new AnswerEntity();
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        ZonedDateTime logOutAt = userAuthEntity.getLogout_at();
        if(logOutAt!= null){
            throw new AuthorizationFailedException("ATHR-002","'User is signed out.Sign in first to delete an answer");
        }
        answerEntity = answerDao.getUser(answerId);
        UsersEntity answerUser = answerEntity.getUsersEntity();
        String username = answerUser.getUserName();
        UsersEntity usersEntity = userAuthEntity.getUsersEntity();
        String user = usersEntity.getUserName();
        String role = usersEntity.getRole();

        if((!(username.equals(user)))||(role != "admin")){
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
        if(answerEntity == null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }

        answerDao.deleteUser(answerEntity);
        return answerEntity;
    }

    public List<AnswerEntity> getAllAnswers(final String questionId,final String autherisation) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = answerDao.getAccessToken(autherisation);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        ZonedDateTime logOutAt = userAuthEntity.getLogout_at();
        if(logOutAt!= null){
            throw new AuthorizationFailedException("ATHR-002","'User is signed out.Sign in first to delete an answer");
        }
        QuestionEntity questionEntity = answerDao.findEntity(questionId);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001","The question with entered uuid whose details are to be seen does not exist");
        }
        List<AnswerEntity> answerEntities = answerDao.findAllAnswersById(questionId);
        return answerEntities;

        }
    }

