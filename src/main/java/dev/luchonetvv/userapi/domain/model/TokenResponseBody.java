package dev.luchonetvv.userapi.domain.model;

public class TokenResponseBody {
    String token;

    public TokenResponseBody() { }

    public TokenResponseBody(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenResponseBody [token=" + token + "]";
    }
}
