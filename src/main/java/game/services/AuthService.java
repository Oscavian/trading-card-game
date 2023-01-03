package game.services;

import game.models.User;

import java.util.concurrent.ConcurrentHashMap;

public class AuthService {
    private final ConcurrentHashMap<String, String> session_tokens = new ConcurrentHashMap<>();

    public String login(String user) {
        String token = user + "-mtcgToken";
        session_tokens.put(token, user);
        return token;
    }

    public void logout(String user) {
        session_tokens.remove(user + "-mtcgToken");
    }

    public String getLogin(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        return session_tokens.get(token);
    }
}
