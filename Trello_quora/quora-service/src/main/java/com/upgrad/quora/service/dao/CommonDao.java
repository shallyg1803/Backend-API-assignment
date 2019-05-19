package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CommonDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserAuthEntity getDetails(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class).setParameter("access_token", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UsersEntity getByUserId(final String userId) {
        try {
            return entityManager.createNamedQuery("userByUserId", UsersEntity.class).setParameter("uuid", userId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
