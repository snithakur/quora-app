package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;
    @Autowired
    UserDao userDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity postQuestion(QuestionEntity questionEntity, String accessToken)
    {
        UserAuthEntity authEntity=userDao.getUserByToken(accessToken);
        questionEntity.setUser(authEntity.getUser());
        return questionDao.postQuestion(questionEntity);
    }
}
