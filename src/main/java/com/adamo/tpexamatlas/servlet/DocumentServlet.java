package com.adamo.tpexamatlas.servlet;

import com.adamo.tpexamatlas.model.Book;
import com.adamo.tpexamatlas.model.Document;
import com.adamo.tpexamatlas.model.Magazine;
import com.adamo.tpexamatlas.repository.DocumentRepository;
import com.adamo.tpexamatlas.util.JsonUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/documents/*")
public class DocumentServlet extends HttpServlet {
    private DocumentRepository documentRepository = new DocumentRepository();
    private EntityManagerFactory emf;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("jpa-atlas-tp-aiven");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = emf.createEntityManager();
        try {
            documentRepository.em = em;
            resp.setContentType("application/json");

            String pathInfo = req.getPathInfo();
            if (pathInfo != null && pathInfo.length() > 1) {
                Long documentId = Long.parseLong(pathInfo.substring(1));
                Document document = documentRepository.findById(documentId);
                if (document == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Document not found");
                    return;
                }
                resp.getWriter().write(JsonUtil.toJson(document));
            } else {
                List<Document> documents = documentRepository.findAll();
                resp.getWriter().write(JsonUtil.toJson(documents));
            }
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = emf.createEntityManager();
        try {
            documentRepository.em = em;
            em.getTransaction().begin();

            String type = req.getParameter("type");
            String title = req.getParameter("title");
            Date dateCreate = new Date();

            System.out.println("Received - Type: " + type + ", Title: " + title);

            if ("book".equalsIgnoreCase(type)) {
                String author = req.getParameter("author");
                String isbn = req.getParameter("isbn");
                String datePublStr = req.getParameter("datePubl");
                System.out.println("Book params - Author: " + author + ", ISBN: " + isbn + ", DatePubl: " + datePublStr);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date datePubl = sdf.parse(datePublStr); // Parse YYYY-MM-DD

                Book book = new Book(title, dateCreate, author, isbn, datePubl);
                documentRepository.save(book);
                System.out.println("Book saved - ID: " + book.getId());

                em.getTransaction().commit();
                System.out.println("Transaction committed");

                resp.setContentType("application/json");
                resp.getWriter().write(JsonUtil.toJson(book));
            } else if ("magazine".equalsIgnoreCase(type)) {
                String publisher = req.getParameter("publisher");
                String issueNumber = req.getParameter("issueNumber");
                String dateIssueStr = req.getParameter("dateIssue");
                System.out.println("Magazine params - Publisher: " + publisher + ", IssueNumber: " + issueNumber + ", DateIssue: " + dateIssueStr);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dateIssue = sdf.parse(dateIssueStr);

                Magazine magazine = new Magazine(title, dateCreate, publisher, issueNumber, dateIssue);
                documentRepository.save(magazine);
                System.out.println("Magazine saved - ID: " + magazine.getId());

                em.getTransaction().commit();
                System.out.println("Transaction committed");

                resp.setContentType("application/json");
                resp.getWriter().write(JsonUtil.toJson(magazine));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid document type");
                return;
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error in doPost: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error: " + e.getMessage());
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