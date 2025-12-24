package com.example.pouch.server.selection;

public record FieldSelection(String apiField, String dbColumn) {
    public String columnWithAlias() {
        if (apiField.equals(dbColumn)) {
            return dbColumn;
        }
        return dbColumn + " AS " + apiField;
    }
}
