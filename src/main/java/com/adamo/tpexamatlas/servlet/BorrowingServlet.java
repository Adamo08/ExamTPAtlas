package com.adamo.tpexamatlas.servlet;

import com.adamo.tpexamatlas.model.Borrowing;
import com.adamo.tpexamatlas.model.Document;
import com.adamo.tpexamatlas.model.User;
import com.adamo.tpexamatlas.repository.BorrowingRepository;
import com.adamo.tpexamatlas.repository.DocumentRepository;
import com.adamo.tpexamatlas.repository.UserRepository;
import com.adamo.tpexamatlas.util.JsonUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/borrowings/*")
public class BorrowingServlet extends HttpServlet {
    private EntityManagerFactory emf;
    private BorrowingRepository borrowingRepository = new BorrowingRepository();
    private UserRepository userRepository = new UserRepository();
    private DocumentRepository documentRepository = new DocumentRepository();

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("jpa-atlas-tp-aiven");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = emf.createEntityManager();
        try {
            borrowingRepository.em = em;
            resp.setContentType("application/json");
            List<Borrowing> borrowings = borrowingRepository.findActiveBorrowings();
            resp.getWriter().write(JsonUtil.toJson(borrowings));
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            borrowingRepository.em = em;
            userRepository.em = em;
            documentRepository.em = em;

            Long userId = Long.parseLong(req.getParameter("userId"));
            Long documentId = Long.parseLong(req.getParameter("documentId"));

            User user = userRepository.findById(userId);
            Document document = documentRepository.findById(documentId);

            if (user == null || document == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Borrowing borrowing = new Borrowing(user, document, new Date(), null);
            borrowingRepository.save(borrowing);

            resp.setContentType("application/json");
            resp.getWriter().write(JsonUtil.toJson(borrowing));
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            borrowingRepository.em = em;
            Long borrowingId = Long.parseLong(req.getParameter("borrowingId"));
            Borrowing borrowing = borrowingRepository.findById(borrowingId);

            if (borrowing == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            borrowing.setReturnDate(new Date());
            borrowingRepository.save(borrowing);

            resp.setContentType("application/json");
            resp.getWriter().write(JsonUtil.toJson(borrowing));
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            em.close();
        }
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}