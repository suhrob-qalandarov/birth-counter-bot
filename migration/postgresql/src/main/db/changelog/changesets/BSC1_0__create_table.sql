set search_path to bot_core;

-- Create bot_sessions table
CREATE TABLE IF NOT EXISTS bot_core.bot_sessions (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT UNIQUE NOT NULL,
    step VARCHAR(50) NOT NULL,
    temp_year INT,
    temp_month INT,
    temp_day INT,
    temp_full_name VARCHAR(255),
    temp_gender VARCHAR(50),
    
    -- Audit fields
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);
