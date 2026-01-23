-- Tabela dla ćwiczeń w treningach
CREATE TABLE training_exercises (
    id SERIAL PRIMARY KEY AUTO_INCREMENT,
    training_id INTEGER NOT NULL,
    exercise_id INTEGER NOT NULL,
    sets INTEGER NOT NULL,
    reps INTEGER NOT NULL,
    weight Real NOT NULL,
    FOREIGN KEY (training_id) REFERENCES trainings(id),
    FOREIGN KEY (exercise_id) REFERENCES exercises(id)
);

-- Tabela dla treningów
CREATE TABLE trainings (
    id SERIAL PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL
);

-- Tabela dla rodzajów ćwiczeń
CREATE TABLE exercises (
    id SERIAL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    muscle_group VARCHAR(255) NOT NULL,
    description TEXT
);

