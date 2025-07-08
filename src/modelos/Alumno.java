package modelos;

import java.io.Serializable;

public class Alumno implements Serializable {
    private static final long serialVersionUID = 1L;
    private String matricula;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int cuatrimestre;
    private String grupo;
    private String plan; // "A" o "B"

    public Alumno(String matricula, String nombre, String apellidoPaterno, 
                 String apellidoMaterno, int cuatrimestre, String grupo, String plan) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.cuatrimestre = cuatrimestre;
        this.grupo = grupo;
        this.plan = plan;
    }

    // Getters y Setters
    public String getMatricula() { return matricula; }
    public String getNombre() { return nombre; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public int getCuatrimestre() { return cuatrimestre; }
    public String getGrupo() { return grupo; }
    public String getPlan() { return plan; }

    public void setMatricula(String matricula) { this.matricula = matricula; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }
    public void setCuatrimestre(int cuatrimestre) { this.cuatrimestre = cuatrimestre; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    public void setPlan(String plan) { this.plan = plan; }

    @Override
public String toString() {
    return "Matrícula: " + matricula + "\n" +
           "Nombre: " + apellidoPaterno + " " + apellidoMaterno + " " + nombre + "\n" +
           "Cuatrimestre: " + cuatrimestre + "\n" +
           "Grupo: " + grupo + "\n" +
           "Plan: " + (plan.equals("A") ? "Plan A" : "Plan B");
}

}