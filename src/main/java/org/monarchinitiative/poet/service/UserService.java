package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.exceptions.AuthenticationException;
import org.monarchinitiative.poet.model.entities.User;
import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

@Service
public class UserService {

    private UserRepository userRepository;

    @Value( "${auth0.nickname}" )
    private String nickname_claim;

    @Value( "${auth0.email}" )
    private String email_claim;

    @Value( "${auth0.role}" )
    private String role_claim;

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
        if(user != null){
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
        return false;
    }

    /***
     * Get or create a new user.
     * @param authentication a spring authentication object
     * @return the created or fetched user object
     */
    public User getExistingUser(Authentication authentication) throws AuthenticationException{
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
        final User user = new User("auth:00000", "hpoteam", "dr.sebastian.koehler@gmail.com", "00000", CurationRole.ELEVATED_CURATOR);
        return userRepository.save(user);
    }


    /**
     * A functon to transform an authentication object to a user entity
     *
     * @param authentication the spring authentication object
     *
     * @return a user object or throw Authentication Exception
     * @since 0.6.0
     */
    private User getUserFromAuthentication(Authentication authentication){
        if(authentication != null){
            final Jwt credentials = (Jwt) authentication.getCredentials();
            String authId = authentication.getName();
            String nickname = credentials.getClaim(nickname_claim);
            String email = credentials.getClaim(email_claim);
            String orcid = "";
            CurationRole curationRole = CurationRole.valueOf(credentials.getClaim(role_claim));
            if(authId != null && nickname != null && email != null){
                return new User(authId, nickname, email, orcid, curationRole);
            } else {
                throw new AuthenticationException(true);
            }
        } else {
            throw new AuthenticationException(true);
        }
    }
}
