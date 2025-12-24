package com.example.pouch.server.controller;

import com.example.pouch.server.model.Role;
import com.example.pouch.server.service.UserService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public Map<String, Object> getUser(
            @PathVariable("id") String id,
            @RequestParam(value = "fields", required = false) String fields,
            @RequestHeader(value = "X-Role", required = false, defaultValue = "USER") Role role) {
        return userService.getUser(id, fields, role);
    }

    @GetMapping("/users")
    public List<Map<String, Object>> listUsers(
            @RequestParam(value = "fields", required = false) String fields,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestHeader(value = "X-Role", required = false, defaultValue = "USER") Role role) {
        return userService.listUsers(fields, role, limit);
    }
}
