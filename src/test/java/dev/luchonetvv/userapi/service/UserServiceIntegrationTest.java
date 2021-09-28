package dev.luchonetvv.userapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import dev.luchonetvv.userapi.domain.dto.PhoneDto;
import dev.luchonetvv.userapi.domain.dto.UserDto;
import dev.luchonetvv.userapi.domain.entities.UserEntity;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {
    @Autowired
    private UserServiceInt userServiceInt;

    @Test
    public void createUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Gabriela Hernandez Rojas");
        userDto.setEmail("gabi@gmail.com");
        userDto.setPassword("esfacilelpass");

        List<PhoneDto> list = new ArrayList<>();
        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setNumber("88990765");
        phoneDto.setCityCode("MGA");
        phoneDto.setCountryCode("NIC");
        list.add(phoneDto);
        userDto.setPhones(list);

        Optional<UserEntity> user = userServiceInt.createUser(userDto);
        assertThat(user.get().getPassword(), not("esfacilelpass"));
    }
}
