CREATE TABLE IF NOT EXISTS users (
    id TEXT PRIMARY KEY,
    name TEXT,
    email TEXT,
    phone TEXT,
    avatar_url TEXT,
    last_login TEXT,
    internal_notes TEXT,
    password_hash TEXT
);
