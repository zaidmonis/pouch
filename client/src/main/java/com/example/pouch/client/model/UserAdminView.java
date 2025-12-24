package com.example.pouch.client.model;

import java.time.Instant;

public record UserAdminView(String id, String name, String email, Instant lastLogin) {
}
