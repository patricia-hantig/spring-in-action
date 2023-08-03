package com.patricia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReactiveUserDetailService {

    private UserRepositoryReactive userRepository;

    @Autowired
    public ReactiveUserDetailService(UserRepositoryReactive userRepository) {
        this.userRepository = userRepository;
    }
    // here we inject UserRepository in UserRepositoryUserDetailsService so that it can now be used in the next methods

    public ReactiveUserDetailsService userDetailsService(UserRepositoryReactive userRepository) {
        return new ReactiveUserDetailsService() {
            @Override
            public Mono<UserDetails> findByUsername(String username) {
                return userRepository.findByUsername(username)
                        .map(User::toUserDetails);
            }
        };
    }

}
