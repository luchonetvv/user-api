package dev.luchonetvv.userapi.service;

import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final Pattern emailPattern;
    private final Pattern passwordPattern;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JwtProvider jwtProvider,
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                           @Value("${app.regex.email}") String emailRegex,
                           @Value("${app.regex.password}") String passwordRegex
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailPattern = Pattern.compile(emailRegex);
        this.passwordPattern = Pattern.compile(passwordRegex);
    }

    @Override
    public Iterable<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public String signin(String email, String password) {
        validateEmailAndPassword(email, password);

        String token;
        Optional<UserEntity> user = userRepository.findByEmail(email);
        
        if (user.isPresent()) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                token = Optional.of(jwtProvider.createToken(email, user.get().getRoles()))
                        .orElseThrow(() -> new ValidateUserException("Credenciales inválidas"));
        } else {
            throw new ValidateUserException("Usuario o contraseña incorrectos");
        }
        return token;
    }

    @Override
    public Optional<UserEntity> createUser(UserDto userDto) {
        validateEmailAndPassword(userDto.getEmail(), userDto.getPassword());

        Optional<UserEntity> user = userRepository.findByEmail(userDto.getEmail());
        Optional<UserEntity> userSave;

        if (user.isPresent()) {
            throw new ValidateUserException("El correo ya registrado");
        } else {
            Optional<RoleEntity> rol = roleRepository.findByRoleName("ROLE_ADMIN");

            if (rol.isPresent()) {
                final String token = Optional.of(jwtProvider.createToken(userDto.getEmail(), Collections.singletonList(rol.get()))).get();

                userSave = Optional.of(userRepository.save(UserDto.toUserEntity(userDto, passwordEncoder.encode(userDto.getPassword()), token, rol.get())));
            } else {
                throw new ValidateUserException("Error al crear el usuario");
            }
        }
        
        return userSave;
    }

    private void validateEmailAndPassword(String email, String password) {
        if (!emailPattern.matcher(email).matches()) {
            throw new ValidateUserException("Ingresar un email válido.");
        }
        if (!passwordPattern.matcher(password).matches()) {
            throw new ValidateUserException("Ingresar una contraseña válida.");
        }
    }
    
}
