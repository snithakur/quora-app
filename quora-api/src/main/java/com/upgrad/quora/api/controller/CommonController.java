package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommonController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, path="/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserProfile(@PathVariable("userId") String userId, @RequestHeader("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        UserEntity userEntity=userService.getUserProfile(userId, token);
        UserDetailsResponse userDetailsResponse=new UserDetailsResponse().userName(userEntity.getUsername()).aboutMe(userEntity.getAboutme())
                .contactNumber(userEntity.getContactnumber()).country(userEntity.getCountry()).dob(userEntity.getDob())
                .emailAddress(userEntity.getEmail()).firstName(userEntity.getFirstname()).lastName(userEntity.getLastname());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
