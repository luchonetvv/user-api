package dev.luchonetvv.userapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luchonetvv.userapi.domain.dto.PhoneDto;
import dev.luchonetvv.userapi.domain.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerMockMvcTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    private UserDto buildUser(String email, String password) {
        UserDto user = new UserDto();
        user.setName("Test User");
        user.setEmail(email);
        user.setPassword(password);

        PhoneDto phone = new PhoneDto();
        phone.setNumber("88990765");
        phone.setCityCode("MNG");
        phone.setCountryCode("NIC");

        user.setPhones(List.of(phone));
        return user;
    }

    @Test
    @WithMockUser(username = "tester") // suficiente si POST /users está público pero security global exige auth
    void createUser_shouldReturn201_andExpectedFields_andNoPassword() throws Exception {
        UserDto req = buildUser("success_" + System.currentTimeMillis() + "@gmail.com", "Abcdefg1");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                // Campos típicos del enunciado (según tu UserDto)
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.last_login").exists())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.isactive").exists())

                // No devolver password
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "tester")
    void createUser_whenEmailAlreadyRegistered_shouldReturn409_andMessage() throws Exception {
        String email = "dup_" + System.currentTimeMillis() + "@gmail.com";

        // 1) Primer registro OK
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUser(email, "Abcdefg1"))))
                .andExpect(status().isCreated());

        // 2) Segundo registro con mismo email => 409 + {message}
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUser(email, "Abcdefg1"))))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("El correo ya registrado"));
    }

    @Test
    @WithMockUser(username = "tester")
    void createUser_whenPasswordDoesNotMatchRegex_shouldReturn400_andMessage() throws Exception {
        // No cumple tu regex: falta mayúscula (tu regex pide al menos una)
        UserDto req = buildUser("badpass_" + System.currentTimeMillis() + "@gmail.com", "abcdefg1");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Ingresar una contraseña válida."));
    }

    @Test
    @WithMockUser(username = "tester")
    void createUser_whenEmailDoesNotMatchRegex_shouldReturn400_andMessage() throws Exception {
        // No cumple tu regex: falta mayúscula (tu regex pide al menos una)
        UserDto req = buildUser("badpass_" + System.currentTimeMillis() + "@gmail", "Abcdefg1");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Ingresar un email válido."));
    }
}
