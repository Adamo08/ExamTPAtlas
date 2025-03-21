package com.adamo.tpexamatlas.repository;


import com.adamo.tpexamatlas.model.Document;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class DocumentRepository {
    @PersistenceContext
    public EntityManager em;

    public Document save(Document document) {
        em.persist(document);
        return document;
    }

    public Document findById(Long id) {
        return em.find(Document.class, id);
    }
}
