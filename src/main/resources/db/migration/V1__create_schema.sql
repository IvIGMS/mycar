-- Tabla 'users'
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla 'cars'
CREATE TABLE IF NOT EXISTS cars (
    id SERIAL PRIMARY KEY,
    company_name VARCHAR(50) NOT NULL,
    model_name VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
    km INT NOT NULL,
    is_active BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cars_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Tabla 'status'
CREATE TABLE IF NOT EXISTS status (
    id SERIAL PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla 'types'
CREATE TABLE IF NOT EXISTS types (
    id SERIAL PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla 'issues'
CREATE TABLE IF NOT EXISTS issues (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    notification_date TIMESTAMP,
    notification_distance INT,
    current_distance INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    car_id INT NOT NULL,
    status_id INT NOT NULL,
    type_id INT NOT NULL,
    CONSTRAINT fk_issues_car FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE,
    CONSTRAINT fk_issues_status FOREIGN KEY (status_id) REFERENCES status (id),
    CONSTRAINT fk_issues_type FOREIGN KEY (type_id) REFERENCES types (id),
    CONSTRAINT uq_issues_name_user UNIQUE (name, car_id)
);
