package com.adamo.tpexamatlas.repository;


import com.adamo.tpexamatlas.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class UserRepository {
    @PersistenceContext
    public EntityManager em;

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User save(User user) {
        em.persist(user);
        return user;
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

}
