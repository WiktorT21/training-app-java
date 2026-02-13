import trainingapp.model.Exercise;
import trainingapp.model.Training;
import trainingapp.model.TrainingEntry;
import trainingapp.repository.ExerciseRepository;
import trainingapp.repository.TrainingRepository;
import trainingapp.repository.DatabaseConnection; // Dodany import

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- ROZPOCZYNAM TEST APLIKACJI ---");

        // 1. NAJWAŻNIEJSZE: Tworzymy tabele, jeśli nie istnieją
        createTables();

        try {
            // 2. Przygotowanie repozytoriów
            ExerciseRepository exerciseRepo = new ExerciseRepository();
            TrainingRepository trainingRepo = new TrainingRepository();

            // 3. Tworzymy i zapisujemy ćwiczenie
            Exercise deadlift = new Exercise("Martwy Ciąg", "Opis techniki...", "Plecy/Nogi");
            exerciseRepo.save(deadlift);
            System.out.println("1. Zapisano ćwiczenie: " + deadlift.getName() + " (ID: " + deadlift.getId() + ")");

            // 4. Tworzymy nowy trening
            Training todayTraining = new Training(LocalDate.now(), "Ciężki trening siłowy");

            // 5. Dodajemy ćwiczenie do treningu
            TrainingEntry entry1 = new TrainingEntry(deadlift, 3, 5, 100.0);
            todayTraining.addEntry(entry1);

            System.out.println("2. Przygotowano trening z " + todayTraining.getEntries().size() + " ćwiczeniami.");

            // 6. Zapisujemy cały trening
            trainingRepo.save(todayTraining);
            System.out.println("3. SUKCES! Trening został zapisany w bazie danych.");

        } catch (Exception e) {
            System.err.println("!!! BŁĄD PODCZAS TESTU !!!");
            e.printStackTrace();
        }
    }

    // Metoda do tworzenia struktury bazy danych
    private static void createTables() {
        String createExercises = "CREATE TABLE IF NOT EXISTS exercises (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(255) NOT NULL, " +
                "description TEXT, " +
                "muscle_group VARCHAR(255) NOT NULL)";

        String createTrainings = "CREATE TABLE IF NOT EXISTS trainings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date DATE NOT NULL, " +
                "notes TEXT)";

        String createTrainingExercises = "CREATE TABLE IF NOT EXISTS training_exercises (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "training_id INTEGER NOT NULL, " +
                "exercise_id INTEGER NOT NULL, " +
                "sets INTEGER NOT NULL, " +
                "reps INTEGER NOT NULL, " +
                "weight REAL NOT NULL, " +
                "FOREIGN KEY (training_id) REFERENCES trainings(id), " +
                "FOREIGN KEY (exercise_id) REFERENCES exercises(id))";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createExercises);
            stmt.execute(createTrainings);
            stmt.execute(createTrainingExercises);
            System.out.println("0. Baza danych została zainicjalizowana (tabele gotowe).");
            
        } catch (SQLException e) {
            System.err.println("Błąd podczas tworzenia tabel: " + e.getMessage());
        }
    }
}