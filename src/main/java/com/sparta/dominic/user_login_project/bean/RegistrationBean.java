package com.sparta.dominic.user_login_project.bean;

import com.sparta.dominic.user_login_project.entities.User;
import com.sparta.dominic.user_login_project.services.UserService;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import java.io.IOException;

@Named
@RequestScoped
public class RegistrationBean {

    @Inject
    private User user;

    @Inject
    private UserService userService;

    @Inject
    private FacesContext facesContext;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void register() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (validateRegistration()) {
            userService.addUser(user);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Successful.", null));
            externalContext.redirect(externalContext.getRequestContextPath() + "/view/admin.xhtml");
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration Unsuccessful.", null));
        }
    }

    private boolean validateRegistration() {
        boolean registrationSuccess = true;
        if (!validateUsername()) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Username already taken.", null));
            registrationSuccess = false;
        }
        if (!validatePassword()) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password does not meet criteria.", null));
            registrationSuccess = false;
        }
        if (!validateName(getUser().getFirstName())) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "First name must be at least 2 characters long.", null));
            registrationSuccess = false;
        }
        if (!validateName(getUser().getLastName())) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Last name must be at least 2 characters long.", null));
            registrationSuccess = false;
        }
        return registrationSuccess;
    }

    private boolean validateName(String name) {
        return name.length() > 1;
    }

    private boolean validatePassword() {
        String password = user.getPassword();
        if (password.length() >= 8) {
            boolean hasDigit = false;
            boolean hasUppercase = false;
            boolean hasLowercase = false;
            boolean hasSymbol = false;
            for (Character character : password.toCharArray()) {
                if (Character.isDigit(character)) {
                    hasDigit = true;
                } else if (Character.isUpperCase(character)) {
                    hasUppercase = true;
                } else if (Character.isLowerCase(character)) {
                    hasLowercase = true;
                } else if (Character.isWhitespace(character)) {
                    return false;
                } else {
                    hasSymbol = true;
                }
            }
            return hasDigit && hasUppercase && hasLowercase && hasSymbol;
        }
        return false;
    }

    private boolean validateUsername() {
        String username = user.getUsername();
        if (username.length() > 1) {
            try {
                userService.getUserByUsername(username);
            } catch (NoResultException e) {
                return true;
            }
        }
        return false;
    }
}