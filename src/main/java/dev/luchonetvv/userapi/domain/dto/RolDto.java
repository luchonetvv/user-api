package dev.luchonetvv.userapi.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Role", description = "Rol asignado al usuario")
public class RolDto {
    @Schema(example = "ROLE_ADMIN", description = "Nombre del rol")
    private String roleName;
    @Schema(example = "Administrator", description = "Descripción del rol")
    private String description;

    public RolDto() { }

    public RolDto(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RolDto [description=" + description + ", roleName=" + roleName + "]";
    }
}
