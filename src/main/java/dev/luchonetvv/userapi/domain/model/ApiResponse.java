package dev.luchonetvv.userapi.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ApiResponse<T> {
    private T body;
    private ApiError error;
    private Object message;
    
    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiResponse [body=" + body + ", error=" + error + ", message=" + message + "]";
    }
}
