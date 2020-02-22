package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    public QuestionEntity postQuestion(QuestionEntity questionEntity)
    {
        em.persist(questionEntity);
        return questionEntity;
    }

    public QuestionEntity getQuestionByUUID(final String uuid)
    {
        return em.createNamedQuery("questionByUUID", QuestionEntity.class).setParameter("uuid", uuid).getSingleResult();
    }

    public List<QuestionEntity> getAllQuestions()
    {
        return em.createNamedQuery("allQuestions",QuestionEntity.class).getResultList();
    }

    public QuestionEntity updateQuestion(QuestionEntity questionEntity)
    {
        em.merge(questionEntity);
        return questionEntity;
    }

    public QuestionEntity deleteQuestion(QuestionEntity questionEntity)
    {
        em.remove(questionEntity);
        em.flush();
        return questionEntity;
    }

    public  List<QuestionEntity> getQuestionsByUser()
    {
        return em.createNamedQuery("allQuestions",QuestionEntity.class).getResultList();
    }
}
