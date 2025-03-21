package com.adamo.tpexamatlas.servlet;

import com.adamo.tpexamatlas.model.User;
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
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {
    private UserRepository userRepository;
    private EntityManagerFactory emf;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("jpa-atlas-tp-aiven");
        userRepository = new UserRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = emf.createEntityManager();
        try {
            userRepository.em = em;
            resp.setContentType("application/json");

            String pathInfo = req.getPathInfo(); // e.g., "/1" or null
            if (pathInfo != null && pathInfo.length() > 1) {
                // Get specific user by ID
                Long userId = Long.parseLong(pathInfo.substring(1)); // Remove leading "/"
                User user = userRepository.findById(userId);
                if (user == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }
                resp.getWriter().write(JsonUtil.toJson(user));
            } else {
                // List all users
                List<User> users = userRepository.findAll();
                resp.getWriter().write(JsonUtil.toJson(users));
            }
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            userRepository.em = em;

            String name = req.getParameter("name");
            String mail = req.getParameter("mail");

            User user = new User(name, mail);
            userRepository.save(user);

            em.getTransaction().commit();

            resp.setContentType("application/json");
            resp.getWriter().write(JsonUtil.toJson(user));
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
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