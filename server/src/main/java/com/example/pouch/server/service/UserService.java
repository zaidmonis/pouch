package com.example.pouch.server.service;

import com.example.pouch.server.model.Role;
import com.example.pouch.server.repository.UserRepository;
import com.example.pouch.server.selection.FieldSelection;
import com.example.pouch.server.selection.FieldSelectionParser;
import com.example.pouch.server.selection.UserFieldPolicy;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private static final int DEFAULT_LIMIT = 3;
    private static final int MAX_LIMIT = 50;

    private final UserRepository userRepository;
    private final FieldSelectionParser parser = new FieldSelectionParser();
    private final UserFieldPolicy policy = new UserFieldPolicy();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> getUser(String userId, String rawFields, Role role) {
        List<FieldSelection> selections = policy.validateAndMap(parser.parse(rawFields), role);
        return userRepository.findById(userId, selections)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
    }

    public List<Map<String, Object>> listUsers(String rawFields, Role role, Integer limit) {
        List<FieldSelection> selections = policy.validateAndMap(parser.parse(rawFields), role);
        int resolvedLimit = limit == null ? DEFAULT_LIMIT : limit;
        if (resolvedLimit <= 0 || resolvedLimit > MAX_LIMIT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Limit must be between 1 and " + MAX_LIMIT + ".");
        }
        return userRepository.findAll(selections, resolvedLimit);
    }
}
