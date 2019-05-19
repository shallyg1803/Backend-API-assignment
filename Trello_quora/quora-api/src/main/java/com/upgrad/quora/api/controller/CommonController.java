package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    CommonBusinessService commonBusinessService;

    @RequestMapping(method= RequestMethod.GET, path="/userprofile/{userId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> userDetails(@PathVariable("userId") final String userId ,@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UsersEntity usersEntity = commonBusinessService.getUserDetails(userId, authorization);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(usersEntity.getFirstName()).lastName(usersEntity.getLastName())
                                                .userName(usersEntity.getUserName()).aboutMe(usersEntity.getAboutMe()).dob(usersEntity.getDob())
                                                .contactNumber(usersEntity.getContactNumber()).country(usersEntity.getCountry()).emailAddress(usersEntity.getEmail());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);

    }
}
