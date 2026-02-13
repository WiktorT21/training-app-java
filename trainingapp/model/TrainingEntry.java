package trainingapp.model;

import java.util.Objects;

public class TrainingEntry {
	private int id;
	private Exercise exercise;
	private int sets;
	private int reps;
	private double weight; // w kilogramach

	public TrainingEntry() {}

	public TrainingEntry(Exercise exercise, int sets, int reps, double weight) {
		this.exercise = exercise;
		this.sets = sets;
		this.reps = reps;
		this.weight = weight;
	}

	public TrainingEntry(int id, Exercise exercise, int sets, int reps, double weight, String notes) {
		this.id = id;
		this.exercise = exercise;
		this.sets = sets;
		this.reps = reps;
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public int getSets() {
		return sets;
	}

	public void setSets(int sets) {
		this.sets = sets;
	}

	public int getReps() {
		return reps;
	}

	public void setReps(int reps) {
		this.reps = reps;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "TrainingEntry{" +
				"id=" + id +
				", exercise=" + (exercise != null ? exercise.getName() : "null") +
				", sets=" + sets +
				", reps=" + reps +
				", weight=" + weight +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TrainingEntry that = (TrainingEntry) o;
		return id == that.id && sets == that.sets && reps == that.reps && Double.compare(that.weight, weight) == 0 && Objects.equals(exercise, that.exercise);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, exercise, sets, reps, weight);
	}
}

