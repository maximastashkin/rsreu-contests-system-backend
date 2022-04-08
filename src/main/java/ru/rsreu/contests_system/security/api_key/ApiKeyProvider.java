package ru.rsreu.contests_system.security.api_key;

public record ApiKeyProvider(String validApiKey) {
    public boolean validateApiKey(String apiKey) {
        return validApiKey.equals(apiKey);
    }
}
