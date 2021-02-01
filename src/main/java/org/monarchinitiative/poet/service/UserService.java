package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.entities.User;
import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * A functon to save a new user if the user exist ensure we have the most updated curation role for them.
     *
     * @param authId the auth0 identifier
     * @param nickname the auth0 username
     * @param email the email address of the user
     * @param orcid an orcid identifier for the curator
     * @param curationRole the role for the curator
     *
     * @return a boolean if the user was created or not
     * @since 0.5.0
     */
    public boolean saveOrUpdateUser(String authId, String nickname, String email, String orcid, CurationRole curationRole){
        if(authId !=null && nickname !=null && email !=null){
            final User user = new User(authId, nickname, email, orcid, curationRole);
            final User existing = userRepository.findDistinctByAuthId(authId);
            if (existing != null) {
                // Check to see if role changed
                if(!existing.getCurationRole().equals(curationRole)){
                    existing.setCurationRole(user.getCurationRole());
                    userRepository.save(existing);
                }
                return true;
            }
            // Check for errors.
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
