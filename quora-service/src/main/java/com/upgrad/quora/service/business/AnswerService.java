package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

    @Autowired
    AnswerDao answerDao;
    @Autowired
    UserDao userDao;
    @Autowired
    QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity postAnswer(AnswerEntity answerEntity,String questionId, String accessToken)
    {
        System.out.println();
        UserAuthEntity authEntity=userDao.getUserByToken(accessToken);
        QuestionEntity questionEntity=questionDao.getQuestionByUUID(questionId);
        answerEntity.setUser(authEntity.getUser());
        answerEntity.setQuestion(questionEntity);
        return answerDao.postAnswer(answerEntity);
    }
}
