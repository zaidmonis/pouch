package com.example.pouch.server.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FieldSelectionParser {
    public static final int MAX_FIELDS = 10;
    public static final int MAX_FIELD_NAME_LENGTH = 40;

    public List<String> parse(String rawFields) {
        if (rawFields == null || rawFields.isBlank()) {
            return Collections.emptyList();
        }

        String[] parts = rawFields.split(",");
        if (parts.length > MAX_FIELDS) {
            throw new FieldSelectionException("Too many fields requested. Max is " + MAX_FIELDS + ".");
        }

        List<String> fields = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (trimmed.length() > MAX_FIELD_NAME_LENGTH) {
                throw new FieldSelectionException("Field name too long: " + trimmed);
            }
            fields.add(trimmed);
        }

        if (fields.size() > MAX_FIELDS) {
            throw new FieldSelectionException("Too many fields requested. Max is " + MAX_FIELDS + ".");
        }
        return fields;
    }
}
