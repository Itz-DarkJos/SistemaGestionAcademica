package modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Grupo implements Serializable {
    private String idGrupo;
    private List<Materia> materias;  // Ahora lista de materias
    private Profesor profesor;
    private String horario;
    private boolean esPlanA; // Para distinguir Plan A/B
    private static final long serialVersionUID = 1L;

    // Constructor básico
    public Grupo(String idGrupo, boolean esPlanA) {
        this.idGrupo = idGrupo;
        this.esPlanA = esPlanA;
        this.materias = new ArrayList<>(); // <-- inicializar lista para evitar null
        this.profesor = null;
        this.horario = "";
    }

    // Getters
    public String getIdGrupo() { return idGrupo; }
    public List<Materia> getMaterias() { return materias; }
    public Profesor getProfesor() { return profesor; }
    public String getHorario() { return horario; }
    public boolean esPlanA() { return esPlanA; }

    // Setters
    public void setIdGrupo(String idGrupo) { this.idGrupo = idGrupo; }
    public void setMaterias(List<Materia> materias) { this.materias = materias; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }
    public void setHorario(String horario) { this.horario = horario; }
    public void setEsPlanA(boolean esPlanA) { this.esPlanA = esPlanA; }

    // Método para agregar materia sin repetir
    public boolean agregarMateria(Materia materia) {
        for (Materia m : materias) {
            if (m.getClave().equals(materia.getClave())) {
                return false; // ya existe esa materia
            }
        }
        materias.add(materia);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grupo: ").append(idGrupo).append("\n");
        sb.append("Modalidad: ").append(esPlanA ? "Plan A (fijo)" : "Plan B (editable)").append("\n");
        sb.append("Materias: ");
        if (materias.isEmpty()) {
            sb.append("No asignadas");
        } else {
            for (Materia m : materias) {
                sb.append("\n  - ").append(m.getNombre());
            }
        }
        sb.append("\nProfesor: ").append(profesor != null ? profesor.getNombre() : "No asignado").append("\n");
        sb.append("Horario: ").append(horario);
        return sb.toString();
    }
}
