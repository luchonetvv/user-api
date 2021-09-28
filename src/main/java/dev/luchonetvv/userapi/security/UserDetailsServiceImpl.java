package dev.luchonetvv.userapi.security;

import dev.luchonetvv.userapi.domain.entities.UserEntity;
import dev.luchonetvv.userapi.repository.UserRepository;

import static org.springframework.security.core.userdetails.User.withUsername;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsServiceInt {
    private UserRepository userRepository;

    private JwtProvider jwtProvider;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = null;
        try {
            user = userRepository.findByEmail(email).orElseThrow(() ->
                    new Exception(String.format("Usuario con email %s no existe", email)));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return withUsername(user.getEmail())
            .password(user.getPassword())
            .authorities(user.getRoles())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }

    @Override
    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(
                withUsername(jwtProvider.getUsername(jwtToken))
                .authorities(jwtProvider.getRoles(jwtToken))
                .password("")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build());
        }
        return Optional.empty();
    }
}
