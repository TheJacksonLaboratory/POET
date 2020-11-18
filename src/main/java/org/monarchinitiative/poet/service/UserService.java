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

    public boolean saveNewUser(String authId, String nickname, String email, String orcid, CurationRole curationRole){
        if(authId !=null && nickname !=null && email !=null){
            final User user = new User(authId, nickname, email, orcid, curationRole);

            if(userRepository.findDistinctByAuthId(authId) != null){
                return true;
            }
            // Check for errors.
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
