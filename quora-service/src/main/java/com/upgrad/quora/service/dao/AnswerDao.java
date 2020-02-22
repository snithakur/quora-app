package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity postAnswer(AnswerEntity answerEntity)
    {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public  AnswerEntity updateAnswer(AnswerEntity answerEntity)
    {
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    public AnswerEntity deleteAnswer(AnswerEntity answerEntity)
    {
        entityManager.remove(answerEntity);
        return answerEntity;
    }

    public List<AnswerEntity> getAllAnswers()
    {
       return entityManager.createNamedQuery("allAnswers",AnswerEntity.class).getResultList();
    }

    public AnswerEntity getAnswerByUUID(String answerId)
    {
        return entityManager.createNamedQuery("answerByUUID",AnswerEntity.class).setParameter("uuid",answerId).getSingleResult();
    }
}

