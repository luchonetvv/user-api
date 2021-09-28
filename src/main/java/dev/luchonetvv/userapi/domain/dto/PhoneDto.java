package dev.luchonetvv.userapi.domain.dto;

public class PhoneDto {
    private String number;
    private String cityCode;
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
