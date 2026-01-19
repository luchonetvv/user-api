package dev.luchonetvv.userapi.controller;

import java.util.List;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

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

@Tag(name = "Users", description = "Endpoints de usuarios: registro, login y consulta")
@RestController
@RequestMapping(value = "/users", produces = {"application/json"})
public class UserController {
    private final UserServiceInt userServiceInt;

    @Autowired
    public UserController(UserServiceInt userServiceInt) {
        this.userServiceInt = userServiceInt;
    }

    @Operation(
            summary = "Listar usuarios",
            description = "Retorna la lista de usuarios. Requiere rol ROLE_ADMIN."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista obtenida"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = dev.luchonetvv.userapi.domain.model.ErrorMessageResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "No autorizado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = dev.luchonetvv.userapi.domain.model.ErrorMessageResponse.class))
            )
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAll() {
        ApiResponse<List<UserDto>> response = new ApiResponse<>();
        response.setBody(UserDto.toListUserDto(userServiceInt.getAll()));
        response.setMessage("Lista de usuarios cargada satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y retorna un token JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(
                                    value = "{ \"email\": \"test@gmail.com\", \"password\": \"Abcdefg1\" }"
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validación fallida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = dev.luchonetvv.userapi.domain.model.ErrorMessageResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = dev.luchonetvv.userapi.domain.model.ErrorMessageResponse.class),
                            examples = @ExampleObject(value = "{ \"message\": \"Credenciales inválidas\" }")
                    )
            )
    })
    @PostMapping(value = "/signin", consumes = "application/json")
    public ResponseEntity<ApiResponse<TokenResponseBody>> signin(@RequestBody UserDto user) {
        ApiResponse<TokenResponseBody> response = new ApiResponse<>();
        final String token = userServiceInt.signin(user.getEmail(), user.getPassword());

        response.setBody(new TokenResponseBody(token));
        response.setMessage("Inicio de sesion satisfactoria.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Registrar usuario",
            description = "Crea un usuario y devuelve el usuario creado con token JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(
                                    name = "Registro válido",
                                    value = "{\n" +
                                            "  \"name\": \"Test User\",\n" +
                                            "  \"email\": \"test@gmail.com\",\n" +
                                            "  \"password\": \"Abcdefg1\",\n" +
                                            "  \"phones\": [\n" +
                                            "    {\"number\": \"88990765\", \"cityCode\": \"MNG\", \"countryCode\": \"NIC\"}\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validación fallida (regex email/password, campos inválidos)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = dev.luchonetvv.userapi.domain.model.ErrorMessageResponse.class),
                            examples = @ExampleObject(value = "{ \"message\": \"Ingresar una contraseña válida.\" }")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Email ya registrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = dev.luchonetvv.userapi.domain.model.ErrorMessageResponse.class),
                            examples = @ExampleObject(value = "{ \"message\": \"El correo ya registrado\" }")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = dev.luchonetvv.userapi.domain.model.ErrorMessageResponse.class),
                            examples = @ExampleObject(value = "{ \"message\": \"Error interno\" }")
                    )
            )
    })
    @PostMapping(consumes = "application/json")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        UserEntity userEntity = userServiceInt.createUser(user)
                .orElseThrow(() -> new RuntimeException("Error en el proceso de registro de usuario."));

        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.toUserDto(userEntity));
    }
}
