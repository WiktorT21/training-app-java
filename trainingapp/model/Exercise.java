package trainingapp.model;

public class Exercise {
    private int id;
    private String name;
    private String description;
    private String muscleGroup;

    // Konstruktor domyślny
    public Exercise() {}

    // Konstruktor z parametrami
    public Exercise(int id, String name, String description, String muscleGroup) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
    }

    public Exercise(String name, String description, String muscleGroup) { 
        this.name = name; 
        this.description = description; 
        this.muscleGroup = muscleGroup; }

    // Gettery i settery
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", muscleGroup='" + muscleGroup + '\'' +
                '}';
    }
}