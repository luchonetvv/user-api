package dev.luchonetvv.userapi.domain.model;

public class ApiError {
    String message;
    String causeMessage;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCauseMessage() {
        return causeMessage;
    }

    public void setCauseMessage(String causeMessage) {
        this.causeMessage = causeMessage;
    }

    @Override
    public String toString() {
        return "ApiError [causeMessage=" + causeMessage + ", message=" + message + "]";
    }
}
