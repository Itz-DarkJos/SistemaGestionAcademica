package gestiones;

import modelos.Profesor;
import utilidades.FileManager;
import utilidades.Validaciones;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;


public class GestionProfesores {
    private static final String ARCHIVO_PROFESORES = "profesores.txt";
    private static List<Profesor> profesores = new ArrayList<>();

    public static void menuProfesores() {
        cargarProfesores();
        String[] opciones = {"Capturar Profesor", "Consultar Profesores", "Borrar Profesor", "Regresar"};
        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(null, "Gestión de Profesores\nSeleccione una opción:", 
                    "Profesores", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                    null, opciones, opciones[0]);
            
            switch(opcion) {
                case 0:
                    capturarProfesor();
                    break;
                case 1:
                    consultarProfesores();
                    break;
                case 2:
                    borrarProfesor();
                    break;
                case 3:
                    guardarProfesores();
                    break;
                default:
                    break;
            }
        } while (opcion != 3);
    }

    private static void capturarProfesor() {
        String id = JOptionPane.showInputDialog("Ingrese el ID del profesor:");
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El ID no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar si el ID ya existe
        for (Profesor p : profesores) {
            if (p.getId().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(null, "Ya existe un profesor con este ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del profesor:");
        if (nombre == null || nombre.trim().isEmpty() || !Validaciones.esSoloLetras(nombre)) {
            JOptionPane.showMessageDialog(null, "Nombre no válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String especialidad = JOptionPane.showInputDialog("Ingrese la especialidad del profesor:");
        if (especialidad == null || especialidad.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La especialidad no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Profesor nuevoProfesor = new Profesor(id, nombre, especialidad);
        profesores.add(nuevoProfesor);
        JOptionPane.showMessageDialog(null, "Profesor registrado con éxito");
    }

    private static void consultarProfesores() {
        if (profesores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay profesores registrados", "Consulta", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Profesor p : profesores) {
            sb.append(p.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Profesores", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void borrarProfesor() {
        if (profesores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay profesores registrados", "Borrar", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String id = JOptionPane.showInputDialog("Ingrese el ID del profesor a borrar:");
        if (id == null || id.trim().isEmpty()) {
            return;
        }

        Profesor profesorABorrar = null;
        for (Profesor p : profesores) {
            if (p.getId().equalsIgnoreCase(id)) {
                profesorABorrar = p;
                break;
            }
        }

        if (profesorABorrar != null) {
            int confirmacion = JOptionPane.showConfirmDialog(null, 
                    "¿Está seguro que desea borrar al profesor?\n" + profesorABorrar.toString(), 
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                profesores.remove(profesorABorrar);
                JOptionPane.showMessageDialog(null, "Profesor eliminado con éxito");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un profesor con ese ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void cargarProfesores() {
        // Implementar carga desde archivo
    }

    private static void guardarProfesores() {
        // Implementar guardado a archivo
    }
    
    public static Profesor buscarProfesor(String id) {
    for (Profesor profesor : profesores) {
        if (profesor.getId().equalsIgnoreCase(id)) {
            return profesor;
        }
    }
    return null;
}
}