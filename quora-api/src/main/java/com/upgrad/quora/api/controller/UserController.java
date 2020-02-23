package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST,path = "/user/signup", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signUpUser(@RequestBody SignupUserRequest signUpUserRequest)
    {
        UserEntity userEntity=new UserEntity();
        System.out.println(signUpUserRequest.getFirstName()+" "+signUpUserRequest.getLastName());
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstname(signUpUserRequest.getFirstName());
        userEntity.setLastname(signUpUserRequest.getLastName());
        userEntity.setUsername(signUpUserRequest.getUserName());
        userEntity.setEmail(signUpUserRequest.getEmailAddress());
        userEntity.setAboutme(signUpUserRequest.getAboutMe());
        userEntity.setCountry(signUpUserRequest.getCountry());
        userEntity.setPassword(signUpUserRequest.getPassword());
        userEntity.setRole("nonadmin");
        userEntity.setSalt("1234abc");
        userEntity.setContactnumber(signUpUserRequest.getContactNumber());
        userEntity.setDob(signUpUserRequest.getDob());
        UserEntity createdUserEntity = userService.createUser(userEntity);
        SignupUserResponse signupUserResponse=new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupUserResponse>(signupUserResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("Authorization") String authorization)
    {
        String[] credentials= new String(Base64.getDecoder().decode(authorization.split("Basic ")[1])).split(":");
        UserAuthEntity authEntity=userService.authenticate(credentials[0],credentials[1]);
        SigninResponse signinResponse=new SigninResponse().id(authEntity.getUuid()).message("TOKEN GENERATED SUCCESSFULLY");
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", authEntity.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse,headers,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signOut(@RequestHeader("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        UserAuthEntity authEntity=userService.signOut(token);
        SignoutResponse signoutResponse=new SignoutResponse().id(authEntity.getUuid()).message("SINGED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse,HttpStatus.OK);
    }
}
