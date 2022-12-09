package http;

import lombok.Setter;
import lombok.Getter;
import lombok.AccessLevel;

public enum HttpStatus {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_FOUND(404, "Not Found"),
    CREATED(201, "Created"),
    METHOD_NOT_ALLOWED(405, "Method not allowed"),

    CONFLICT(409, "Conflict");

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int code;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String msg;

    HttpStatus(int code, String msg) {
        setCode(code);
        setMsg(msg);
    }
}
