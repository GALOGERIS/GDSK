CREATE TABLE IF NOT EXISTS events (
 id TEXT PRIMARY KEY, group_name TEXT NOT NULL, type TEXT NOT NULL DEFAULT 'lesson',
 title TEXT NOT NULL, start_time TEXT NOT NULL, end_time TEXT NOT NULL DEFAULT '',
 teacher TEXT NOT NULL DEFAULT '', room TEXT NOT NULL DEFAULT '', details TEXT NOT NULL DEFAULT '',
 audience TEXT NOT NULL DEFAULT 'group', event_date TEXT NOT NULL, created_at TEXT NOT NULL
);
CREATE INDEX IF NOT EXISTS events_group_date ON events(group_name,event_date,start_time);
CREATE TABLE IF NOT EXISTS devices (
 token TEXT PRIMARY KEY, group_name TEXT NOT NULL, updated_at TEXT NOT NULL
);
CREATE INDEX IF NOT EXISTS devices_group ON devices(group_name);