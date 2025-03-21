package com.adamo.tpexamatlas.servlet;

import com.adamo.tpexamatlas.model.Book;
import com.adamo.tpexamatlas.model.Magazine;
import com.adamo.tpexamatlas.repository.DocumentRepository;
import com.adamo.tpexamatlas.util.JsonUtil;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

@WebServlet("/documents/*")
public class DocumentServlet extends HttpServlet {
    private DocumentRepository documentRepository = new DocumentRepository();
    private EntityManagerFactory emf;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("jpa-atlas-tp-aiven");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        documentRepository.em = emf.createEntityManager();
        try {
            documentRepository.em.getTransaction().begin();

            String type = req.getParameter("type");
            String title = req.getParameter("title");
            Date dateCreate = new Date();

            if ("book".equalsIgnoreCase(type)) {
                String author = req.getParameter("author");
                String isbn = req.getParameter("isbn");
                Date datePubl = new Date(req.getParameter("datePubl"));

                Book book = new Book(title, dateCreate, author, isbn, datePubl);
                documentRepository.save(book);

                resp.setContentType("application/json");
                resp.getWriter().write(JsonUtil.toJson(book));
            } else if ("magazine".equalsIgnoreCase(type)) {
                String publisher = req.getParameter("publisher");
                String issueNumber = req.getParameter("issueNumber");
                Date dateIssue = new Date(req.getParameter("dateIssue"));

                Magazine magazine = new Magazine(title, dateCreate, publisher, issueNumber, dateIssue);
                documentRepository.save(magazine);

                resp.setContentType("application/json");
                resp.getWriter().write(JsonUtil.toJson(magazine));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid document type");
            }
            documentRepository.em.getTransaction().commit();

        } catch (Exception e) {
            documentRepository.em.getTransaction().rollback();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            documentRepository.em.close();
        }
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}