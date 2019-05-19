package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    EntityManager entityManager;

    public QuestionEntity findEntity(String questionId){
        try {
            return entityManager.createNamedQuery("userByQuestionId", QuestionEntity.class).setParameter("uuid", questionId).getSingleResult();
        }catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthEntity getAccessToken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class).setParameter("access_token", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    public AnswerEntity updateAnswer(AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
        return answerEntity;

    }

    public AnswerEntity getUser(String answerId) {
        try {
            return entityManager.createNamedQuery("userByAnswerId", AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AnswerEntity editAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    public void deleteUser(AnswerEntity answerEntity){
        entityManager.remove(answerEntity);
    }

    public List<AnswerEntity> findAllAnswersById(String questionId){
        try {
            QuestionEntity questionEntity = entityManager.createNamedQuery("userByQuestionId", QuestionEntity.class).setParameter("uuid", questionId).getSingleResult();
            Integer ID = questionEntity.getId();
            return entityManager.createNamedQuery("getAllAnswersById", AnswerEntity.class).setParameter("questionEntity.id", ID).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }




}
