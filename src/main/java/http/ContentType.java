package http;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public enum ContentType {
    HTML("text/html"),
    JSON("application/json"),
    TEXT("text/plain");

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String name;

    ContentType(String name) {
        setName(name);
    }
}
