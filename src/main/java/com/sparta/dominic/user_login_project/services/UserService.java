package com.sparta.dominic.user_login_project.services;

import com.sparta.dominic.user_login_project.entities.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;

@Named
@RequestScoped
public class UserService {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public Collection<User> getAllUsers() {
        return entityManager.createNamedQuery("getAllUsers", User.class).getResultList();
    }

    public User getUserByID(int id) {
        return entityManager.createNamedQuery("getUserByID", User.class).setParameter("id", id).getSingleResult();
    }

    public User getUserByUsername(String username) {
        return entityManager.createNamedQuery("getUserByUsername", User.class).setParameter("username", username).getSingleResult();
    }

    public void addUser(User user) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }
}
