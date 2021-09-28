package dev.luchonetvv.userapi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luchonetvv.userapi.domain.dto.UserDto;
import dev.luchonetvv.userapi.domain.entities.UserEntity;
import dev.luchonetvv.userapi.domain.model.ApiResponse;
import dev.luchonetvv.userapi.domain.model.TokenResponseBody;
import dev.luchonetvv.userapi.service.UserServiceInt;

@RestController
@RequestMapping(value = "/users", consumes = {"application/json"}, produces = {"application/json"})
public class UserController {
    private UserServiceInt userServiceInt;

    @Autowired
    public UserController(UserServiceInt userServiceInt) {
        this.userServiceInt = userServiceInt;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAll() {
        ApiResponse<List<UserDto>> response = new ApiResponse<>();
        response.setBody(UserDto.toListUserDto(userServiceInt.getAll()));
        response.setMessage("Lista de usuarios cargada satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<ApiResponse<TokenResponseBody>> signin(@RequestBody UserDto user) {
        ApiResponse<TokenResponseBody> response = new ApiResponse<>();
        Optional<String> token = userServiceInt.signin(user.getEmail(), user.getPassword());

        try {
            token.orElseThrow(() -> new Exception("Error en el proceso de login"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setBody(new TokenResponseBody(token.get()));
        response.setMessage("Inicio de sesion satisfactoria.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody UserDto user) {
        ApiResponse<UserDto> response = new ApiResponse<>();
        Optional<UserEntity> userEntity = userServiceInt.createUser(user);

        try {
            userEntity.orElseThrow(() -> new Exception("Error en el proceso de registro de usuario."));
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setBody(UserDto.toUserDto(userEntity.get()));
        response.setMessage("Inicio de sesion satisfactoria.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
