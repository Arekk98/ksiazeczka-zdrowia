package com.project.ksiazeczkazdrowiadlazwierzat.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import com.project.ksiazeczkazdrowiadlazwierzat.data.collection.User;
import com.project.ksiazeczkazdrowiadlazwierzat.data.repository.UserRepository;

import static com.project.ksiazeczkazdrowiadlazwierzat.service.common.Commons.TIMEOUT;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).block(TIMEOUT);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                                                                      user.getPassword(),
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      true,
                                                                      Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
    }
}
