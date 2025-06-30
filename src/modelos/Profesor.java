package modelos;

public class Profesor {
    private String id;
    private String nombre;
    private String especialidad;

    public Profesor(String id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    @Override
    public String toString() {
        return "ID: " + id + "\nNombre: " + nombre + "\nEspecialidad: " + especialidad;
    }
}