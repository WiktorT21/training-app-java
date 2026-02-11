-- Tabela dla ćwiczeń w treningach
CREATE TABLE training_exercises (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    training_id INTEGER NOT NULL,
    exercise_id INTEGER NOT NULL,
    sets INTEGER NOT NULL,
    reps INTEGER NOT NULL,
    weight REAL NOT NULL,
    FOREIGN KEY (training_id) REFERENCES trainings(id),
    FOREIGN KEY (exercise_id) REFERENCES exercises(id)
);

-- Tabela dla treningów
CREATE TABLE trainings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    date DATE NOT NULL,
    notes TEXT
);

-- Tabela dla rodzajów ćwiczeń
CREATE TABLE exercises (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL,
    muscle_group VARCHAR(255) NOT NULL,
    description TEXT
);

