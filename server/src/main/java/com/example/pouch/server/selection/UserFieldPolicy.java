package com.example.pouch.server.selection;

import com.example.pouch.server.model.Role;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserFieldPolicy {
    private static final Map<String, String> WHITELIST;
    private static final Set<String> ADMIN_ONLY_FIELDS = Set.of("lastLogin");
    private static final List<String> DEFAULT_FIELDS = List.of("id", "name", "avatarUrl");

    static {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", "id");
        map.put("name", "name");
        map.put("email", "email");
        map.put("phone", "phone");
        map.put("avatarUrl", "avatar_url");
        map.put("lastLogin", "last_login");
        WHITELIST = Collections.unmodifiableMap(map);
    }

    public List<FieldSelection> validateAndMap(List<String> requestedFields, Role role) {
        List<String> fields = requestedFields.isEmpty() ? DEFAULT_FIELDS : requestedFields;

        for (String field : fields) {
            if (!WHITELIST.containsKey(field)) {
                throw new FieldSelectionException("Unknown or forbidden field: " + field);
            }
            if (ADMIN_ONLY_FIELDS.contains(field) && role != Role.ADMIN) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Field '%s' requires ADMIN role.".formatted(field));
            }
        }

        return fields.stream()
                .map(field -> new FieldSelection(field, WHITELIST.get(field)))
                .collect(Collectors.toList());
    }
}
