package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByUUID(final String userUuid) {
        return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid).getSingleResult();
    }

    public UserEntity getUserByUsername(final String username) {
        return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
    }

    public UserEntity getUser(final Integer id)
    {
        return entityManager.createNamedQuery("userById", UserEntity.class).setParameter("id", id).getSingleResult();
    }

    public UserAuthEntity getUserByToken(final String token)
    {
        return entityManager.createNamedQuery("userByAuthtoken", UserAuthEntity.class).setParameter("accessToken", token).getSingleResult();
    }
    public UserAuthEntity createToken (UserAuthEntity userAuthEntity)
    {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    public UserEntity deleteUser(UserEntity userEntity)
    {
        entityManager.remove(userEntity);
        return userEntity;
    }
}
