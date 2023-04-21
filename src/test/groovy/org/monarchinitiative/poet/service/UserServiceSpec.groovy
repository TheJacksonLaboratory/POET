//package org.monarchinitiative.poet.service
//
//import org.monarchinitiative.poet.repository.UserRepository
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.security.core.Authentication
//import org.springframework.security.test.context.support.WithMockUser
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.ContextConfiguration
//import spock.lang.Shared
//import spock.lang.Specification
//
//@AutoConfigureMockMvc
//@ContextConfiguration
//@ActiveProfiles(value = "test")
//@WithMockUser(username = "user1", password = "pwd", roles = "USER")
//class UserServiceSpec extends Specification {
//
//    @Autowired
//    private UserRepository userRepository = Mock();
//
//    @Shared
//    Authentication authentication = Stub(Authentication.class)
//
//
//    def getAuthentication(){
//    }
//
//}
