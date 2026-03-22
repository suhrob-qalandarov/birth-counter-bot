set search_path to bot_core;

CREATE TABLE IF NOT EXISTS bot_core.tg_users (
    id BIGINT PRIMARY KEY,
    chat_id BIGINT UNIQUE NOT NULL,
    username VARCHAR(255) UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(50),
    is_premium BOOLEAN DEFAULT FALSE,
    can_join_groups BOOLEAN DEFAULT FALSE,
    language_code VARCHAR(10),
    is_bot BOOLEAN DEFAULT FALSE,
    status INT DEFAULT 1,
    app_user_id BIGINT,
    
    -- Audit fields
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);
