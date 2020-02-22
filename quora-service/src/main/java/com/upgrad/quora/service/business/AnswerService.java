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

import java.util.List;

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
        UserAuthEntity authEntity=userDao.getUserByToken(accessToken);
        QuestionEntity questionEntity=questionDao.getQuestionByUUID(questionId);
        answerEntity.setUser(authEntity.getUser());
        answerEntity.setQuestion(questionEntity);
        return answerDao.postAnswer(answerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity updateAnswer(String content,String answerId, String accessToken)
    {
        UserAuthEntity authEntity=userDao.getUserByToken(accessToken);
        AnswerEntity answerEntity=answerDao.getAnswerByUUID(answerId);
        answerEntity.setAnswer(content);
        return answerDao.updateAnswer(answerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(String answerId, String accessToken)
    {
        UserAuthEntity authEntity=userDao.getUserByToken(accessToken);
        AnswerEntity answerEntity=answerDao.getAnswerByUUID(answerId);
        return answerDao.deleteAnswer(answerEntity);
    }

    public List<AnswerEntity> getAnswersByQuestion(String accessToken)
    {
        UserAuthEntity authEntity=userDao.getUserByToken(accessToken);
        return answerDao.getAllAnswers();
    }
}
