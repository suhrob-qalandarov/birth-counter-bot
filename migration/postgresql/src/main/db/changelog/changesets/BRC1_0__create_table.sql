set search_path to bot_core;

CREATE TABLE IF NOT EXISTS bot_core.birth_records (
    id BIGSERIAL PRIMARY KEY,
    tg_user_id BIGINT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    gender VARCHAR(10),
    
    -- Audit fields
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    
    CONSTRAINT fk_itg_birth_records_tg_user FOREIGN KEY (tg_user_id) REFERENCES bot_core.tg_users(id)
);
