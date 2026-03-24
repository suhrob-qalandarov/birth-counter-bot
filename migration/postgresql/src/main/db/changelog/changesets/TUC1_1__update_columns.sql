set search_path to bot_core;

-- Update tg_users table
ALTER TABLE bot_core.tg_users ADD COLUMN IF NOT EXISTS timezone_id VARCHAR(100) DEFAULT 'Asia/Tashkent';
ALTER TABLE bot_core.tg_users ADD COLUMN IF NOT EXISTS latitude DOUBLE PRECISION;
ALTER TABLE bot_core.tg_users ADD COLUMN IF NOT EXISTS longitude DOUBLE PRECISION;
ALTER TABLE bot_core.tg_users ADD COLUMN IF NOT EXISTS notification_time TIME;
ALTER TABLE bot_core.tg_users ADD COLUMN IF NOT EXISTS notification_time_utc TIME;

CREATE INDEX IF NOT EXISTS idx_tguser_notif_utc ON bot_core.tg_users(notification_time_utc);
CREATE INDEX IF NOT EXISTS idx_tg_user_status ON bot_core.tg_users(status);
CREATE INDEX IF NOT EXISTS idx_tg_user_chat_id ON bot_core.tg_users(chat_id);

-- Audit fields
ALTER TABLE bot_core.tg_users ADD COLUMN IF NOT EXISTS created_by VARCHAR(255) NOT NULL DEFAULT 'system';
ALTER TABLE bot_core.tg_users ADD COLUMN IF NOT EXISTS updated_by VARCHAR(255) NOT NULL DEFAULT 'system';
ALTER TABLE bot_core.tg_users ALTER COLUMN created_by DROP DEFAULT;
ALTER TABLE bot_core.tg_users ALTER COLUMN updated_by DROP DEFAULT;
