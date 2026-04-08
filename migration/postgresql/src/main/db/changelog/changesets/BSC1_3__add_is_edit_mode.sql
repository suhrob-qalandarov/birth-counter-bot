-- file: BSC1_3__add_is_edit_mode.sql
ALTER TABLE bot_core.bot_sessions
    ADD COLUMN is_edit_mode BOOLEAN DEFAULT FALSE;
