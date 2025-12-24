INSERT OR IGNORE INTO users (id, name, email, phone, avatar_url, last_login, internal_notes, password_hash)
VALUES
    ('u-100', 'Ava Patel', 'ava@example.com', '+1-555-0100', 'https://example.com/ava.png', '2024-06-01T10:15:30Z', 'VIP customer', 'hash1'),
    ('u-200', 'Noah Smith', 'noah@example.com', '+1-555-0200', 'https://example.com/noah.png', '2024-06-02T09:05:10Z', 'Needs follow-up', 'hash2'),
    ('u-300', 'Liam Chen', 'liam@example.com', '+1-555-0300', 'https://example.com/liam.png', '2024-06-03T08:45:00Z', 'Internal review', 'hash3');
