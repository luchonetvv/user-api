package dev.luchonetvv.userapi.domain.dto;

public class RolDto {
    private String roleName;
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
