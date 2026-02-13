package trainingapp.repository;

import trainingapp.model.Training;
import trainingapp.model.TrainingEntry;
import java.sql.*;

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
}