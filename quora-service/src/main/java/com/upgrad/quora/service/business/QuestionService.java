package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<QuestionEntity> getAllQuestions(String accessToken)
    {
        UserAuthEntity authEntity=userDao.getUserByToken(accessToken);
        if(authEntity!=null)
        {
            return questionDao.getAllQuestions();
        }

        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity updateQuestion(String questionId, String content, String accessToken)
    {
        UserAuthEntity authEntity=userDao.getUserByToken(accessToken);
        QuestionEntity questionEntity=questionDao.getQuestionByUUID(questionId);
        questionEntity.setContent(content);
        return questionDao.updateQuestion(questionEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(String questionId, String accessToken)
    {
        UserAuthEntity authEntity=userDao.getUserByToken(accessToken);
        QuestionEntity questionEntity=questionDao.getQuestionByUUID(questionId);
        return questionDao.deleteQuestion(questionEntity);
    }

    public List<QuestionEntity> getQuestionsByUser(String userId,String accessToken)
    {
        UserAuthEntity authEntity=userDao.getUserByToken(accessToken);
        if(authEntity!=null && authEntity.getUser().getUuid().equalsIgnoreCase(userId))
        {
            return questionDao.getQuestionsByUser();
        }

        return null;
    }
}
