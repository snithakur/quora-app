package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") String userId, @RequestHeader("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        UserEntity deletedEntity = userService.deleteUser(userId, token);
        UserDeleteResponse userDeleteResponse=new UserDeleteResponse().id(deletedEntity.getUuid()).status("USER DELETED SUCCESSFULLY");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);


    }
}
