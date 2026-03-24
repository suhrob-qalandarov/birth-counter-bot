set search_path to bot_core;

-- Update birth_records table
ALTER TABLE bot_core.birth_records ADD COLUMN IF NOT EXISTS next_notification_time_utc TIMESTAMP WITHOUT TIME ZONE;

CREATE INDEX IF NOT EXISTS idx_br_next_notif ON bot_core.birth_records(next_notification_time_utc);
CREATE INDEX IF NOT EXISTS idx_br_user_id ON bot_core.birth_records(tg_user_id);

-- Audit fields
ALTER TABLE bot_core.birth_records ADD COLUMN IF NOT EXISTS created_by VARCHAR(255) NOT NULL DEFAULT 'system';
ALTER TABLE bot_core.birth_records ADD COLUMN IF NOT EXISTS updated_by VARCHAR(255) NOT NULL DEFAULT 'system';
ALTER TABLE bot_core.birth_records ALTER COLUMN created_by DROP DEFAULT;
ALTER TABLE bot_core.birth_records ALTER COLUMN updated_by DROP DEFAULT;
