package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserAuthEntity getAccessToken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class).setParameter("access_token", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity saveQuestion(QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getQuestionsOfAnyUser(){
        return entityManager.createNamedQuery("getAllQuestionsAnyUser", QuestionEntity.class).getResultList();
    }

    public List<QuestionEntity> getAllQuestions(String userId){
        return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).setParameter("uuid", userId).getResultList();
//        TypedQuery<QuestionEntity> query = entityManager.createQuery("SELECT q.content from QuestionEntity q ", QuestionEntity.class);
//        List<QuestionEntity> resultList = query.getResultList();
//        return resultList;
    }

    public QuestionEntity getUserByQuestionId(String questionId){
        try {
            return entityManager.createNamedQuery("userByQuestionId", QuestionEntity.class).setParameter("uuid", questionId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

//    public QuestionEntity getQuestionById(String userId){
//        try {
//            return entityManager.createNamedQuery("userByuserId", QuestionEntity.class).setParameter("uuid", userId).getSingleResult();
//        } catch (NoResultException nre) {
//            return null;
//        }
//    }

    public void updateQuestionEntity(QuestionEntity questionEntity){
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(questionEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

    }

    public void deleteQuestionEntity(String questioId){
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            QuestionEntity questionEntity1 =entityManager.find(QuestionEntity.class, questioId);
            entityManager.remove(questionEntity1);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    public UsersEntity checkUser(String userId){
        try {
            return entityManager.createNamedQuery("userByUserId", UsersEntity.class).setParameter("uuid", userId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

//    public List<QuestionEntity> getAllQuestion(String userId){
//        try {
//            return entityManager.createNamedQuery("questionsByUserId", QuestionEntity.class).setParameter("id", userId).getResultList();
//        } catch (NoResultException nre) {
//            return null;
//        }
//        TypedQuery<QuestionEntity> query = entityManager.createQuery("SELECT q from QuestionEntity q where q.uuid=userId", QuestionEntity.class);
//        List<QuestionEntity> resultList = query.getResultList();
//        return resultList;
    }


//}
