package trainingapp.repository;

import trainingapp.model.Exercise;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseRepository {

    // Dodaj nowe ćwiczenie
    public void save(Exercise exercise) {
        String sql = "INSERT INTO exercises (name, description, muscle_group) VALUES (?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, exercise.getName());
                stmt.setString(2, exercise.getDescription());
                stmt.setString(3, exercise.getMuscleGroup());
                stmt.executeUpdate();

                // Pobierz wygenerowane ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        exercise.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas zapisywania ćwiczenia: " + e.getMessage());
            throw new RuntimeException("Nie udało się zapisać ćwiczenia", e);
        }
    }

    // Znajdź ćwiczenie po ID
    public Exercise findById(int id) {
        String sql = "SELECT * FROM exercises WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Exercise(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("muscle_group")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas wyszukiwania ćwiczenia po ID: " + e.getMessage());
            throw new RuntimeException("Nie udało się znaleźć ćwiczenia", e);
        }
        return null;
    }

    // Znajdź wszystkie ćwiczenia
    public List<Exercise> findAll() {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercises";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    exercises.add(new Exercise(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("muscle_group")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania wszystkich ćwiczeń: " + e.getMessage());
            throw new RuntimeException("Nie udało się pobrać ćwiczeń", e);
        }
        return exercises;
    }

    // Usuń ćwiczenie po ID
    public void delete(int id) {
        String sql = "DELETE FROM exercises WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas usuwania ćwiczenia: " + e.getMessage());
            throw new RuntimeException("Nie udało się usunąć ćwiczenia", e);
        }
    }
}