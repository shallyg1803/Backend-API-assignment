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
         UsersEntity usersEntity = entityManager.createNamedQuery("userByUserId", UsersEntity.class).setParameter("uuid", userId).getSingleResult();
         Integer ID = usersEntity.getId();
        return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).setParameter("usersEntity.id", ID).getResultList();
   }

    public QuestionEntity getUserByQuestionId(String questionId){
        try {
            return entityManager.createNamedQuery("userByQuestionId", QuestionEntity.class).setParameter("uuid", questionId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


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

    public void deleteQuestionEntity(QuestionEntity questionEntity){
        entityManager.remove(questionEntity);
    }


    public UsersEntity checkUser(String userId){
        try {
            return entityManager.createNamedQuery("userByUserId", UsersEntity.class).setParameter("uuid", userId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}



