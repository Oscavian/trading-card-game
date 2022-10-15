package server;

import http.ContentType;
import http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class Response {
    private int statusCode;
    private String statusMessage;
    private String contentType;
    private String content;

    public Response(HttpStatus status, ContentType contentType, String content) {
        setStatusCode(status.getCode());
        setContentType(contentType.getName());
        setContent(content);
        setStatusMessage(status.getMsg());
    }

    protected String build() {
        return "HTTP/1.1 " + getStatusCode() + " " + getStatusMessage() + "\r\n" +
               "Content-Type: " + getContentType() + "\r\n" +
               "Content-Length: " + getContent().length() + "\r\n\r\n" +
                getContent();
    }
}