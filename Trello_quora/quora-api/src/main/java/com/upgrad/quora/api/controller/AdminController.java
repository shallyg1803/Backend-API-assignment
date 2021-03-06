package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
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
public class AdminController {

    @Autowired
    AdminBusinessService adminBusinessService;

//    Admin can delete a user.
    @RequestMapping(method= RequestMethod.DELETE , path="/admin/user/{userId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable("userId") final String userId , @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UsersEntity usersEntity = adminBusinessService.deleteUser(userId,authorization);
        String userUUid = usersEntity.getUuid();
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(userUUid).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
    }


}
