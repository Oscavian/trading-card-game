package com.oscavian.tradingcardgame.server;

import com.oscavian.tradingcardgame.http.ContentType;
import com.oscavian.tradingcardgame.http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
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

    public Response(HttpStatus status) {
        setContentType(ContentType.JSON);
        setStatusCode(status.getCode());
        setContent(null);
        setStatusMessage(status.getMsg());
        setDescription(status.getMsg());
    }


    protected String build() throws IllegalArgumentException {

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
               "Content-Length: " + getContent().getBytes().length + "\r\n\r\n" +
                getContent();
    }
}