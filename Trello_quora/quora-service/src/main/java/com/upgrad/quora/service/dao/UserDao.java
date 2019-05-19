package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager entityManager;


    public UsersEntity getUserByName(UsersEntity usersEntity) {
        String username = usersEntity.getUserName();
        try {
            return entityManager.createNamedQuery("userByUsername", UsersEntity.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }

    public UsersEntity getUserByEmail(String email){
            try {
                return entityManager.createNamedQuery("userByEmail", UsersEntity.class).setParameter("email", email).getSingleResult();
            } catch (NoResultException nre) {
                return null;
            }
        }
//        entityManager.persist(usersEntity);
//        return usersEntity;
    public UsersEntity createUser(UsersEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
}

    public UsersEntity getUserByUsername(final String username){
        try {

            return entityManager.createNamedQuery("userByUsername", UsersEntity.class).setParameter("username", username).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }


    }

    public UsersEntity getUserByPassword(final String password){
        try {

            return entityManager.createNamedQuery("userByPassword", UsersEntity.class).setParameter("password", password)
                    .getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }


    }

    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity){
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    public void updateUser(final UsersEntity updatedUserEntity){
        entityManager.merge(updatedUserEntity);
    }

    public UserAuthEntity getUserAuthEntity(final String accessToken){
        try {

            return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class).setParameter("access_token", accessToken).getSingleResult();

        } catch (NoResultException nre) {

            return null;
        }

    }

    public void updateLogOutAt(final UserAuthEntity userAuthEntity){
        entityManager.merge(userAuthEntity);
    }

    public UsersEntity getUUID(Integer userID){
        try {

            return entityManager.createNamedQuery("getUserByID", UsersEntity.class).setParameter("id", userID).getSingleResult();

        } catch (NoResultException nre) {

            return null;
        }

    }


}
