package modelos;

public class Materia {
    private String clave;
    private String nombre;
    private int creditos;

    public Materia(String clave, String nombre, int creditos) {
        this.clave = clave;
        this.nombre = nombre;
        this.creditos = creditos;
    }

    // Getters y Setters
    public String getClave() { return clave; }
    public String getNombre() { return nombre; }
    public int getCreditos() { return creditos; }

    @Override
    public String toString() {
        return "Clave: " + clave + "\nNombre: " + nombre + "\nCréditos: " + creditos;
    }
}