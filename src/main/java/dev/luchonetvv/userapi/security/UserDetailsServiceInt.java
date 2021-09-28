package dev.luchonetvv.userapi.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailsServiceInt extends UserDetailsService {
    Optional<UserDetails> loadUserByJwtToken(String jwtToken);
}
