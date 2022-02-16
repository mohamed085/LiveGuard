package com.liveguard.service;

import com.liveguard.domain.LiveGuardUserDetails;
import com.liveguard.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LiveGuardUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public LiveGuardUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        return userRepository
                .findByEmail(email)
                .map(LiveGuardUserDetails:: new)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user with email: " + email));
    }
}
