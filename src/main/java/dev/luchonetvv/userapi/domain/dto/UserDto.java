package dev.luchonetvv.userapi.domain.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.lang.NonNull;

import dev.luchonetvv.userapi.domain.entities.PhoneEntity;
import dev.luchonetvv.userapi.domain.entities.RoleEntity;
import dev.luchonetvv.userapi.domain.entities.UserEntity;

@JsonInclude(Include.NON_NULL)
public class UserDto {
    private UUID id;
    private String name;
    @NonNull
    @Email(regexp = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$", message = "Ingresar un email valido.")
    private String email;
    @NonNull
    private String password;
    private List<PhoneDto> phones;
    private LocalDateTime lastLogin;
    private String token;
    private boolean active;
    private List<RolDto> roles;
    private LocalDateTime created;
    private LocalDateTime modified;

    public UserDto() { }

    public UserDto(UUID id, String name, String email, String password, List<PhoneDto> phones, LocalDateTime lastLogin,
            String token, boolean active, List<RolDto> roles, LocalDateTime created, LocalDateTime modified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
        this.lastLogin = lastLogin;
        this.token = token;
        this.active = active;
        this.roles = roles;
        this.created = created;
        this.modified = modified;
    }

    public UserDto(String name, String email, String password, List<PhoneDto> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PhoneDto> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDto> phones) {
        this.phones = phones;
    }

    @JsonProperty(value = "last_login")
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty(value = "isactive")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<RolDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RolDto> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public static UserEntity toUserEntity(UserDto userDto, String passwordEncode, String token, RoleEntity userRole) {
        LocalDateTime created = LocalDateTime.now();
        UserEntity userEntity = new UserEntity();

        userEntity.setName(userDto.getName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(passwordEncode);
        userEntity.setLastLogin(created);
        userEntity.setToken(token);
        userEntity.setActive(true);
        userEntity.setCreated(created);
        userEntity.setRoles(Arrays.asList(userRole));

        List<PhoneEntity> listPhone = new ArrayList<>();
        userDto.getPhones().forEach(phone -> {
            PhoneEntity phoneEntity = new PhoneEntity(
                phone.getNumber(),
                phone.getCityCode(),
                phone.getCountryCode()
            );
            phoneEntity.setUser(userEntity);
            listPhone.add(phoneEntity);
        });
        listPhone.forEach(phone -> {
            userEntity.getPhones().add(phone);
        });
        
        return userEntity;
    }

    public static UserDto toUserDto(UserEntity user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setPhones(
            user.getPhones().stream()
                .map(phone -> new PhoneDto(phone.getNumber(), phone.getCityCode(), phone.getCountryCode()))
                .collect(Collectors.toList())
        );
        userDto.setLastLogin(user.getLastLogin());
        userDto.setToken(user.getToken());
        userDto.setActive(user.isActive());
        userDto.setCreated(user.getCreated());
        userDto.setModified(user.getModified());
        return userDto;
    }

    public static List<UserDto> toListUserDto(Iterable<UserEntity> users) {
        return StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList())
            .stream().map(user -> new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhones().stream()
                    .map(phone -> new PhoneDto(
                            phone.getNumber(),
                            phone.getCityCode(),
                            phone.getCountryCode()
                        )
                    ).collect(Collectors.toList()),
                user.getLastLogin(),
                user.getToken(),
                user.isActive(),
                user.getRoles().stream()
                    .map(rol -> new RolDto(
                            rol.getRoleName(),
                            rol.getDescription()
                        )
                    ).collect(Collectors.toList()),
                user.getCreated(),
                user.getModified()
            )
        ).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "UserDto [active=" + active + ", created=" + created + ", email=" + email + ", id=" + id + ", lastLogin="
                + lastLogin + ", modified=" + modified + ", name=" + name + ", password=" + password + ", phones="
                + phones + ", roles=" + roles + ", token=" + token + "]";
    }
}
