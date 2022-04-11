package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.exceptions.AuthenticationException;
import org.monarchinitiative.poet.exceptions.UserModificationException;
import org.monarchinitiative.poet.model.entities.User;
import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    @Value( "${auth0.nickname}" )
    private String nickname_claim;

    @Value( "${auth0.email}" )
    private String email_claim;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Save a new user or update an existing users roles.
     * 
     * @param authentication the spring authentication object
     * 
     * @return a boolean if a user was created or not
     */
    public boolean saveOrUpdateUser(Authentication authentication){
        final User user = getUserFromAuthentication(authentication);
        final User existing = userRepository.findDistinctByAuthId(user.getAuthId());
        if (existing != null) {
            // Check to see if role changed
            if(!existing.getCurationRole().equals(user.getCurationRole())){
                existing.setCurationRole(user.getCurationRole());
                userRepository.save(existing);
            }
            return true;
        }
        userRepository.save(user);
        return true;
    }

    /***
     * Get or create a new user.
     * @param authentication a spring authentication object
     * @return the created or fetched user object
     */
    public User getExistingUser(Authentication authentication) throws AuthenticationException {
        final User user = getUserFromAuthentication(authentication);
        User existing = userRepository.findDistinctByAuthId(user.getAuthId());
        if(existing != null){
            return existing;
        } else {
            userRepository.save(user);
            return user;
        }
    }

    /***
     * Create the user for phenotype.hpoa file load.
     * Phenol currently does not carry biocurator information from the
     * HpoAnnotation objects.
     *
     * This functions should only be run once before the switch to POET.
     */
    public User getHumanPhenotypeOntologyUser(){
        final User user = new User("auth:00000", "hpoteam", "dr.sebastian.koehler@gmail.com", CurationRole.POET_ADMIN);
        return userRepository.save(user);
    }

    public void setUserOrcId(Authentication authentication, String orcid) throws AuthenticationException {
        User user = getExistingUser(authentication);
        if(!userRepository.existsUserByOrcid(orcid)){
            user.setOrcid(orcid);
            userRepository.save(user);
        } else {
            throw new UserModificationException(orcid);
        }
    }


    /**
     * A function to transform an authentication object to a user entity
     *
     * @param authentication the spring authentication object
     *
     * @return a user object or throw Authentication Exception
     * @since 0.6.0
     */
    private User getUserFromAuthentication(Authentication authentication) throws AuthenticationException {
        if(authentication != null){
            final Jwt credentials = (Jwt) authentication.getCredentials();
            String authId = authentication.getName();
            String nickname = credentials.getClaim(nickname_claim);
            String email = credentials.getClaim(email_claim);
            List<String> permissions = credentials.getClaim("permissions");
            CurationRole curationRole = CurationRole.POET_CURATOR;
            if(permissions.contains("poet:admin")) {
                curationRole = CurationRole.POET_ADMIN;
            }

            if(authId != null && nickname != null && email != null){
                return new User(authId, nickname, email, curationRole);
            } else {
                throw new AuthenticationException(true);
            }
        } else {
            throw new AuthenticationException(true);
        }
    }
}
