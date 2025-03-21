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

    // Initialize EntityManagerFactory in servlet init
    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("jpa-atlas-tp-aiven");
        userRepository = new UserRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = emf.createEntityManager();
        try {
            // Set EntityManager in repository
            userRepository.em = em;
            resp.setContentType("application/json");
            List<User> users = userRepository.findAll();
            resp.getWriter().write(JsonUtil.toJson(users));
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
