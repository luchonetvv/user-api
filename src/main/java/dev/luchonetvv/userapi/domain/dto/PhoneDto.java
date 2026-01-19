package dev.luchonetvv.userapi.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Phone", description = "Teléfono asociado al usuario")
public class PhoneDto {
    @Schema(example = "88990765", description = "Número de teléfono")
    private String number;
    @Schema(example = "MNG", description = "Código de ciudad (3 caracteres)")
    private String cityCode;
    @Schema(example = "NIC", description = "Código de país (3 caracteres)")
    private String countryCode;

    public PhoneDto() { }

    public PhoneDto(String number, String cityCode, String countryCode) {
        this.number = number;
        this.cityCode = cityCode;
        this.countryCode = countryCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "PhoneDto [cityCode=" + cityCode + ", countryCode=" + countryCode + ", number=" + number + "]";
    }
}
