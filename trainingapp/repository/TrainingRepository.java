package trainingapp.repository;

import trainingapp.model.Training;
import trainingapp.model.TrainingEntry;
import trainingapp.model.Exercise;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainingRepository {

    public void save(Training training) {
        String insertTrainingSql = "INSERT INTO trainings (date, notes) VALUES (?, ?)";
        String insertEntrySql = "INSERT INTO training_exercises (training_id, exercise_id, sets, reps, weight) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            
            // 1. Rozpoczynamy transakcję (wyłączamy automatyczny zapis)
            conn.setAutoCommit(false); 

            // 2. Zapisujemy TRENING (nagłówek)
            int trainingId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(insertTrainingSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setDate(1, java.sql.Date.valueOf(training.getDate()));
                stmt.setString(2, training.getNotes());
                stmt.executeUpdate();

                // Pobieramy ID, które baza nadała temu treningowi
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        trainingId = generatedKeys.getInt(1);
                        training.setId(trainingId); 
                    } else {
                        throw new SQLException("Nie udało się pobrać ID treningu.");
                    }
                }
            }

            // 3. Zapisujemy WPISY (ćwiczenia) przypisane do tego treningu
            try (PreparedStatement stmt = conn.prepareStatement(insertEntrySql)) {
                for (TrainingEntry entry : training.getEntries()) {
                    stmt.setInt(1, trainingId); 
                    stmt.setInt(2, entry.getExercise().getId());
                    stmt.setInt(3, entry.getSets());
                    stmt.setInt(4, entry.getReps());
                    stmt.setDouble(5, entry.getWeight());
                    
                    stmt.addBatch(); 
                }
                stmt.executeBatch(); 
            }

            conn.commit(); 
            System.out.println("Trening zapisany pomyślnie! ID: " + trainingId);

        } catch (SQLException e) {
            e.printStackTrace();
            // 5. W razie błędu -> COFAMY wszystko (Rollback)
            if (conn != null) {
                try {
                    System.err.println("Wycofywanie transakcji...");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // 6. Sprzątanie: przywracamy domyślny tryb
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Training> getAllTrainings() {
        List<Training> trainings = new ArrayList<>();
        String selectTrainingsSql = "SELECT id, date, notes FROM trainings ORDER BY date DESC";
        
        // POPRAWKA 1: Pobieramy wszystkie kolumny ćwiczenia (description, muscle_group)
        String selectEntriesSql = "SELECT te.id, te.exercise_id, te.sets, te.reps, te.weight, " + 
                                  "e.name, e.description, e.muscle_group " +
                                  "FROM training_exercises te " +
                                  "JOIN exercises e ON te.exercise_id = e.id " +
                                  "WHERE te.training_id = ?";

        // POPRAWKA 2: Poprawna konstrukcja try-with-resources (otwieramy oba zapytania tutaj)
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtTrainings = conn.prepareStatement(selectTrainingsSql);
             PreparedStatement stmtEntries = conn.prepareStatement(selectEntriesSql)) {

            try (ResultSet rsTrainings = stmtTrainings.executeQuery()) {
                while (rsTrainings.next()) {
                    int trainingId = rsTrainings.getInt("id");
                    java.sql.Date sqlDate = rsTrainings.getDate("date");
                    String notes = rsTrainings.getString("notes");

                    Training training = new Training(trainingId, sqlDate.toLocalDate(), notes);

                    // Ustawiamy ID treningu w zapytaniu o ćwiczenia
                    stmtEntries.setInt(1, trainingId);
                    
                    try (ResultSet rsEntries = stmtEntries.executeQuery()) {
                        while (rsEntries.next()) {
                            int entryId = rsEntries.getInt("id");
                            int exerciseId = rsEntries.getInt("exercise_id");
                            String exerciseName = rsEntries.getString("name");
                            // POPRAWKA 1 cd.: Pobieramy brakujące dane
                            String description = rsEntries.getString("description");
                            String muscleGroup = rsEntries.getString("muscle_group");
                            
                            int sets = rsEntries.getInt("sets");
                            int reps = rsEntries.getInt("reps");
                            double weight = rsEntries.getDouble("weight");

                            // POPRAWKA 3: Używamy poprawnego konstruktora Exercise (z 4 argumentami)
                            Exercise exercise = new Exercise(exerciseId, exerciseName, description, muscleGroup);
                            TrainingEntry entry = new TrainingEntry(entryId, exercise, sets, reps, weight, "");

                            training.addEntry(entry);
                        }
                    }
                    // POPRAWKA 4: Dodajemy trening do listy WEWNĄTRZ pętli while
                    trainings.add(training);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trainings;
    }
}