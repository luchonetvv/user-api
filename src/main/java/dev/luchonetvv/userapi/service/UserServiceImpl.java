package dev.luchonetvv.userapi.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.luchonetvv.userapi.domain.dto.UserDto;
import dev.luchonetvv.userapi.domain.entities.RoleEntity;
import dev.luchonetvv.userapi.domain.entities.UserEntity;
import dev.luchonetvv.userapi.exception.ValidateUserException;
import dev.luchonetvv.userapi.repository.RoleRepository;
import dev.luchonetvv.userapi.repository.UserRepository;
import dev.luchonetvv.userapi.security.JwtProvider;

@Service
public class UserServiceImpl implements UserServiceInt {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
        JwtProvider jwtProvider, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Iterable<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<String> signin(String email, String password) {
        Optional<String> token = Optional.empty();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        
        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                token = Optional.of(jwtProvider.createToken(email, user.get().getRoles()));
            } catch (AuthenticationException e){
                e.printStackTrace();
            }
        }
        return token;
    }

    @Override
    public Optional<UserEntity> createUser(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findByEmail(userDto.getEmail());
        Optional<UserEntity> userSave = Optional.empty();

        if (user.isPresent()) {
            throw new ValidateUserException(String.format("El correo %s ya fue registrado.", userDto.getEmail()));
        } else {
            Optional<RoleEntity> rol = roleRepository.findByRoleName("ROLE_ADMIN");
            final String token = Optional.of(jwtProvider.createToken(userDto.getEmail(), Arrays.asList(rol.get()))).get();
            
            userSave = Optional.of(userRepository.save(UserDto.toUserEntity(userDto, passwordEncoder.encode(userDto.getPassword()), token, rol.get())));
        }
        
        return userSave;
    }
    
}
