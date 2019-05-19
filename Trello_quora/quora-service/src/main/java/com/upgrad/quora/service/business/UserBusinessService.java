package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UsersEntity signup(UsersEntity usersEntity) throws SignUpRestrictedException {
        String user = usersEntity.getUserName();
        String email = usersEntity.getEmail();
        UsersEntity userName = userDao.getUserByUsername(user);
        if(userName != null){
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }

        UsersEntity emailUser = userDao.getUserByEmail(email);
        if(emailUser != null){
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }

        String[] encryptedText = passwordCryptographyProvider.encrypt(usersEntity.getPassword());
        usersEntity.setSalt(encryptedText[0]);
        usersEntity.setPassword(encryptedText[1]);
        userDao.createUser(usersEntity);
        return usersEntity;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signin(final String username, final String password) throws AuthenticationFailedException {
        {
            UsersEntity usersEntity = userDao.getUserByUsername(username);
            if(usersEntity == null){
                throw new AuthenticationFailedException("ATH-001", "This username does not exist ");
            }

            final String encryptedPassword = passwordCryptographyProvider.encrypt(password, usersEntity.getSalt());
            if(encryptedPassword.equals(usersEntity.getPassword())){
                JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
                UserAuthEntity userAuthToken = new UserAuthEntity();
                userAuthToken.setUsersEntity(usersEntity);

                final ZonedDateTime now = ZonedDateTime.now();
                final ZonedDateTime expiresAt = now.plusHours(8);
                userAuthToken.setAccess_token(jwtTokenProvider.generateToken(usersEntity.getUuid(), now, expiresAt));

                userAuthToken.setLogin_at(now);
                userAuthToken.setExpires_at(expiresAt);

                userDao.createAuthToken(userAuthToken);

                userAuthToken.setLogout_at(now);
                userDao.updateUser(usersEntity);
                return userAuthToken;

            }
            else{
                throw new AuthenticationFailedException("ATH-002", "Password Failed");
            }

            }


    }

    public UsersEntity getUser(final String bearerToken ) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthEntity(bearerToken);

        if(userAuthEntity == null){
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }else {
            final ZonedDateTime now = ZonedDateTime.now();
            userAuthEntity.setLogout_at(now);
            userDao.updateLogOutAt(userAuthEntity);
            Integer userID = userAuthEntity.getID();
            UsersEntity usersEntity = userDao.getUUID(userID);
            return usersEntity;
        }

    }
    public UsersEntity getUserFromToken(String authorization) throws SignOutRestrictedException{
        String [] bearerToken = authorization.split("Bearer ");
        if(bearerToken[0] == "Bearer"){
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        UserAuthEntity userAuthEntity = userDao.getUserAuthEntity(bearerToken[1]);
        final ZonedDateTime now = ZonedDateTime.now();
        userAuthEntity.setLogout_at(now);
        userDao.updateLogOutAt(userAuthEntity);
//        Integer userID = userAuthEntity.getID();
//        UsersEntity usersEntity = userDao.getUUID(userID);
        UsersEntity usersEntity = userAuthEntity.getUsersEntity();
        return usersEntity;

    }
}
