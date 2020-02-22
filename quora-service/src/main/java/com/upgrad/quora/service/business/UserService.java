package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity createUser(UserEntity userEntity) {
        String encryptedPassword = cryptographyProvider.encrypt(userEntity.getSalt(), userEntity.getPassword());
        userEntity.setPassword(encryptedPassword);
        return userDao.createUser(userEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity authenticate(String username, String password) {
        UserEntity userEntity = userDao.getUserByUsername(username);
        UserAuthEntity userAuthToken = new UserAuthEntity();
        if (userEntity != null) {
            String encryptedPassword = cryptographyProvider.encrypt(userEntity.getSalt(), password);
            if (userEntity.getPassword().equalsIgnoreCase(encryptedPassword)) {
                JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userEntity.getPassword());
                userAuthToken.setUser(userEntity);

                final ZonedDateTime now = ZonedDateTime.now();
                final ZonedDateTime expiresAt = now.plusHours(8);
                userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
                userAuthToken.setUuid(UUID.randomUUID().toString());
                userAuthToken.setLoginAt(now);
                userAuthToken.setExpiresAt(expiresAt);
                userDao.createToken(userAuthToken);
            }
        }
        return userAuthToken;
    }

    public UserEntity getUserProfile(String userId, String token) {
        UserAuthEntity authEntity = userDao.getUserByToken(token);
        UserEntity userEntity = userDao.getUserByUUID(userId);
        if (userEntity.getUuid().equalsIgnoreCase(authEntity.getUser().getUuid())) {
            return userEntity;
        }
        return null;
    }

    public UserEntity deleteUser(String userId, String token)
    {
        UserAuthEntity authEntity=userDao.getUserByToken(token);
        if(authEntity.getUser().getRole().equalsIgnoreCase("admin"))
        {
            UserEntity userEntity=userDao.getUserByUUID(userId);
            return userDao.deleteUser(userEntity);
        }
        return null;
    }
}

