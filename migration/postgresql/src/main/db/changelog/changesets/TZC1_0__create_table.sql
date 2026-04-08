set search_path to bot_core;

CREATE TABLE IF NOT EXISTS bot_core.timezones (
    id BIGSERIAL PRIMARY KEY,
    zone_name VARCHAR(255) UNIQUE NOT NULL,
    country_code VARCHAR(3),
    utc_offset_seconds INT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- Audit fields
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_tz_country ON bot_core.timezones(country_code);

-- Insert initial values for Uzbekistan
INSERT INTO bot_core.timezones(zone_name, country_code, utc_offset_seconds, is_active, created_by, updated_by)
VALUES ('Asia/Tashkent', 'UZ', 18000, TRUE, 'system', 'system')
ON CONFLICT (zone_name) DO NOTHING;
