package com.sparta.dominic.user_login_project.authentication;

import com.sparta.dominic.user_login_project.services.UserService;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Set;

public class InMemoryIdentityStore implements IdentityStore {

    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;

        return new UserService().getAllUsers().stream()
                .filter(user -> usernamePasswordCredential.getCaller().equals(user.getUsername())
                        && usernamePasswordCredential.getPasswordAsString().equals(user.getPassword()))
                .map(u -> new CredentialValidationResult(u.getFirstName(), Set.of(u.getRole())))
                .reduce(CredentialValidationResult.NOT_VALIDATED_RESULT, (user1, user2) -> user2);
    }


}
