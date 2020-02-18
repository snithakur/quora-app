package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity postQuestion(QuestionEntity questionEntity)
    {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public QuestionEntity getQuestionByUUID(final String uuid)
    {
        return entityManager.createNamedQuery("questionByUUID", QuestionEntity.class).setParameter("uuid", uuid).getSingleResult();
    }

    public List<QuestionEntity> getAllQuestions()
    {
        return entityManager.createNamedQuery("allQuestions",QuestionEntity.class).getResultList();
    }
}
