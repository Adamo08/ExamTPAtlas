package com.adamo.tpexamatlas.repository;

import com.adamo.tpexamatlas.model.Borrowing;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class BorrowingRepository {
    @PersistenceContext
    public EntityManager em;

    public List<Borrowing> findActiveBorrowings() {
        return em.createQuery("SELECT b FROM Borrowing b WHERE b.returnDate IS NULL", Borrowing.class)
                .getResultList();
    }

    public Borrowing save(Borrowing borrowing) {
        em.persist(borrowing);
        return borrowing;
    }

    public Borrowing findById(Long id) {
        return em.find(Borrowing.class, id);
    }
}