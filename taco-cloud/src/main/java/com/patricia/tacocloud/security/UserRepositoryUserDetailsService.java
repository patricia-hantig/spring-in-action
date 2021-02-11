package com.patricia.tacocloud.security;

import com.patricia.tacocloud.User;
import com.patricia.tacocloud.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // here we inject UserRepository in UserRepositoryUserDetailsService so that it can now be used in the next methods

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("User '" + username + "' not found");
    }
}
// @Service - includes the class in Spring's component scanning so there is no need to declare this class as a bean
//          - Spring will automatically discover it & instantiate it as a bean
