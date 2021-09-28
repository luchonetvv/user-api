package dev.luchonetvv.userapi.service;

import java.util.Optional;

import dev.luchonetvv.userapi.domain.dto.UserDto;
import dev.luchonetvv.userapi.domain.entities.UserEntity;

public interface UserServiceInt {
    Iterable<UserEntity> getAll();

    Optional<String> signin(String email, String password);

    Optional<UserEntity> createUser(UserDto user);
}
