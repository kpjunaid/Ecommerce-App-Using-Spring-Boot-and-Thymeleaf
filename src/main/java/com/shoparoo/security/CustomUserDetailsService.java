package com.shoparoo.security;

import com.shoparoo.entity.Role;
import com.shoparoo.entity.User;
import com.shoparoo.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities(getGrantedAuthority(user.getRoles()))
                    .build();
        } else {
            throw new UsernameNotFoundException("No user exists with this email.");
        }
    }

    private List<GrantedAuthority> getGrantedAuthority(Set<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role: roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return grantedAuthorities;
    }
}
