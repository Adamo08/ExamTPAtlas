package com.adamo.tpexamatlas;


import com.adamo.tpexamatlas.model.Book;
import com.adamo.tpexamatlas.model.Borrowing;
import com.adamo.tpexamatlas.model.Magazine;
import com.adamo.tpexamatlas.model.User;
import com.adamo.tpexamatlas.repository.BorrowingRepository;
import com.adamo.tpexamatlas.repository.DocumentRepository;
import com.adamo.tpexamatlas.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Calendar;
import java.util.Date;

public class InitialDataLoader {
    public static void main(String[] args) {
        // Create EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-atlas-tp-aiven");
        EntityManager em = emf.createEntityManager();

        // Create repositories
        UserRepository userRepository = new UserRepository();
        DocumentRepository documentRepository = new DocumentRepository();
        BorrowingRepository borrowingRepository = new BorrowingRepository();

        // Inject EntityManager into repositories
        em.getTransaction().begin();
        userRepository.em = em;
        documentRepository.em = em;
        borrowingRepository.em = em;

        try {
            // Create sample users
            User user1 = new User("John Doe", "john.doe@example.com");
            User user2 = new User("Jane Smith", "jane.smith@example.com");
            userRepository.save(user1);
            userRepository.save(user2);

            // Create sample books
            Book book1 = new Book(
                    "The Great Gatsby",
                    new Date(),
                    "F. Scott Fitzgerald",
                    "978-0743273565",
                    new Date(1925 - 1900, Calendar.APRIL, 10)
            );
            Book book2 = new Book(
                    "1984",
                    new Date(),
                    "George Orwell",
                    "978-0451524935",
                    new Date(1949 - 1900, Calendar.JUNE, 8)
            );
            documentRepository.save(book1);
            documentRepository.save(book2);

            // Create sample magazines
            Magazine magazine1 = new Magazine(
                    "National Geographic",
                    new Date(),
                    "National Geographic Society",
                    "Vol 245 No 3",
                    new Date(2024 - 1900, Calendar.MARCH, 1)
            );
            Magazine magazine2 = new Magazine(
                    "Time",
                    new Date(),
                    "Time Inc.",
                    "Vol 203 No 5",
                    new Date(2024 - 1900, Calendar.FEBRUARY, 15)
            );
            documentRepository.save(magazine1);
            documentRepository.save(magazine2);

            // Create sample borrowings
            Borrowing borrowing1 = new Borrowing(
                    user1,
                    book1,
                    new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000),
                    null // Still borrowed
            );
            Borrowing borrowing2 = new Borrowing(
                    user2,
                    magazine1,
                    new Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000),
                    null // Still borrowed
            );
            borrowingRepository.save(borrowing1);
            borrowingRepository.save(borrowing2);

            // Commit transaction
            em.getTransaction().commit();
            System.out.println("Initial data successfully loaded!");

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
