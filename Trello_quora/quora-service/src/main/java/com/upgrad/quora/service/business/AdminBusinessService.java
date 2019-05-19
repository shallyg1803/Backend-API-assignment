package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AdminDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminBusinessService {

    @Autowired
    AdminDao adminDao;

    public UsersEntity getUserDetails(final String userId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthEntity userAuthEntity = adminDao.getDetails(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        UsersEntity usersEntity = adminDao.getByUserId(userId);
        if(usersEntity == null){
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }

        return usersEntity;
        }

    public UsersEntity deleteUser(final String userId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthEntity userAuthEntity = adminDao.getAuthToken(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthEntity.getLogout_at() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out");
        }

        UsersEntity usersEntity = userAuthEntity.getUsersEntity();
        String role = usersEntity.getRole();
        UsersEntity userRole = adminDao.getUserByRole(role);
        if(userRole.equals("nonadmin")){
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }

        UsersEntity usersEntityId = adminDao.getByUserId(userId);
        if(usersEntityId == null){
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }

        adminDao.deleteFromDB(usersEntity);
        return usersEntity;
    }

}
