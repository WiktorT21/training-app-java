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
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- URUCHAMIAM APLIKACJĘ (menu) ---");

        // Tworzymy tabele, jeśli nie istnieją
        createTables();
        System.out.println("Witaj w dzienniku treningowym!");

        Scanner scanner  = new Scanner(System.in);
        boolean isRunning = true;

        // Przygotowanie repozytoriów raz
        ExerciseRepository exerciseRepo = new ExerciseRepository();
        TrainingRepository trainingRepo = new TrainingRepository();

        while (isRunning) {
            System.out.println();
            System.out.println("Wybierz opcję:");
            System.out.println("1 - Dodaj nowe ćwiczenie");
            System.out.println("2 - Wyświetl liste dostępnych ćwiczeń");
            System.out.println("3 - Wyświetl historie treningów");
            System.out.println("4 - Wyjdź");
            System.out.print("Twój wybór: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    uiAddExercise(exerciseRepo, scanner); // Wywołujemy funkcję dodawania
                    break;
                case "2":
                    uiShowExercises(exerciseRepo); // Wywołujemy funkcję wyświetlania ćwiczeń
                    break;
                case "3":
                    uiShowHistory(trainingRepo); // Wywołujemy funkcję historii
                    break;
                case "4":
                    isRunning = false;
                    System.out.println("Koniec. Do widzenia!");
                    break;
                default:
                    System.out.println("Nieznana opcja, spróbuj ponownie.");
            }
        }

        scanner.close();
    }

    private static void uiAddExercise(ExerciseRepository exerciseRepo, TrainingRepository trainingRepo) {
        try {
            // Tworzymy i zapisujemy przykładowe ćwiczenie
            Exercise deadlift = new Exercise("Martwy Ciąg", "Opis techniki...", "Plecy/Nogi");
            exerciseRepo.save(deadlift);
            System.out.println("1. Zapisano ćwiczenie: " + deadlift.getName() + " (ID: " + deadlift.getId() + ")");

            // Tworzymy nowy trening
            Training todayTraining = new Training(LocalDate.now(), "Ciężki trening siłowy");

            // Dodajemy ćwiczenie do treningu
            TrainingEntry entry1 = new TrainingEntry(deadlift, 3, 5, 100.0);
            todayTraining.addEntry(entry1);

            System.out.println("2. Przygotowano trening z " + todayTraining.getEntries().size() + " ćwiczeniami.");

            // Zapisujemy cały trening
            trainingRepo.save(todayTraining);
            System.out.println("3. SUKCES! Trening został zapisany w bazie danych.");

        } catch (Exception e) {
            System.err.println("!!! BŁĄD PODCZAS DEMO !!!");
            e.printStackTrace();
        }
    }

    private static void uiShowExercises(ExerciseRepository exerciseRepo) {
        try {
            List<Exercise> exercises = exerciseRepo.findAll();
            if (exercises.isEmpty()) {
                System.out.println("Brak zapisanych ćwiczeń.");
                return;
            }
            System.out.println("Lista ćwiczeń:");
            for (Exercise ex : exercises) {
                System.out.println("- ID: " + ex.getId() + " | " + ex.getName() + " | " + ex.getMuscleGroup());
                if (ex.getDescription() != null && !ex.getDescription().isEmpty()) {
                    System.out.println("    Opis: " + ex.getDescription());
                }
            }
        } catch (Exception e) {
            System.err.println("Błąd podczas pobierania ćwiczeń: " + e.getMessage());
        }
    }

    private static void uiShowHistory(TrainingRepository trainingRepo) {
        try {
            List<Training> trainings = trainingRepo.getAllTrainings();
            if (trainings.isEmpty()) {
                System.out.println("Brak zapisanych treningów.");
                return;
            }
            System.out.println("Historia treningów:");
            for (Training t : trainings) {
                System.out.println("- Trening ID: " + t.getId() + " | Data: " + t.getDate() + " | Wpisy: " + t.getEntries().size());
                if (t.getNotes() != null && !t.getNotes().isEmpty()) {
                    System.out.println("    Notatki: " + t.getNotes());
                }
                for (TrainingEntry e : t.getEntries()) {
                    System.out.println("    * " + e.getExercise().getName() + " (ID:" + e.getExercise().getId() + ") - " + e.getSets() + "x" + e.getReps() + " @ " + e.getWeight() + "kg");
                }
            }
        } catch (Exception e) {
            System.err.println("Błąd podczas pobierania historii treningów: " + e.getMessage());
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