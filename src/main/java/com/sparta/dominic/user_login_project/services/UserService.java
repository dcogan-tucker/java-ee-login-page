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

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    public Collection<User> getAllUsers() {
        return entityManager.createNamedQuery("getAllUsers", User.class).getResultList();
    }

    public User getUserByID(int id) {
        return entityManager.createNamedQuery("getUserByID", User.class).getSingleResult();
    }

    public User getUserByUsername(String username) {
        return entityManager.createNamedQuery("getUserByUsername", User.class).getSingleResult();
    }

    public void addUser(User user) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.flush();
        entityManager.refresh(user);
        entityManager.getTransaction().commit();
    }
}
