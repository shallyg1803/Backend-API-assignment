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
            return entityManager.createNamedQuery("findQuestionById", QuestionEntity.class).setParameter("uuid", questionId).getSingleResult();
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

    public AnswerEntity getUser(final String answerId) {
        try {
            return entityManager.createNamedQuery("userByAnswerId", AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void editAnswer(AnswerEntity answerEntity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(answerEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

    }

    public void deleteUser(String answerId){
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            AnswerEntity answerEntity =entityManager.find(AnswerEntity.class, answerId);
            entityManager.remove(answerEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    public List<AnswerEntity> findAllAnswersById(String questionId){
        TypedQuery<AnswerEntity> query = entityManager.createQuery("SELECT u.content from AnswerEntity u where u.question_id=questionId", AnswerEntity.class);
        List<AnswerEntity> resultList = query.getResultList();
        return resultList;
    }




}
