package org.alilopez.managers;


public interface TokenManager {
    String issueToken(String nombre);

    boolean authorize(String token, String nombre);
}
