package com.example.pouch.server.repository;

import com.example.pouch.server.selection.FieldSelection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Map<String, Object>> findById(String userId, List<FieldSelection> selections) {
        String columns = selections.stream()
                .map(FieldSelection::columnWithAlias)
                .collect(Collectors.joining(", "));
        String sql = "SELECT " + columns + " FROM users WHERE id = ?";

        List<Map<String, Object>> rows = jdbcTemplate.query(sql, this::mapRow, userId);
        return rows.stream().findFirst();
    }

    public List<Map<String, Object>> findAll(List<FieldSelection> selections, int limit) {
        String columns = selections.stream()
                .map(FieldSelection::columnWithAlias)
                .collect(Collectors.joining(", "));
        String sql = "SELECT " + columns + " FROM users LIMIT ?";

        return jdbcTemplate.query(sql, this::mapRow, limit);
    }

    private Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        Map<String, Object> row = new LinkedHashMap<>();
        for (int i = 1; i <= count; i++) {
            String label = meta.getColumnLabel(i);
            row.put(label, rs.getObject(i));
        }
        return row;
    }
}
