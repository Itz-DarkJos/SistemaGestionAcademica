package gestiones;

import modelos.Grupo;
import modelos.Materia;
import modelos.Profesor;
import utilidades.FileManager;
import utilidades.Validaciones;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class GestionGrupos {
    private static final String ARCHIVO_GRUPOS = "data/grupos.txt";
    private static List<Grupo> grupos = new ArrayList<>();

    public static void menuGrupos() {
        cargarGrupos();
        String[] opciones = {"Capturar Grupo", "Asignar Materia/Profesor", "Consultar Grupos", "Regresar"};
        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(null, "Gestión de Grupos\nSeleccione una opción:", 
                    "Grupos", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                    null, opciones, opciones[0]);
            
            switch(opcion) {
                case 0:
                    capturarGrupo();
                    break;
                case 1:
                    asignarMateriaProfesor();
                    break;
                case 2:
                    consultarGrupos();
                    break;
                case 3:
                    guardarGrupos();
                    break;
                default:
                    break;
            }
        } while (opcion != 3);
    }

    private static void capturarGrupo() {
        String idGrupo = JOptionPane.showInputDialog("Ingrese el ID del grupo:");
        if (idGrupo == null || idGrupo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El ID del grupo no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si el grupo ya existe
        if (buscarGrupo(idGrupo) != null) {
            JOptionPane.showMessageDialog(null, "Ya existe un grupo con este ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String horario = JOptionPane.showInputDialog("Ingrese el horario del grupo (Ej: Lunes 10:00-12:00):");
        if (horario == null || horario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El horario no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Grupo nuevoGrupo = new Grupo(idGrupo, null, null, horario);
        grupos.add(nuevoGrupo);
        JOptionPane.showMessageDialog(null, "Grupo creado con éxito. Ahora puede asignar materia y profesor.");
    }

    private static void asignarMateriaProfesor() {
        if (grupos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay grupos registrados", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idGrupo = JOptionPane.showInputDialog("Ingrese el ID del grupo a asignar:");
        if (idGrupo == null || idGrupo.trim().isEmpty()) {
            return;
        }

        Grupo grupo = buscarGrupo(idGrupo);
        if (grupo == null) {
            JOptionPane.showMessageDialog(null, "No se encontró el grupo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Asignar materia
        String claveMateria = JOptionPane.showInputDialog("Ingrese la clave de la materia a asignar:");
        if (claveMateria == null || claveMateria.trim().isEmpty()) {
            return;
        }

        Materia materia = GestionMaterias.buscarMateria(claveMateria);
        if (materia == null) {
            JOptionPane.showMessageDialog(null, "No se encontró la materia", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Asignar profesor
        String idProfesor = JOptionPane.showInputDialog("Ingrese el ID del profesor a asignar:");
        if (idProfesor == null || idProfesor.trim().isEmpty()) {
            return;
        }

        Profesor profesor = GestionProfesores.buscarProfesor(idProfesor);
        if (profesor == null) {
            JOptionPane.showMessageDialog(null, "No se encontró el profesor", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        grupo.setMateria(materia);
        grupo.setProfesor(profesor);
        JOptionPane.showMessageDialog(null, "Asignación realizada con éxito:\n" + grupo.toString());
    }

    private static void consultarGrupos() {
        if (grupos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay grupos registrados", "Consulta", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("=== LISTA DE GRUPOS ===\n\n");
        for (Grupo g : grupos) {
            sb.append(g.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Grupos Registrados", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean materiaEstaAsignada(String claveMateria) {
        for (Grupo g : grupos) {
            if (g.getMateria() != null && g.getMateria().getClave().equalsIgnoreCase(claveMateria)) {
                return true;
            }
        }
        return false;
    }

    public static Grupo buscarGrupo(String idGrupo) {
        for (Grupo g : grupos) {
            if (g.getIdGrupo().equalsIgnoreCase(idGrupo)) {
                return g;
            }
        }
        return null;
    }

    private static void cargarGrupos() {
        Object obj = FileManager.cargarDatos(ARCHIVO_GRUPOS);
        if (obj != null) {
            grupos = (List<Grupo>) obj;
        }
    }

    private static void guardarGrupos() {
        FileManager.guardarDatos(ARCHIVO_GRUPOS, grupos);
    }
}