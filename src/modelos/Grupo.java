package modelos;

import java.io.Serializable;

public class Grupo implements Serializable {
    private String idGrupo;
    private Materia materia;
    private Profesor profesor;
    private String horario;

    public Grupo(String idGrupo, Materia materia, Profesor profesor, String horario) {
        this.idGrupo = idGrupo;
        this.materia = materia;
        this.profesor = profesor;
        this.horario = horario;
    }

    // Getters
    public String getIdGrupo() {
        return idGrupo;
    }

    public Materia getMateria() {
        return materia;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public String getHorario() {
        return horario;
    }

    // Setters
    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return "Grupo: " + idGrupo + 
               "\nMateria: " + (materia != null ? materia.getNombre() : "No asignada") + 
               "\nProfesor: " + (profesor != null ? profesor.getNombre() : "No asignado") + 
               "\nHorario: " + horario;
    }
}