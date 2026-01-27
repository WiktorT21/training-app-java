package trainingapp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Training {
    private int id;
    private LocalDate date;
    private List<TrainingEntry> entries;
    private String notes;

    public Training(LocalDate date, String notes) {
        this.date = date;
        this.entries = new ArrayList<>();
        this.notes = notes;
    }

    public Training(int id,LocalDate date, String notes) {
        this.id = id;
        this.date = date;
        this.entries = new ArrayList<>();
        this.notes = notes;
    }

    public void addEntry(TrainingEntry entry) {
        entries.add(entry);
    }

    public void removeEntry(TrainingEntry entry) {
        entries.remove(entry);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<TrainingEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", date=" + date +
                ", entries=" + entries.size() +
                '}';
    }
}