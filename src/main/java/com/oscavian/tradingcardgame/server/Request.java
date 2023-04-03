package com.oscavian.tradingcardgame.server;

import com.oscavian.tradingcardgame.http.HttpMethod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.Locale;

@Getter
@Setter(AccessLevel.PROTECTED)
public class Request {
    private HttpMethod method;
    private String params;
    private String pathname;
    private String contentType;
    private Integer contentLength;

    private String authorization;
    private String body = "";

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String CONTENT_TYPE = "Content-Type: ";

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String CONTENT_LENGTH = "Content-Length: ";

    private final String AUTHORIZATION = "Authorization: Bearer ";

    public Request(BufferedReader inputStream) {
        assembleRequest(inputStream);
    }

    private void assembleRequest(BufferedReader inputStream) {
        try {
            String line = inputStream.readLine();

            if (line != null) {
                String[] splitFirstLine = line.split(" ");
                Boolean hasParams = splitFirstLine[1].contains("?");

                setMethod(getMethodFromInputLine(splitFirstLine));
                setPathname(getPathnameFromInputLine(splitFirstLine, hasParams));
                setParams(getParamsFromInputLine(splitFirstLine, hasParams));

                while(!line.isEmpty()){
                    line = inputStream.readLine();
                    if (line.startsWith(CONTENT_LENGTH)){
                        setContentLength(getContentLengthFromInputLine(line));
                    }

                    if (line.startsWith(CONTENT_TYPE)){
                        setContentType(getContentTypeFromInputLine(line));
                    }

                    if (line.startsWith(AUTHORIZATION)) {
                        setAuthorization(getAuthorizationFromInputLine(line));
                    }
                }

                if (getMethod() == HttpMethod.POST || getMethod() == HttpMethod.PUT){
                    int asciiChar;
                    if (getContentLength() != null){
                        for (int i = 0; i < getContentLength(); i++) {
                            asciiChar = inputStream.read();
                            String body = getBody();
                            setBody(body + (char) asciiChar);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param splitFirstLine First line of the HTTP Header
     * @return HttpMethod enum value
     */
    private HttpMethod getMethodFromInputLine(String[] splitFirstLine){
        return HttpMethod.valueOf(splitFirstLine[0].toUpperCase(Locale.ROOT));
    }

    /**
     * @param splitFirstLine First line of the HTTP Header
     * @param hasParams If the path has params followed by a "?"
     * @return pathname String
     */
    private String getPathnameFromInputLine(String[] splitFirstLine, Boolean hasParams) {
        if (hasParams) {
            return splitFirstLine[1].split("\\?")[0];
        }

        return splitFirstLine[1];
    }

    private String getParamsFromInputLine(String[] splitFirstLine, Boolean hasParams) {
        if (hasParams) {
            return splitFirstLine[1].split("\\?")[1];
        }

        return "";
    }

    private Integer getContentLengthFromInputLine(String line) {
        return Integer.parseInt(line.substring(CONTENT_LENGTH.length()));
    }

    private String getContentTypeFromInputLine(String line) {
        return line.substring(CONTENT_TYPE.length());
    }
    private String getAuthorizationFromInputLine(String line) {
        return line.substring(AUTHORIZATION.length());
    }



}
