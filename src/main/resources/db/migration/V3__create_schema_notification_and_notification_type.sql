-- Tabla 'notification_types'
CREATE TABLE IF NOT EXISTS notification_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Tabla 'notifications'
CREATE TABLE IF NOT EXISTS notifications (
    id SERIAL PRIMARY KEY,
    header VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    sender VARCHAR(100) NOT NULL,
    receiver VARCHAR(100) NOT NULL,
    notification_type_id BIGINT NOT NULL,
    is_sent BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_type FOREIGN KEY (notification_type_id)
        REFERENCES notification_type (id)
);


