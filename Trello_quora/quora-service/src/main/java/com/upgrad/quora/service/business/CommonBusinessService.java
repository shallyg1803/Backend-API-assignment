package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.CommonDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonBusinessService {

    @Autowired
    CommonDao commonDao;
    public UsersEntity getUserDetails(final String userId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthEntity userAuthEntity = commonDao.getDetails(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuthEntity.getLogout_at() != null){

            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }
        UsersEntity usersEntity = commonDao.getByUserId(userId);
        if(usersEntity == null){
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }

        return usersEntity;
    }
}
