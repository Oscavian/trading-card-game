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
    //private String contentType;

    private ContentType contentType;
    private String content;
    private String description;


    public Response(HttpStatus status, ContentType contentType, String content, String description) {
        setStatusCode(status.getCode());
        setContentType(contentType);
        setContent(content);
        setStatusMessage(status.getMsg());
        setDescription(description);
    }

    protected String build() {

        switch (contentType) {
            case JSON -> setContent(
                    "{\"content\": " + getContent() + ", \"description\": \"" + getDescription() + "\"}"
            );
            case TEXT -> setContent(content + "\nDescription: " + description);
            case HTML -> setContent(content);
            default -> throw new IllegalArgumentException("Illegal Content Type \"" + contentType.getName() + "\"");
        }

        return "HTTP/1.1 " + getStatusCode() + " " + getStatusMessage() + "\r\n" +
               "Content-Type: " + getContentType() + "\r\n" +
               "Content-Length: " + getContent().length() + "\r\n\r\n" +
                getContent();
    }
}