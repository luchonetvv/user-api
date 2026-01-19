package dev.luchonetvv.userapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.luchonetvv.userapi.exception.ValidateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import dev.luchonetvv.userapi.domain.dto.PhoneDto;
import dev.luchonetvv.userapi.domain.dto.UserDto;
import dev.luchonetvv.userapi.domain.entities.UserEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceIntegrationTest {
    @Autowired
    private UserServiceInt userServiceInt;

    @Test
    public void createUser_shouldEncodePassword() {
        UserDto userDto = new UserDto();
        userDto.setName("Gabriela Hernandez Rojas");
        userDto.setEmail("gabi@gmail.com");
        // Debe cumplir el regex configurable (ej: mayúscula + número + >=8)
        userDto.setPassword("EsFacil123");

        List<PhoneDto> list = new ArrayList<>();
        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setNumber("88990765");
        phoneDto.setCityCode("MGA");
        phoneDto.setCountryCode("NIC");
        list.add(phoneDto);
        userDto.setPhones(list);

        Optional<UserEntity> user = userServiceInt.createUser(userDto);
        assertThat(user).isPresent();
        assertThat(user.get().getPassword()).isNotEqualTo("EsFacil123");
        assertThat(user.get().getPassword()).isNotBlank();
    }

    @Test
    void createUser_whenEmailAlreadyExists_shouldThrow() {
        UserDto userDto = new UserDto();
        userDto.setName("A");
        userDto.setEmail("dup@gmail.com");
        userDto.setPassword("EsFacil123");
        userDto.setPhones(List.of(new PhoneDto("111", "1", "57")));

        userServiceInt.createUser(userDto);

        assertThatThrownBy(() -> userServiceInt.createUser(userDto))
                .isInstanceOf(ValidateUserException.class)
                .hasMessage("El correo ya registrado");
    }
}
