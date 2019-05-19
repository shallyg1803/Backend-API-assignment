package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    @RequestMapping(method= RequestMethod.POST, path="/user/signup", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

        final UsersEntity usersEntity = new UsersEntity();
        usersEntity.setUuid(UUID.randomUUID().toString());
        usersEntity.setFirstName(signupUserRequest.getFirstName());
        usersEntity.setLastName(signupUserRequest.getLastName());
        usersEntity.setUserName(signupUserRequest.getUserName());
        usersEntity.setEmail(signupUserRequest.getEmailAddress());
        usersEntity.setPassword(signupUserRequest.getPassword());
        usersEntity.setSalt("abc123");
        usersEntity.setCountry(signupUserRequest.getCountry());
        usersEntity.setAboutMe(signupUserRequest.getAboutMe());
        usersEntity.setDob(signupUserRequest.getDob());
        System.out.println(signupUserRequest.getContactNumber());
        usersEntity.setContactNumber(signupUserRequest.getContactNumber());
        System.out.println("**************************");
        System.out.println(signupUserRequest.getContactNumber());
//        usersEntity.setRole("admin");

        final UsersEntity createdUser = userBusinessService.signup(usersEntity);

        SignupUserResponse userResponse = new SignupUserResponse().id(createdUser.getUuid()).status("Registered");

        return new ResponseEntity<SignupUserResponse>(userResponse,HttpStatus.CREATED);
    }


    @RequestMapping(method= RequestMethod.POST, path="/user/signin", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization ) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        UserAuthEntity userAuthToken = userBusinessService.signin(decodedArray[0],decodedArray[1]);

        UsersEntity user = userAuthToken.getUsersEntity();

        SigninResponse authorizedUserResponse =  new SigninResponse().id(user.getUuid());

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthToken.getAccess_token());
        return new ResponseEntity<SigninResponse>(authorizedUserResponse,headers, HttpStatus.OK);


    }


    @RequestMapping(method= RequestMethod.POST, path="/user/signout", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String authorization) throws  SignOutRestrictedException {
//        String [] bearerToken = authorization.split("Bearer ");
//        UsersEntity userEntity = userBusinessService.getUser(bearerToken[1]);
        UsersEntity usersEntity = userBusinessService.getUserFromToken(authorization);
        SignoutResponse signoutResponse = new SignoutResponse().id(usersEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");

        return new ResponseEntity<SignoutResponse>(signoutResponse,HttpStatus.OK);
    }
}
